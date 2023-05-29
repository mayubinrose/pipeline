package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.q.Queue
import com.ctg.pipeline.execute.model.ExecutionStatus.PAUSED
import com.netflix.spinnaker.orca.q.PauseStage
import org.springframework.stereotype.Component

@Component
class PauseStageHandler(
  override val queue: Queue,
  override val repository: ExecutionRepository
) : OrcaMessageHandler<PauseStage> {

  override val messageType = PauseStage::class.java

  override fun handle(message: PauseStage) {
    message.withStage { stage ->
      stage.status = PAUSED
      repository.storeStage(stage)
      stage.parentStageId?.let { parentStageId ->
        queue.push(PauseStage(message, parentStageId))
      }
    }
  }
}
