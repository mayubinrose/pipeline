package com.ctg.pipeline.execute.notifications;

import com.netflix.spinnaker.kork.jedis.RedisClientDelegate;
import com.netflix.spinnaker.kork.jedis.RedisClientSelector;

import redis.clients.jedis.params.SetParams;

public class RedisNotificationClusterLock implements NotificationClusterLock {

  private final RedisClientDelegate redisClientDelegate;

  public RedisNotificationClusterLock(RedisClientSelector redisClientSelector) {
    this.redisClientDelegate = redisClientSelector.primary("default");
  }

  @Override
  public boolean tryAcquireLock(String notificationType, long lockTimeoutSeconds) {
    String key = "lock:" + notificationType;
    return redisClientDelegate.withCommandsClient(
        client -> {
          return "OK"
              .equals(
                  client
                      // assuming lockTimeoutSeconds will be < 2147483647
                      .set(
                      key,
                      "\uD83D\uDD12",
                      SetParams.setParams().nx().ex((int) lockTimeoutSeconds)));
        });
  }
}
