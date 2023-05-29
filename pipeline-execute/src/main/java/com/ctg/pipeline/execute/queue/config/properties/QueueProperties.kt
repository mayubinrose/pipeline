package com.ctg.pipeline.execute.queue.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("keiko.queue")
class QueueProperties {
    var enabled: Boolean = true
    var handlerThreadNamePrefix: String = "handlers-"
    var handlerCorePoolSize: Int = 20
    var handlerMaxPoolSize: Int = 20
    var fillExecutorEachCycle: Boolean = false
    var requeueDelaySeconds: Long = 0
    var requeueMaxJitterSeconds: Long = 0
}
