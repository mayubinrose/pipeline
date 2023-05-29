package com.ctg.pipeline.execute.queue.config

import com.netflix.spinnaker.q.NoopQueue
import com.netflix.spinnaker.q.Queue
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.Clock

@Configuration
@ComponentScan(basePackages = [
  "com.ctg.pipeline.execute.queue.orca",
  "com.ctg.pipeline.execute.queue.orca.handler"
])
@EnableScheduling
open class OrcaQueueConfiguration {
  @Bean
  @ConditionalOnMissingBean(Clock::class)
  open fun systemClock(): Clock = Clock.systemDefaultZone()

  @Bean
  @ConditionalOnMissingBean(Queue::class)
  open fun queue(): Queue = NoopQueue()
}
