package com.ctg.pipeline.execute.notifications;

import javax.validation.constraints.NotNull;

/**
 * @author zhiHuang
 * @date 2022/11/15 16:44
 **/
public interface NotificationClusterLock {

    boolean tryAcquireLock(String notificationType, long lockTimeoutSeconds);

}