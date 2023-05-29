package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.basic.task.TaskResolver
import com.ctg.pipeline.execute.events.TaskStarted
import com.netflix.spinnaker.q.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.Clock
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.util.ContextParameterProcessor
import com.ctg.pipeline.execute.util.StageDefinitionBuilderFactory
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING
import com.netflix.spinnaker.orca.q.RunTask
import com.netflix.spinnaker.orca.q.StartTask

@Component
class StartTaskHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        override val contextParameterProcessor: ContextParameterProcessor,
        override val stageDefinitionBuilderFactory: StageDefinitionBuilderFactory,
        @Qualifier("queueEventPublisher") private val publisher: ApplicationEventPublisher,
        private val taskResolver: TaskResolver,
        private val clock: Clock
) : OrcaMessageHandler<StartTask>, ExpressionAware {

  override fun handle(message: StartTask) {
    message.withTask { stage, task ->
      task.status = RUNNING
      task.startTime = clock.millis()
      val mergedContextStage = stage.withMergedContext()
      repository.storeStage(mergedContextStage)

      queue.push(RunTask(message, task.id, task.type))

      publisher.publishEvent(TaskStarted(this, mergedContextStage, task))
    }
  }

  override val messageType = StartTask::class.java

  @Suppress("UNCHECKED_CAST")
  private val com.ctg.pipeline.execute.model.Task.type
    get() = taskResolver.getTaskClass(implementingClass)
}
