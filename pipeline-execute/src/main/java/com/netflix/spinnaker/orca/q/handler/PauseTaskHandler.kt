package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.model.ExecutionStatus.PAUSED
import com.netflix.spinnaker.orca.q.PauseStage
import com.netflix.spinnaker.orca.q.PauseTask
import com.netflix.spinnaker.q.Queue
import org.springframework.stereotype.Component

@Component
class PauseTaskHandler(
  override val queue: Queue,
  override val repository: ExecutionRepository
) : OrcaMessageHandler<PauseTask> {

  override val messageType = PauseTask::class.java

  override fun handle(message: PauseTask) {
    message.withTask { stage, task ->
      task.status = PAUSED
      repository.storeStage(stage)
      queue.push(PauseStage(message))
    }
  }
}
