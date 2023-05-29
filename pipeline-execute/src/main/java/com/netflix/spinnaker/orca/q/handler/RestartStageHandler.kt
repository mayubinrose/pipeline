package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.model.ExecutionStatus.NOT_STARTED
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.queue.model.PipelineTrigger
import com.ctg.pipeline.execute.util.StageDefinitionBuilderFactory
import com.netflix.spinnaker.orca.q.RestartStage
import com.netflix.spinnaker.orca.q.StartStage
import com.netflix.spinnaker.orca.q.pending.PendingExecutionService
import com.netflix.spinnaker.q.Queue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class RestartStageHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        override val stageDefinitionBuilderFactory: StageDefinitionBuilderFactory,
        private val pendingExecutionService: PendingExecutionService,
        private val clock: Clock
) : OrcaMessageHandler<RestartStage>, StageBuilderAware {

  override val messageType = RestartStage::class.java

  private val log: Logger get() = LoggerFactory.getLogger(javaClass)

  override fun handle(message: RestartStage) {
    message.withStage { stage ->
      if (stage.execution.shouldQueue()) {
        // this pipeline is already running and has limitConcurrent = true
        stage.execution.pipelineConfigId?.let {
          log.info("Queueing restart of {} {} {}", stage.execution.application, stage.execution.name, stage.execution.id)
          pendingExecutionService.enqueue(it, message)
        }
      } else {
        // If RestartStage is requested for a synthetic stage, operate on its parent
        val topStage = stage.topLevelStage
        val startMessage = StartStage(message.executionType, message.executionId, message.application, topStage.id)
        if (topStage.status.isComplete) {
          topStage.addRestartDetails(message.user)
          topStage.reset()
          restartParentPipelineIfNeeded(message, topStage)
          repository.updateStatus(topStage.execution.type, topStage.execution.id, RUNNING)
          queue.push(StartStage(startMessage))
        }
      }
    }
  }

  private fun restartParentPipelineIfNeeded(message: RestartStage, topStage: Stage) {
    if (topStage.execution.trigger !is PipelineTrigger) {
      return
    }

    val trigger = topStage.execution.trigger as PipelineTrigger
    // We have a copy of the parent execution, not the live one. So we retrieve the live one.
    val parentExecution = repository.retrieve(trigger.parentExecution.type, trigger.parentExecution.id)

    if (!parentExecution.status.isComplete()) {
      // only attempt to restart the parent pipeline if it's not running
      return
    }

    val parentStage = parentExecution.stageById(trigger.parentPipelineStageId)
    parentStage.addSkipRestart()
    repository.storeStage(parentStage)

    queue.push(RestartStage(trigger.parentExecution, parentStage.id, message.user))
  }

  /**
   * Inform the parent stage when it restarts that the child is already running
   */
  private fun Stage.addSkipRestart() {
    context["_skipPipelineRestart"] = true
  }

  private fun Stage.addRestartDetails(user: String?) {
    context["restartDetails"] = mapOf(
      "restartedBy" to (user ?: "anonymous"),
      "restartTime" to clock.millis(),
      "previousException" to context.remove("exception")
    )
  }

  private fun Stage.reset() {
    if (status.isComplete) {
      status = NOT_STARTED
      startTime = null
      endTime = null
      tasks = emptyList()
      builder().prepareStageForRestart(this)
      repository.storeStage(this)

      removeSynthetics()
    }

    downstreamStages().forEach { it.reset() }
  }

  private fun Stage.removeSynthetics() {
    execution
      .stages
      .filter { it.parentStageId == id }
      .forEach {
        it.removeSynthetics()
        repository.removeStage(execution, it.id)
      }
  }
}