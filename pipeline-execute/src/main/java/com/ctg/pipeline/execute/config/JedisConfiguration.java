package com.ctg.pipeline.execute.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import rx.Scheduler;
import rx.schedulers.Schedulers;

@Configuration
public class JedisConfiguration {

  @Bean("QueryAll")
  public ThreadPoolTaskExecutor queryAll() {
    return newFixedThreadPool(10);
  }

  @Bean
  public Scheduler queryAllScheduler(@Qualifier("QueryAll") ThreadPoolTaskExecutor executor) {
    return Schedulers.from(executor);
  }

  @Bean("QueryByApp")
  public ThreadPoolTaskExecutor queryByApp(
      @Value("${thread-pool.execution-repository:150}") int threadPoolSize) {
    return newFixedThreadPool(threadPoolSize);
  }

  @Bean
  public Scheduler queryByAppScheduler(@Qualifier("QueryByApp") ThreadPoolTaskExecutor executor) {
    return Schedulers.from(executor);
  }

  private static ThreadPoolTaskExecutor newFixedThreadPool(int threadPoolSize) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(threadPoolSize);
    executor.setMaxPoolSize(threadPoolSize);
    return executor;
  }
}
