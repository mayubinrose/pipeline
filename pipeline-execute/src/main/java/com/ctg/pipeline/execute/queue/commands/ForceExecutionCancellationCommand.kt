package com.ctg.pipeline.execute.queue.commands

import com.ctg.pipeline.execute.model.Execution
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.model.ExecutionStatus.NOT_STARTED
import com.ctg.pipeline.execute.model.ExecutionStatus.CANCELED
import org.slf4j.LoggerFactory
import java.time.Clock

class ForceExecutionCancellationCommand(
        private val executionRepository: ExecutionRepository,
        private val clock: Clock
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun forceCancel(executionType: Execution.ExecutionType, executionId: String, canceledBy: String) {
    log.info("Forcing cancel of $executionType:$executionId by: $canceledBy")
    val execution = executionRepository.retrieve(executionType, executionId)

    if (forceCancel(execution, canceledBy)) {
      executionRepository.store(execution)
    }
  }

  private fun forceCancel(execution: Execution, canceledBy: String): Boolean {
    val now = clock.instant().toEpochMilli()

    var changes = false
    execution.stages
      .filter { !it.status.isComplete && it.status != NOT_STARTED }
      .forEach { stage ->
        stage.tasks.forEach { task ->
          task.status = CANCELED
          task.endTime = now
        }
        stage.status = CANCELED
        stage.endTime = now

        changes = true
      }

    if (!execution.status.isComplete) {
      execution.status = CANCELED
      execution.canceledBy = canceledBy
      execution.cancellationReason = "Force canceled by admin"
      execution.endTime = now

      changes = true
    }

    return changes
  }
}
