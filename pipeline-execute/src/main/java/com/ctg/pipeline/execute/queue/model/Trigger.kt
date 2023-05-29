package com.ctg.pipeline.execute.queue.model

import com.ctg.pipeline.execute.queue.model.support.TriggerDeserializer
import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.netflix.spinnaker.kork.artifacts.model.Artifact
import com.netflix.spinnaker.kork.artifacts.model.ExpectedArtifact

@JsonDeserialize(using = TriggerDeserializer::class)
interface Trigger {
  val type: String
  val correlationId: String?
  val user: String?
  val parameters: Map<String, Any>
  val artifacts: List<Artifact>
  val notifications: List<Map<String, Any>>
  @get:JsonProperty("rebake")
  var isRebake: Boolean
  @get:JsonProperty("dryRun")
  var isDryRun: Boolean
  @get:JsonProperty("strategy")
  var isStrategy: Boolean
  var resolvedExpectedArtifacts: List<ExpectedArtifact>
  @set:JsonAnySetter
  @get:JsonAnyGetter
  var other: Map<String, Any>
}
