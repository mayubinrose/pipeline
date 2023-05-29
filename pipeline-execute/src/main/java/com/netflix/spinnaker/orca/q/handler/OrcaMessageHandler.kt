package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.exceptions.ExceptionHandler
import com.ctg.pipeline.execute.exceptions.ExecutionNotFoundException
import com.ctg.pipeline.execute.model.Execution
import com.ctg.pipeline.execute.util.ExecuteObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.q.Message
import com.netflix.spinnaker.q.MessageHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository.ExecutionCriteria
import com.ctg.pipeline.execute.queue.orca.*
import com.ctg.pipeline.execute.model.ExecutionStatus.RUNNING;
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.model.Task
import com.ctg.pipeline.execute.queue.orca.handler.ext.parent
import com.netflix.spinnaker.orca.q.*

internal interface OrcaMessageHandler<M : Message> : MessageHandler<M> {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val mapper: ObjectMapper = ExecuteObjectMapper.getInstance()
  }

  val repository: ExecutionRepository

  fun Collection<ExceptionHandler>.shouldRetry(ex: Exception, taskName: String?): ExceptionHandler.Response? {
    val exceptionHandler = find { it.handles(ex) }
    return exceptionHandler?.handle(taskName ?: "unspecified", ex)
  }

  fun TaskLevel.withTask(block: (Stage, Task) -> Unit) =
    withStage { stage ->
      stage
        .taskById(taskId)
        .let { task ->
          if (task == null) {
            log.error("InvalidTaskId: Unable to find task {} in stage '{}' while processing message {}", taskId, mapper.writeValueAsString(stage), this)
            queue.push(InvalidTaskId(this))
          } else {
            block.invoke(stage, task)
          }
        }
    }

  fun StageLevel.withStage(block: (Stage) -> Unit) =
    withExecution { execution ->
      try {
        execution
          .stageById(stageId)
          .also {
            /**
             * Mutates it.context in a required way (such as removing refId and requisiteRefIds from the
             * context map) for some non-linear stage features.
             */
            Stage(execution, it.type, it.context)
          }
          .let(block)
      } catch (e: IllegalArgumentException) {
        queue.push(InvalidStageId(this))
      }
    }

  fun ExecutionLevel.withExecution(block: (Execution) -> Unit) =
    try {
      val execution = repository.retrieve(executionType, executionId)
      block.invoke(execution)
    } catch (e: ExecutionNotFoundException) {
      queue.push(InvalidExecutionId(this))
    }

  fun Stage.startNext() {
    execution.let { execution ->
      val downstreamStages = downstreamStages()
      val phase = syntheticStageOwner
      if (downstreamStages.isNotEmpty()) {
        downstreamStages.forEach {
          queue.push(StartStage(it))
        }
      } else if (phase != null) {
        queue.ensure(ContinueParentStage(parent(), phase), Duration.ZERO)
      } else {
        queue.push(CompleteExecution(execution))
      }
    }
  }

  fun Execution.shouldQueue(): Boolean {
    val configId = pipelineConfigId
    return when {
      !isLimitConcurrent -> false
      configId == null -> false
      else -> {
        val criteria = ExecutionCriteria().setPageSize(2).setStatuses(RUNNING)
        repository
          .retrievePipelinesForPipelineConfigId(configId, criteria)
          .filter { it.id != id }
          .count()
          .toBlocking()
          .first() > 0
      }
    }
  }
}
