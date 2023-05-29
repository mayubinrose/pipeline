package com.ctg.pipeline.execute.queue.orca.handler.ext

import com.ctg.pipeline.execute.model.ExecutionStatus
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.model.Task
import com.ctg.pipeline.execute.model.ExecutionStatus.*;
import com.ctg.pipeline.execute.model.SyntheticStageOwner.*;

/**
 * @return the stage's first before stage or `null` if there are none.
 */
fun Stage.firstBeforeStages(): List<Stage> =
  beforeStages().filter { it.isInitial() }

/**
 * @return the stage's first after stage or `null` if there are none.
 */
fun Stage.firstAfterStages(): List<Stage> =
  afterStages().filter { it.isInitial() }

fun Stage.isInitial(): Boolean =
  requisiteStageRefIds.isEmpty()

/**
 * @return the stage's first task or `null` if there are none.
 */
fun Stage.firstTask(): Task? = tasks.firstOrNull()

/**
 * @return the stage's parent stage.
 * @throws IllegalStateException if the stage is not synthetic.
 */
fun Stage.parent(): Stage =
  execution
    .stages
    .find { it.id == parentStageId }
    ?: throw IllegalStateException("Not a synthetic stage")

/**
 * @return the task that follows [task] or `null` if [task] is the end of the
 * stage.
 */
fun Stage.nextTask(task: Task): Task? =
  if (task.isStageEnd) {
    null
  } else {
    val index = tasks.indexOf(task)
    tasks[index + 1]
  }

/**
 * @return all stages directly upstream of this stage.
 */
fun Stage.upstreamStages(): List<Stage> =
  execution.stages.filter { it.refId in requisiteStageRefIds }

/**
 * @return `true` if all upstream stages of this stage were run successfully.
 */
fun Stage.allUpstreamStagesComplete(): Boolean =
  upstreamStages().all { it.status in listOf(SUCCEEDED, FAILED_CONTINUE, SKIPPED) }

fun Stage.anyUpstreamStagesFailed(): Boolean =
  upstreamStages().any { it.status in listOf(TERMINAL, STOPPED, CANCELED) || it.status == NOT_STARTED && it.anyUpstreamStagesFailed() }

fun Stage.syntheticStages(): List<Stage> =
  execution.stages.filter { it.parentStageId == id }

fun Stage.recursiveSyntheticStages(): List<Stage> =
  syntheticStages() + syntheticStages().flatMap {
    it.recursiveSyntheticStages()
  }

fun Stage.beforeStages(): List<Stage> =
  syntheticStages().filter { it.syntheticStageOwner == STAGE_BEFORE }

fun Stage.afterStages(): List<Stage> =
  syntheticStages().filter { it.syntheticStageOwner == STAGE_AFTER }

fun Stage.allBeforeStagesSuccessful(): Boolean =
  beforeStages().all { it.status in listOf(SUCCEEDED, FAILED_CONTINUE, SKIPPED) }

fun Stage.allAfterStagesSuccessful(): Boolean =
  afterStages().all { it.status in listOf(SUCCEEDED, FAILED_CONTINUE, SKIPPED) }

fun Stage.anyBeforeStagesFailed(): Boolean =
  beforeStages().any { it.status in listOf(TERMINAL, STOPPED, CANCELED) }

fun Stage.anyAfterStagesFailed(): Boolean =
  afterStages().any { it.status in listOf(TERMINAL, STOPPED, CANCELED) }

fun Stage.allAfterStagesComplete(): Boolean =
  afterStages().all { it.status.isComplete }

fun Stage.hasTasks(): Boolean =
  tasks.isNotEmpty()

fun Stage.hasAfterStages(): Boolean =
  firstAfterStages().isNotEmpty()

inline fun <reified O> Stage.mapTo(pointer: String): O = mapTo(pointer, O::class.java)

inline fun <reified O> Stage.mapTo(): O = mapTo(O::class.java)

fun Stage.shouldFailPipeline(): Boolean =
  context["failPipeline"] in listOf(null, true)

fun Stage.failureStatus(default: ExecutionStatus = TERMINAL) =
  when {
    continuePipelineOnFailure -> FAILED_CONTINUE
    shouldFailPipeline() -> default
    else -> STOPPED
  }

fun Stage.isManuallySkipped(): Boolean {
  return context["manualSkip"] == true || parent?.isManuallySkipped() == true
}
