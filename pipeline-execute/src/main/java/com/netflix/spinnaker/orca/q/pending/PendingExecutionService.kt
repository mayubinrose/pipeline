package com.netflix.spinnaker.orca.q.pending

import com.netflix.spinnaker.q.Message

/**
 * Used to prevent multiple executions of the same pipeline from running
 * concurrently if [Execution.limitConcurrent] is `true`.
 */
interface PendingExecutionService {
  fun enqueue(pipelineConfigId: String, message: Message)
  fun popOldest(pipelineConfigId: String): Message?
  fun popNewest(pipelineConfigId: String): Message?
  fun purge(pipelineConfigId: String, callback: (Message) -> Unit)
  fun depth(pipelineConfigId: String): Int
}
