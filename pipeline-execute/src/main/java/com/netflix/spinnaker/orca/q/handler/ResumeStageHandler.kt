package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.model.ExecutionStatus.PAUSED
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.orca.q.ResumeStage
import com.netflix.spinnaker.orca.q.ResumeTask
import com.netflix.spinnaker.q.Queue
import org.springframework.stereotype.Component

@Component
class ResumeStageHandler(
  override val queue: Queue,
  override val repository: ExecutionRepository
) : OrcaMessageHandler<ResumeStage> {

  override val messageType = ResumeStage::class.java

  override fun handle(message: ResumeStage) {
    message.withStage { stage ->
      stage.status = RUNNING
      repository.storeStage(stage)

      stage
        .tasks
        .filter { it.status == PAUSED }
        .forEach { queue.push(ResumeTask(message, it.id)) }
    }
  }
}
