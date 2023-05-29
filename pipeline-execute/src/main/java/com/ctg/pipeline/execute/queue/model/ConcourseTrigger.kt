package com.ctg.pipeline.execute.queue.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.netflix.spinnaker.kork.artifacts.model.Artifact
import com.netflix.spinnaker.kork.artifacts.model.ExpectedArtifact

data class ConcourseTrigger
@JvmOverloads constructor(
  override val type: String = "artifactory",
  override val correlationId: String? = null,
  override val user: String? = "[anonymous]",
  override val parameters: Map<String, Any> = mutableMapOf(),
  override val artifacts: List<Artifact> = mutableListOf(),
  override val notifications: List<Map<String, Any>> = mutableListOf(),
  override var isRebake: Boolean = false,
  override var isDryRun: Boolean = false,
  override var isStrategy: Boolean = false
) : Trigger {
  override var other: Map<String, Any> = mutableMapOf()
  override var resolvedExpectedArtifacts: List<ExpectedArtifact> = mutableListOf()
  var buildInfo: ConcourseBuildInfo? = null
  var properties: Map<String, Any> = mutableMapOf()
}

class ConcourseBuildInfo
@JsonCreator
constructor(
  @param:JsonProperty("name") override val name: String?,
  @param:JsonProperty("number") override val number: Int,
  @param:JsonProperty("url") override val url: String?,
  @param:JsonProperty("result") override val result: String?,
  @param:JsonProperty("artifacts") override val artifacts: List<JenkinsArtifact>?,
  @param:JsonProperty("scm") override val scm: List<SourceControl>?,
  @param:JsonProperty("building") override var building: Boolean = false
) :
  BuildInfo<Any>(name, number, url, result, artifacts, scm, building)
