package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.model.ExecutionStatus.TERMINAL
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.orca.q.ConfigurationError
import com.netflix.spinnaker.orca.q.InvalidExecutionId
import com.netflix.spinnaker.q.Queue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ConfigurationErrorHandler(
  override val queue: Queue,
  override val repository: ExecutionRepository
) : OrcaMessageHandler<ConfigurationError> {

  override val messageType = ConfigurationError::class.java

  private val log = LoggerFactory.getLogger(javaClass)

  override fun handle(message: ConfigurationError) {
    when (message) {
      is InvalidExecutionId ->
        log.error("No such ${message.executionType} ${message.executionId} for ${message.application}")
      else -> {
        log.error("${message.javaClass.simpleName} for ${message.executionType} ${message.executionId} for ${message.application}")
        repository.updateStatus(message.executionType, message.executionId, TERMINAL)
      }
    }
  }
}
