package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.events.StageComplete
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.model.ExecutionStatus.FAILED_CONTINUE
import com.ctg.pipeline.execute.model.ExecutionStatus.NOT_STARTED
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING
import com.ctg.pipeline.execute.model.ExecutionStatus.SKIPPED
import com.ctg.pipeline.execute.model.ExecutionStatus.SUCCEEDED
import com.ctg.pipeline.execute.model.ExecutionStatus.TERMINAL
import com.ctg.pipeline.execute.queue.orca.handler.ext.isManuallySkipped
import com.ctg.pipeline.execute.queue.orca.handler.ext.recursiveSyntheticStages
import com.netflix.spinnaker.orca.q.SkipStage
import com.netflix.spinnaker.q.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class SkipStageHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        @Qualifier("queueEventPublisher") private val publisher: ApplicationEventPublisher,
        private val clock: Clock
) : OrcaMessageHandler<SkipStage> {
  override fun handle(message: SkipStage) {
    message.withStage { stage ->
      if (stage.status in setOf(RUNNING, NOT_STARTED) || stage.isManuallySkipped()) {
        stage.status = SKIPPED
        if (stage.isManuallySkipped()) {
          stage.recursiveSyntheticStages().forEach {
            if (it.status !in setOf(SUCCEEDED, TERMINAL, FAILED_CONTINUE)) {
              it.status = SKIPPED
              it.endTime = clock.millis()
              repository.storeStage(it)
              publisher.publishEvent(StageComplete(this, it))
            }
          }
        }
        stage.endTime = clock.millis()
        repository.storeStage(stage)
        stage.startNext()
        publisher.publishEvent(StageComplete(this, stage))
      }
    }
  }

  override val messageType = SkipStage::class.java
}
