package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.events.TaskComplete
import com.ctg.pipeline.execute.model.ExecutionStatus
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.util.ContextParameterProcessor
import com.ctg.pipeline.execute.util.StageDefinitionBuilderFactory
import com.netflix.spectator.api.BasicTag
import com.netflix.spectator.api.Registry
import com.ctg.pipeline.execute.model.ExecutionStatus.*
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.model.Task
import com.ctg.pipeline.execute.queue.orca.*
import com.ctg.pipeline.execute.queue.orca.handler.ext.isManuallySkipped
import com.ctg.pipeline.execute.queue.orca.handler.ext.nextTask
import com.netflix.spinnaker.orca.q.*
import com.netflix.spinnaker.orca.q.metrics.MetricsTagHelper
import com.netflix.spinnaker.q.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.Clock
import java.util.concurrent.TimeUnit

@Component
class CompleteTaskHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        override val stageDefinitionBuilderFactory: StageDefinitionBuilderFactory,
        override val contextParameterProcessor: ContextParameterProcessor,
        @Qualifier("queueEventPublisher") private val publisher: ApplicationEventPublisher,
        private val clock: Clock,
        private val registry: Registry
) : OrcaMessageHandler<CompleteTask>, ExpressionAware {

  override fun handle(message: CompleteTask) {
    message.withTask { stage, task ->
      task.status = message.status
      task.endTime = clock.millis()
      val mergedContextStage = stage.withMergedContext()
      trackResult(stage, task, message.status)

      if (message.status == REDIRECT) {
        mergedContextStage.handleRedirect()
      } else {
        repository.storeStage(mergedContextStage)

        if (stage.isManuallySkipped()) {
          queue.push(SkipStage(stage.topLevelStage))
        } else if (shouldCompleteStage(task, message.status, message.originalStatus)) {
          queue.push(CompleteStage(message))
        } else {
          mergedContextStage.nextTask(task).let {
            if (it == null) {
              queue.push(NoDownstreamTasks(message))
            } else {
              queue.push(StartTask(message, it.id))
            }
          }
        }

        publisher.publishEvent(TaskComplete(this, mergedContextStage, task))
      }
    }
  }

  fun shouldCompleteStage(task: Task, status: ExecutionStatus, originalStatus: ExecutionStatus?): Boolean {
    if (task.isStageEnd) {
      // last task in the stage
      return true
    }

    if (originalStatus == FAILED_CONTINUE) {
      // the task explicitly returned FAILED_CONTINUE and _should_ run subsequent tasks
      return false
    }

    // the task was not successful and _should not_ run subsequent tasks
    return status != SUCCEEDED
  }

  override val messageType = CompleteTask::class.java

  private fun Stage.handleRedirect() {
    tasks.let { tasks ->
      val start = tasks.indexOfFirst { it.isLoopStart }
      val end = tasks.indexOfLast { it.isLoopEnd }
      tasks[start..end].forEach {
        it.endTime = null
        it.status = NOT_STARTED
      }
      repository.storeStage(this)
      queue.push(StartTask(execution.type, execution.id, execution.application, id, tasks[start].id))
    }
  }

  private fun trackResult(stage: Stage, taskModel: com.ctg.pipeline.execute.model.Task, status: ExecutionStatus) {
    val commonTags = MetricsTagHelper.commonTags(stage, taskModel, status)
    val detailedTags = MetricsTagHelper.detailedTaskTags(stage, taskModel, status)

    // we are looking at the time it took to complete the whole execution, not just one invocation
    val elapsedMillis = clock.millis() - (taskModel.startTime ?: 0)

    hashMapOf(
      "task.completions.duration" to commonTags + BasicTag("application", stage.execution.application),
      "task.completions.duration.withType" to commonTags + detailedTags
    ).forEach {
      name, tags -> registry.timer(name, tags).record(elapsedMillis, TimeUnit.MILLISECONDS)
    }
  }
}
