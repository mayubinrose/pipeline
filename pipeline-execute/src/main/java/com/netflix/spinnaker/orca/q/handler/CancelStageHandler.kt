package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.basic.task.TaskResolver
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.util.StageDefinitionBuilderFactory
import com.ctg.pipeline.execute.util.StageNavigator
import com.netflix.spinnaker.q.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.concurrent.Executor
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING
import com.ctg.pipeline.execute.model.Execution.ExecutionType.PIPELINE
import com.ctg.pipeline.execute.util.CancellableStage
import com.netflix.spinnaker.orca.q.CancelStage
import com.netflix.spinnaker.orca.q.RescheduleExecution
import com.netflix.spinnaker.orca.q.RunTask

@Component
class CancelStageHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        override val stageDefinitionBuilderFactory: StageDefinitionBuilderFactory,
        override val stageNavigator: StageNavigator,

        @Qualifier("cancellableStageExecutor") private val executor: Executor,
        private val taskResolver: TaskResolver
) : OrcaMessageHandler<CancelStage>, StageBuilderAware, AuthenticationAware {

  override val messageType = CancelStage::class.java

  override fun handle(message: CancelStage) {
    message.withStage { stage ->
      /**
       * When an execution ends with status !SUCCEEDED, still-running stages
       * remain in the RUNNING state until their running tasks are dequeued
       * to RunTaskHandler. For tasks leveraging getDynamicBackoffPeriod(),
       * stages may incorrectly report as RUNNING for a considerable length
       * of time, unless we short-circuit their backoff time.
       *
       * For !SUCCEEDED executions, CompleteExecutionHandler enqueues CancelStage
       * messages for all top-level stages. For stages still RUNNING, we requeue
       * RunTask messages for any RUNNING tasks, for immediate execution. This
       * ensures prompt stage cancellation and correct handling of onFailure or
       * cancel conditions. This is safe as RunTaskHandler validates execution
       * status before processing work. RunTask messages are idempotent for
       * cancelled executions, though additional work is generally avoided due
       * to queue deduplication.
       *
       */
      if (stage.status == RUNNING) {
        stage.tasks
          .filter { it.status == RUNNING }
          .forEach {
            queue.reschedule(
              RunTask(
                stage.execution.type,
                stage.execution.id,
                stage.execution.application,
                stage.id,
                it.id,
                it.type
              )
            )
          }
      }
      if (stage.status.isHalt) {
        stage.builder().let { builder ->
          if (builder is CancellableStage) {
            // for the time being we execute this off-thread as some cancel
            // routines may run long enough to cause message acknowledgment to
            // time out.
            executor.execute {
              stage.withAuth {
                builder.cancel(stage)
              }
              // Special case for PipelineStage to ensure prompt cancellation of
              // child pipelines and deployment strategies regardless of task backoff
              if (stage.type.equals("pipeline", true) && stage.context.containsKey("executionId")) {
                val childId = stage.context["executionId"] as? String
                if (childId != null) {
                  val child = repository.retrieve(PIPELINE, childId)
                  queue.push(RescheduleExecution(child))
                }
              }
            }
          }
        }
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  private val com.ctg.pipeline.execute.model.Task.type
    get() = taskResolver.getTaskClass(implementingClass)
}
