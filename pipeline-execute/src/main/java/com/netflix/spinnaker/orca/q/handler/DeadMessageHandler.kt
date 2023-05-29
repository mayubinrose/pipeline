package com.netflix.spinnaker.orca.q.handler

import com.fasterxml.jackson.annotation.JsonTypeName
import com.ctg.pipeline.execute.model.ExecutionStatus.TERMINAL
import com.ctg.pipeline.execute.queue.orca.*
import com.netflix.spinnaker.orca.q.*
import com.netflix.spinnaker.q.Attribute
import com.netflix.spinnaker.q.DeadMessageCallback
import com.netflix.spinnaker.q.Message
import com.netflix.spinnaker.q.Queue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component class DeadMessageHandler : DeadMessageCallback {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun invoke(queue: Queue, message: Message) {
    log.error("Dead message: $message")
    terminationMessageFor(message)
      ?.let {
        it.setAttribute(DeadMessageAttribute)
        queue.push(it)
      }
  }

  private fun terminationMessageFor(message: Message): Message? {
    if (message.getAttribute<DeadMessageAttribute>() != null) {
      log.warn("Already sent $message to DLQ")
      return null
    }
    return when (message) {
      is TaskLevel -> CompleteTask(message, TERMINAL)
      is StageLevel -> AbortStage(message)
      is ExecutionLevel -> CompleteExecution(message)
      else -> {
        log.error("Unhandled message type ${message.javaClass}")
        null
      }
    }
  }
}

@JsonTypeName("deadMessage") object DeadMessageAttribute : Attribute
