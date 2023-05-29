package com.ctg.pipeline.execute.config;

import com.ctg.pipeline.execute.notifications.NotificationClusterLock;
import com.ctg.pipeline.execute.notifications.RedisClusterNotificationClusterLock;
import com.ctg.pipeline.execute.notifications.RedisNotificationClusterLock;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.ctg.pipeline.execute.pipeline.persistence.jedis.RedisExecutionRepository;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.netflix.spectator.api.Registry;
import com.netflix.spinnaker.kork.jedis.JedisClientConfiguration;
import com.netflix.spinnaker.kork.jedis.RedisClientSelector;

import redis.clients.jedis.JedisCluster;
import rx.Scheduler;

@Configuration
@Import({JedisClientConfiguration.class, JedisConfiguration.class})
public class RedisConfiguration {

  public static class Clients {
    public static final String EXECUTION_REPOSITORY = "executionRepository";
    public static final String TASK_QUEUE = "taskQueue";
  }

  @Bean
  @ConditionalOnProperty(value = "execution-repository.redis.enabled", matchIfMissing = true)
  public ExecutionRepository redisExecutionRepository(
          Registry registry,
      RedisClientSelector redisClientSelector,
      @Qualifier("queryAllScheduler") Scheduler queryAllScheduler,
      @Qualifier("queryByAppScheduler") Scheduler queryByAppScheduler,
      @Value("${chunk-size.execution-repository:75}") Integer threadPoolChunkSize,
      @Value("${keiko.queue.redis.queue-name:}") String bufferedPrefix) {
    return new RedisExecutionRepository(
            registry,
            redisClientSelector,
            queryAllScheduler,
            queryByAppScheduler,
            threadPoolChunkSize,
            bufferedPrefix);
  }

  @Bean
  @ConditionalOnProperty(
      value = "redis.cluster-enabled",
      havingValue = "false",
      matchIfMissing = true)
  @ConditionalOnMissingBean(NotificationClusterLock.class)
  public NotificationClusterLock redisNotificationClusterLock(
      RedisClientSelector redisClientSelector) {
    return new RedisNotificationClusterLock(redisClientSelector);
  }

  @Bean
  @ConditionalOnProperty(value = "redis.cluster-enabled")
  @ConditionalOnMissingBean(NotificationClusterLock.class)
  public NotificationClusterLock redisClusterNotificationClusterLock(JedisCluster cluster) {
    return new RedisClusterNotificationClusterLock(cluster);
  }

  @Bean
  @ConfigurationProperties("redis")
  public GenericObjectPoolConfig redisPoolConfig() {
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setMaxTotal(100);
    config.setMaxIdle(100);
    config.setMinIdle(25);
    return config;
  }
}
