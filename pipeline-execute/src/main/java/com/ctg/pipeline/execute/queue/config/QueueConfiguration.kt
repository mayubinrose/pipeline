package com.ctg.pipeline.execute.queue.config

import com.ctg.pipeline.execute.queue.config.properties.EnabledActivator
import com.ctg.pipeline.execute.queue.config.properties.QueueProperties
import com.netflix.spectator.api.Registry
import com.netflix.spinnaker.config.ObjectMapperSubtypeProperties
import com.netflix.spinnaker.q.*
import com.netflix.spinnaker.q.metrics.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.time.Clock
import java.time.Duration

@Configuration
@EnableConfigurationProperties(QueueProperties::class, ObjectMapperSubtypeProperties::class)
@ComponentScan(basePackages = ["com.netflix.spinnaker.q"])
@EnableScheduling
open class QueueConfiguration {

  @Bean
  @ConditionalOnMissingBean(Clock::class)
  open fun systemClock(): Clock = Clock.systemDefaultZone()

  @Bean
  open fun messageHandlerPool(queueProperties: QueueProperties) =
          ThreadPoolTaskExecutor().apply {
            threadNamePrefix = queueProperties.handlerThreadNamePrefix
            corePoolSize = queueProperties.handlerCorePoolSize
            maxPoolSize = queueProperties.handlerMaxPoolSize
            setQueueCapacity(0)
          }

  @Bean
  @ConditionalOnMissingBean(QueueExecutor::class)
  open fun queueExecutor(messageHandlerPool: ThreadPoolTaskExecutor) =
          object : QueueExecutor<ThreadPoolTaskExecutor>(messageHandlerPool) {
            override fun hasCapacity() =
                    executor.threadPoolExecutor.run {
                      activeCount < maximumPoolSize
                    }

            override fun availableCapacity() =
                    executor.threadPoolExecutor.run {
                      maximumPoolSize - activeCount
                    }
          }

  @Bean
  open fun queueProcessor(
          queue: Queue,
          executor: QueueExecutor<*>,
          handlers: Collection<MessageHandler<*>>,
          activators: List<Activator>,
          publisher: EventPublisher,
          queueProperties: QueueProperties,
          deadMessageHandler: DeadMessageCallback
  ) = QueueProcessor(
          queue,
          executor,
          handlers,
          activators,
          publisher,
          deadMessageHandler,
          queueProperties.fillExecutorEachCycle,
          Duration.ofSeconds(queueProperties.requeueDelaySeconds),
          Duration.ofSeconds(queueProperties.requeueMaxJitterSeconds)
  )

  @Bean
  open fun enabledActivator(queueProperties: QueueProperties) = EnabledActivator(queueProperties.enabled)

  @Bean
  @ConditionalOnProperty("queue.metrics.enabled", havingValue = "true", matchIfMissing = true)
  open fun queueMonitor(
          registry: Registry,
          clock: Clock,
          queue: MonitorableQueue
  ) = QueueMonitor(registry, clock, queue)

  @Bean
  @ConditionalOnProperty("queue.metrics.enabled", havingValue = "true", matchIfMissing = true)
  open fun queueMetricsPublisher(
          registry: Registry,
          clock: Clock
  ): EventPublisher =
          QueueMetricsPublisher(registry, clock)

  @Bean
  @ConditionalOnMissingBean(EventPublisher::class)
  open fun queueEventPublisher(): EventPublisher =
          NoopEventPublisher()
}
