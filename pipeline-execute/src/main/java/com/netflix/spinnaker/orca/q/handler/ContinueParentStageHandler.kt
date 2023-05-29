package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.model.SyntheticStageOwner.STAGE_BEFORE
import com.ctg.pipeline.execute.model.ExecutionStatus.NOT_STARTED
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.queue.orca.handler.ext.allAfterStagesComplete
import com.ctg.pipeline.execute.queue.orca.handler.ext.allBeforeStagesSuccessful
import com.ctg.pipeline.execute.queue.orca.handler.ext.anyBeforeStagesFailed
import com.ctg.pipeline.execute.queue.orca.handler.ext.hasTasks
import com.netflix.spinnaker.orca.q.CompleteStage
import com.netflix.spinnaker.orca.q.ContinueParentStage
import com.netflix.spinnaker.orca.q.StartTask
import com.netflix.spinnaker.q.Queue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class ContinueParentStageHandler(
        override val queue: Queue,
        override val repository: ExecutionRepository,
        @Value("\${queue.retry.delay.ms:5000}") retryDelayMs: Long
) : OrcaMessageHandler<ContinueParentStage> {

  private val log: Logger = LoggerFactory.getLogger(javaClass)
  private val retryDelay = Duration.ofMillis(retryDelayMs)

  override fun handle(message: ContinueParentStage) {
    message.withStage { stage ->
      if (message.phase == STAGE_BEFORE) {
        if (stage.allBeforeStagesSuccessful()) {
          when {
            stage.hasTasks() -> stage.runFirstTask()
            else -> queue.push(CompleteStage(stage))
          }
        } else if (!stage.anyBeforeStagesFailed()) {
          log.info("Re-queuing $message as other ${message.phase} stages are still running")
          queue.push(message, retryDelay)
        }
      } else {
        if (stage.allAfterStagesComplete()) {
          queue.push(CompleteStage(stage))
        } else {
          log.info("Re-queuing $message as other ${message.phase} stages are still running")
          queue.push(message, retryDelay)
        }
      }
    }
  }

  private fun Stage.runFirstTask() {
    val firstTask = tasks.first()
    if (firstTask.status == NOT_STARTED) {
      queue.push(StartTask(this, firstTask))
    } else {
      log.warn("Ignoring $messageType for $id as tasks are already running")
    }
  }

  override val messageType = ContinueParentStage::class.java
}
