package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.events.StageComplete
import com.ctg.pipeline.execute.model.ExecutionStatus.NOT_STARTED
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING
import com.ctg.pipeline.execute.model.ExecutionStatus.TERMINAL
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.q.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.Clock
import com.ctg.pipeline.execute.queue.orca.handler.ext.parent
import com.netflix.spinnaker.orca.q.AbortStage
import com.netflix.spinnaker.orca.q.CancelStage
import com.netflix.spinnaker.orca.q.CompleteExecution
import com.netflix.spinnaker.orca.q.CompleteStage

@Component
class AbortStageHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        @Qualifier("queueEventPublisher") private val publisher: ApplicationEventPublisher,
        private val clock: Clock
) : OrcaMessageHandler<AbortStage> {

  override fun handle(message: AbortStage) {
    message.withStage { stage ->
      if (stage.status in setOf(RUNNING, NOT_STARTED)) {
        stage.status = TERMINAL
        stage.endTime = clock.millis()
        repository.storeStage(stage)
        queue.push(CancelStage(message))
        if (stage.parentStageId == null) {
          queue.push(CompleteExecution(message))
        } else {
          queue.push(CompleteStage(stage.parent()))
        }
        publisher.publishEvent(StageComplete(this, stage))
      }
    }
  }

  override val messageType = AbortStage::class.java
}
