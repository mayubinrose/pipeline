package com.netflix.spinnaker.orca.q

import com.ctg.pipeline.execute.model.Execution
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.model.SyntheticStageOwner
import com.ctg.pipeline.execute.model.Task
import com.ctg.pipeline.execute.pipeline.graph.StageGraphBuilder
import com.ctg.pipeline.execute.util.RestrictExecutionDuringTimeWindow
import com.ctg.pipeline.execute.util.StageDefinitionBuilder
import com.ctg.pipeline.execute.util.StageDefinitionBuilder.newStage
import com.ctg.pipeline.execute.util.TaskNode
import com.ctg.pipeline.execute.model.SyntheticStageOwner.STAGE_BEFORE


/**
 * Build and append the tasks for [stage].
 */
fun StageDefinitionBuilder.buildTasks(stage: Stage) {
  buildTaskGraph(stage)
    .listIterator()
    .forEachWithMetadata { processTaskNode(stage, it) }
}

fun StageDefinitionBuilder.addContextFlags(stage: Stage) {
  if (canManuallySkip()) {
    // Provides a flag for the UI to indicate that the stage can be skipped.
    stage.context["canManuallySkip"] = true
  }
}

private fun processTaskNode(
        stage: Stage,
        element: IteratorElement<TaskNode>,
        isSubGraph: Boolean = false
) {
  element.apply {
    when (value) {
      is TaskNode.TaskDefinition -> {
        val task = Task()
        task.id = (stage.tasks.size + 1).toString()
        task.name = value.name
        task.implementingClass = value.implementingClass.name
        if (isSubGraph) {
          task.isLoopStart = isFirst
          task.isLoopEnd = isLast
        } else {
          task.isStageStart = isFirst
          task.isStageEnd = isLast
        }
        stage.tasks.add(task)
      }
      is TaskNode.TaskGraph -> {
        value
          .listIterator()
          .forEachWithMetadata {
            processTaskNode(stage, it, isSubGraph = true)
          }
      }
    }
  }
}

/**
 * Build the synthetic stages for [stage] and inject them into the execution.
 */
fun StageDefinitionBuilder.buildBeforeStages(
  stage: Stage,
  callback: (Stage) -> Unit = {}
) {
  val executionWindow = stage.buildExecutionWindow()

  val graph = StageGraphBuilder.beforeStages(stage, executionWindow)
  beforeStages(stage, graph)
  val beforeStages = graph.build().toList()

  stage.execution.apply {
    beforeStages.forEach {
      it.sanitizeContext()
      injectStage(stages.indexOf(stage), it)
      callback.invoke(it)
    }
  }
}

fun StageDefinitionBuilder.buildAfterStages(
  stage: Stage,
  callback: (Stage) -> Unit = {}
) {
  val graph = StageGraphBuilder.afterStages(stage)
  afterStages(stage, graph)
  val afterStages = graph.build().toList()

  stage.appendAfterStages(afterStages, callback)
}

fun StageDefinitionBuilder.buildFailureStages(
  stage: Stage,
  callback: (Stage) -> Unit = {}
) {
  val graph = StageGraphBuilder.afterStages(stage)
  onFailureStages(stage, graph)
  val afterStages = graph.build().toList()

  stage.appendAfterStages(afterStages, callback)
}

fun Stage.appendAfterStages(
  afterStages: Iterable<Stage>,
  callback: (Stage) -> Unit = {}
) {
  val index = execution.stages.indexOf(this) + 1
  afterStages.reversed().forEach {
    it.sanitizeContext()
    execution.injectStage(index, it)
    callback.invoke(it)
  }
}

private typealias SyntheticStages = Map<SyntheticStageOwner, List<Stage>>

private fun Stage.buildExecutionWindow(): Stage? {
  if (context.getOrDefault("restrictExecutionDuringTimeWindow", false) as Boolean) {
    val execution = execution
    val executionWindow = newStage(
      execution,
      RestrictExecutionDuringTimeWindow.TYPE,
      RestrictExecutionDuringTimeWindow.TYPE,
      context.filterKeys { it !in setOf("restrictExecutionDuringTimeWindow", "stageTimeoutMs") },
      this,
      STAGE_BEFORE
    )
    executionWindow.refId = "$refId<0"
    return executionWindow
  } else {
    return null
  }
}

@Suppress("UNCHECKED_CAST")
private fun Execution.injectStage(index: Int, stage: Stage) {
  stages.add(index, stage)
}

private fun Stage.sanitizeContext() {
  if (type != RestrictExecutionDuringTimeWindow.TYPE) {
    context.apply {
      remove("restrictExecutionDuringTimeWindow")
      remove("restrictedExecutionWindow")
    }
  }
}
