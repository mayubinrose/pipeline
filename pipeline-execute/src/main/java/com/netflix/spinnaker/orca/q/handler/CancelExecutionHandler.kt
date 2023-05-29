package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.events.ExecutionComplete
import com.ctg.pipeline.execute.model.ExecutionStatus.CANCELED
import com.ctg.pipeline.execute.model.ExecutionStatus.PAUSED
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.orca.q.CancelExecution
import com.netflix.spinnaker.orca.q.RescheduleExecution
import com.netflix.spinnaker.orca.q.ResumeStage
import com.netflix.spinnaker.q.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CancelExecutionHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        @Qualifier("queueEventPublisher")private val publisher: ApplicationEventPublisher
) : OrcaMessageHandler<CancelExecution> {
  override val messageType = CancelExecution::class.java

  override fun handle(message: CancelExecution) {
    message.withExecution { execution ->
      repository.cancel(execution.type, execution.id, message.user, message.reason)

      // Resume any paused stages so that their RunTaskHandler gets executed
      // and handles the `canceled` flag.
      execution
        .stages
        .filter { it.status == PAUSED }
        .forEach { stage ->
          queue.push(ResumeStage(stage))
        }

      // then, make sure those runTask messages get run right away
      queue.push(RescheduleExecution(execution))

      publisher.publishEvent(ExecutionComplete(this, message.executionType, message.executionId, CANCELED))
    }
  }
}
