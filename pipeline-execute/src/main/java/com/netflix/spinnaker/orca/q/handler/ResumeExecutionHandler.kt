package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.model.ExecutionStatus.PAUSED
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.orca.q.ResumeExecution
import com.netflix.spinnaker.orca.q.ResumeStage
import com.netflix.spinnaker.q.Queue
import org.springframework.stereotype.Component

@Component
class ResumeExecutionHandler(
  override val queue: Queue,
  override val repository: ExecutionRepository
) : OrcaMessageHandler<ResumeExecution> {

  override val messageType = ResumeExecution::class.java

  override fun handle(message: ResumeExecution) {
    message.withExecution { execution ->
      execution
        .stages
        .filter { it.status == PAUSED }
        .forEach { queue.push(ResumeStage(message, it.id)) }
    }
  }
}
