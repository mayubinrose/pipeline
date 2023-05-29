package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.basic.task.TaskResolver
import com.ctg.pipeline.execute.model.ExecutionStatus.PAUSED
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.orca.q.ResumeTask
import com.netflix.spinnaker.orca.q.RunTask
import com.netflix.spinnaker.q.Queue
import org.springframework.stereotype.Component

@Component
class ResumeTaskHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        private val taskResolver: TaskResolver
) : OrcaMessageHandler<ResumeTask> {

  override val messageType = ResumeTask::class.java

  override fun handle(message: ResumeTask) {
    message.withStage { stage ->
      stage
        .tasks
        .filter { it.status == PAUSED }
        .forEach {
          it.status = RUNNING
          queue.push(RunTask(message, it.type))
        }
      repository.storeStage(stage)
    }
  }

  @Suppress("UNCHECKED_CAST")
  private val com.ctg.pipeline.execute.model.Task.type
    get() = taskResolver.getTaskClass(implementingClass)
}
