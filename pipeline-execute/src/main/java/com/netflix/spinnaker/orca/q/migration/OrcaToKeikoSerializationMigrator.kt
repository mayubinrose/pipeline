package com.netflix.spinnaker.orca.q.migration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.netflix.spinnaker.q.migration.SerializationMigrator

internal val orcaToKeikoTypes = mapOf(
  ".StartTask" to "startTask",
  ".CompleteTask" to "completeTask",
  ".PauseTask" to "pauseTask",
  ".ResumeTask" to "resumeTask",
  ".RunTask" to "runTask",
  ".StartStage" to "startStage",
  ".ContinueParentStage" to "continueParentStage",
  ".CompleteStage" to "completeStage",
  ".SkipStage" to "skipStage",
  ".AbortStage" to "abortStage",
  ".PauseStage" to "pauseStage",
  ".RestartStage" to "restartStage",
  ".ResumeStage" to "resumeStage",
  ".CancelStage" to "cancelStage",
  ".StartExecution" to "startExecution",
  ".RescheduleExecution" to "rescheduleExecution",
  ".CompleteExecution" to "completeExecution",
  ".ResumeExecution" to "resumeExecution",
  ".CancelExecution" to "cancelExecution",
  ".InvalidExecutionId" to "invalidExecutionId",
  ".InvalidStageId" to "invalidStageId",
  ".InvalidTaskId" to "invalidTaskId",
  ".InvalidTaskType" to "invalidTaskType",
  ".NoDownstreamTasks" to "noDownstreamTasks",
  ".TotalThrottleTimeAttribute" to "totalThrottleTime",
  ".handler.DeadMessageAttribute" to "deadMessage",
  ".MaxAttemptsAttribute" to "maxAttempts",
  ".AttemptsAttribute" to "attempts"
)

class OrcaToKeikoSerializationMigrator(
  private val mapper: ObjectMapper
) : SerializationMigrator {

  override fun migrate(json: String): String {
    val m = mapper.readValue<MutableMap<String, Any?>>(json)

    val replaceKind = fun (target: MutableMap<String, Any?>) {
      if (target.containsKey("@class")) {
        target["kind"] = orcaToKeikoTypes[target["@class"]]
        target.remove("@class")
      }
    }

    replaceKind(m)
    if (m.containsKey("attributes")) {
      (m["attributes"] as List<MutableMap<String, Any?>>).forEach(replaceKind)
    }

    return mapper.writeValueAsString(m)
  }
}
