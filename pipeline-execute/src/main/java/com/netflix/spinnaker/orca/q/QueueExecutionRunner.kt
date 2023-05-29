package com.netflix.spinnaker.orca.q

import com.ctg.pipeline.execute.model.Execution
import com.ctg.pipeline.execute.queue.orca.*
import com.ctg.pipeline.execute.util.ExecutionRunner
import com.netflix.spinnaker.q.Queue
import org.springframework.stereotype.Component

@Component
class QueueExecutionRunner(
  private val queue: Queue
) : ExecutionRunner {

  override fun start(execution: Execution) =
    queue.push(StartExecution(execution))

  override fun reschedule(execution: Execution) {
    queue.push(RescheduleExecution(execution))
  }

  override fun restart(execution: Execution, stageId: String) {
    queue.push(RestartStage(execution, stageId, null))
  }

  override fun unpause(execution: Execution) {
    queue.push(ResumeExecution(execution))
  }

  override fun cancel(execution: Execution, user: String, reason: String?) {
    queue.push(CancelExecution(execution, user, reason))
  }
}
