package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.events.ExecutionComplete
import com.ctg.pipeline.execute.model.Execution
import com.ctg.pipeline.execute.model.ExecutionStatus
import com.ctg.pipeline.execute.model.ExecutionStatus.*
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.queue.orca.handler.ext.allUpstreamStagesComplete
import com.netflix.spectator.api.Registry
import com.netflix.spinnaker.orca.q.CancelStage
import com.netflix.spinnaker.orca.q.CompleteExecution
import com.netflix.spinnaker.orca.q.StartWaitingExecutions
import com.netflix.spinnaker.q.AttemptsAttribute
import com.netflix.spinnaker.q.Queue
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CompleteExecutionHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        @Qualifier("queueEventPublisher") private val publisher: ApplicationEventPublisher,
        private val registry: Registry,
        @Value("\${queue.retry.delay.ms:30000}") retryDelayMs: Long
) : OrcaMessageHandler<CompleteExecution> {

  private val log = LoggerFactory.getLogger(javaClass)
  private val retryDelay = Duration.ofMillis(retryDelayMs)
  private val completedId = registry.createId("executions.completed")

  override fun handle(message: CompleteExecution) {
    message.withExecution { execution ->
      if (execution.status.isComplete) {
        log.info("Execution ${execution.id} already completed with ${execution.status} status")
      } else {
        message.determineFinalStatus(execution) { status ->
          repository.updateStatus(execution.type, message.executionId, status)
          publisher.publishEvent(
            ExecutionComplete(this, message.executionType, message.executionId, status)
          )
          registry.counter(completedId.withTags(
            "status", status.name,
            "executionType", execution.type.name,
            "application", execution.application,
            "origin", execution.origin ?: "unknown"
          )).increment()
          if (status != SUCCEEDED) {
            execution.topLevelStages.filter { it.status == RUNNING }.forEach {
              queue.push(CancelStage(it))
            }
          }
        }
      }
      execution.pipelineConfigId?.let {
        queue.push(StartWaitingExecutions(it, purgeQueue = !execution.isKeepWaitingPipelines))
      }
    }
  }

  private fun CompleteExecution.determineFinalStatus(
          execution: Execution,
          block: (ExecutionStatus) -> Unit
  ) {
    execution.topLevelStages.let { stages ->
      if (stages.map { it.status }.all { it in setOf(SUCCEEDED, SKIPPED, FAILED_CONTINUE) }) {
        block.invoke(SUCCEEDED)
      } else if (stages.any { it.status == TERMINAL }) {
        block.invoke(TERMINAL)
      } else if (stages.any { it.status == CANCELED }) {
        block.invoke(CANCELED)
      } else if (stages.any { it.status == STOPPED } && !stages.otherBranchesIncomplete()) {
        block.invoke(if (execution.shouldOverrideSuccess()) TERMINAL else SUCCEEDED)
      } else {
        val attempts = getAttribute<AttemptsAttribute>()?.attempts ?: 0
        log.info("Re-queuing $this as the execution is not yet complete (attempts: $attempts)")
        queue.push(this, retryDelay)
      }
    }
  }

  private val Execution.topLevelStages
    get(): List<Stage> = stages.filter { it.parentStageId == null }

  private fun Execution.shouldOverrideSuccess(): Boolean =
    stages
      .filter { it.status == STOPPED }
      .any { it.context["completeOtherBranchesThenFail"] == true }

  private fun List<Stage>.otherBranchesIncomplete() =
    any { it.status == RUNNING } ||
      any { it.status == NOT_STARTED && it.allUpstreamStagesComplete() }

  override val messageType = CompleteExecution::class.java
}
