package com.ctg.pipeline.execute.queue.model

import com.netflix.spinnaker.kork.artifacts.model.Artifact
import com.netflix.spinnaker.kork.artifacts.model.ExpectedArtifact

data class DockerTrigger
@JvmOverloads constructor(
  override val type: String = "docker",
  override val correlationId: String? = null,
  override val user: String? = "[anonymous]",
  override val parameters: Map<String, Any> = mutableMapOf(),
  override val artifacts: List<Artifact> = mutableListOf(),
  override val notifications: List<Map<String, Any>> = mutableListOf(),
  override var isRebake: Boolean = false,
  override var isDryRun: Boolean = false,
  override var isStrategy: Boolean = false,
  val account: String,
  val repository: String,
  val tag: String?
) : Trigger {
  override var other: Map<String, Any> = mutableMapOf()
  override var resolvedExpectedArtifacts: List<ExpectedArtifact> = mutableListOf()
}