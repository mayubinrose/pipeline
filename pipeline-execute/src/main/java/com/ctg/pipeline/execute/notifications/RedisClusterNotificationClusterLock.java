package com.ctg.pipeline.execute.notifications;


import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

public class RedisClusterNotificationClusterLock implements NotificationClusterLock {

  private final JedisCluster cluster;

  public RedisClusterNotificationClusterLock(JedisCluster cluster) {
    this.cluster = cluster;
  }

  @Override
  public boolean tryAcquireLock(String notificationType, long lockTimeoutSeconds) {
    String key = "lock:" + notificationType;
    // assuming lockTimeoutSeconds will be < 2147483647
    return "OK"
        .equals(
            cluster.set(
                key, "\uD83D\uDD12", SetParams.setParams().nx().ex((int) lockTimeoutSeconds)));
  }
}
