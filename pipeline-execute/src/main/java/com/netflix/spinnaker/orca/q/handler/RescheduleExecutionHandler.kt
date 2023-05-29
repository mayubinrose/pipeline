package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.basic.task.TaskResolver
import com.ctg.pipeline.execute.model.ExecutionStatus
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.netflix.spinnaker.orca.q.RescheduleExecution
import com.netflix.spinnaker.orca.q.RunTask
import com.netflix.spinnaker.q.Queue
import org.springframework.stereotype.Component

@Component
class RescheduleExecutionHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        private val taskResolver: TaskResolver
) : OrcaMessageHandler<RescheduleExecution> {

  override val messageType = RescheduleExecution::class.java

  @Suppress("UNCHECKED_CAST")
  override fun handle(message: RescheduleExecution) {
    message.withExecution { execution ->
      execution
        .stages
        .filter { it.status == ExecutionStatus.RUNNING }
        .forEach { stage ->
          stage.tasks
            .filter { it.status == ExecutionStatus.RUNNING }
            .forEach {
              queue.reschedule(RunTask(message,
                stage.id,
                it.id,
                taskResolver.getTaskClass(it.implementingClass)
              ))
            }
        }
    }
  }
}
