package com.netflix.spinnaker.orca.q

import com.netflix.spinnaker.q.QueueExecutor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class ThreadPoolQueueExecutor(
  @Qualifier("messageHandlerPool") executor: ThreadPoolTaskExecutor
) : QueueExecutor<ThreadPoolTaskExecutor>(executor) {
  override fun hasCapacity() =
    executor.threadPoolExecutor.run {
      activeCount < maximumPoolSize
    }

  override fun availableCapacity() =
    executor.threadPoolExecutor.run {
      maximumPoolSize - activeCount
    }
}
