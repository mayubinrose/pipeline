package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository
import com.ctg.pipeline.execute.queue.orca.*
import com.netflix.spinnaker.orca.q.*
import com.netflix.spinnaker.orca.q.pending.PendingExecutionService
import com.netflix.spinnaker.q.Queue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StartWaitingExecutionsHandler(
  override val queue: Queue,
  override val repository: ExecutionRepository,
  private val pendingExecutionService: PendingExecutionService
) : OrcaMessageHandler<StartWaitingExecutions> {

  private val log: Logger get() = LoggerFactory.getLogger(javaClass)

  override val messageType = StartWaitingExecutions::class.java

  override fun handle(message: StartWaitingExecutions) {
    if (message.purgeQueue) {
      // when purging the queue, run the latest message and discard the rest
      pendingExecutionService.popNewest(message.pipelineConfigId)
        .also { _ ->
          pendingExecutionService.purge(message.pipelineConfigId) { purgedMessage ->
            when (purgedMessage) {
              is StartExecution -> {
                log.info("Dropping queued pipeline {} {}", purgedMessage.application, purgedMessage.executionId)
                queue.push(CancelExecution(purgedMessage))
              }
              is RestartStage -> {
                log.info("Cancelling restart of {} {}", purgedMessage.application, purgedMessage.executionId)
                // don't need to do anything else
              }
            }
          }
        }
    } else {
      // when not purging the queue, run the messages in the order they came in
      pendingExecutionService.popOldest(message.pipelineConfigId)
    }
      ?.let {
        // spoiler, it always is!
        if (it is ExecutionLevel) {
          log.info("Starting queued pipeline {} {} {}", it.application, it.executionId)
        }
        queue.push(it)
      }
  }
}
