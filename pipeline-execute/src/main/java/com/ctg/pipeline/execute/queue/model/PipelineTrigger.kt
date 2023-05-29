package com.ctg.pipeline.execute.queue.model

import com.ctg.pipeline.execute.model.Execution
import com.ctg.pipeline.execute.model.Stage
import com.fasterxml.jackson.annotation.JsonIgnore
import com.netflix.spinnaker.kork.artifacts.model.Artifact
import com.netflix.spinnaker.kork.artifacts.model.ExpectedArtifact

data class PipelineTrigger
@JvmOverloads constructor(
        override val type: String = "pipeline",
        override val correlationId: String? = null,
        override val user: String? = "[anonymous]",
        override val parameters: Map<String, Any> = mutableMapOf(),
        override val artifacts: List<Artifact> = mutableListOf(),
        override val notifications: List<Map<String, Any>> = mutableListOf(),
        override var isRebake: Boolean = false,
        override var isDryRun: Boolean = false,
        override var isStrategy: Boolean = false,
        val parentExecution: Execution,
        val parentPipelineStageId: String? = null
) : Trigger {
  override var other: Map<String, Any> = mutableMapOf()
  override var resolvedExpectedArtifacts: List<ExpectedArtifact> = mutableListOf()

  @JsonIgnore
  val parentStage: Stage? =
    if (parentPipelineStageId != null) {
      parentExecution.stageById(parentPipelineStageId)
    } else {
      null
    }
}
