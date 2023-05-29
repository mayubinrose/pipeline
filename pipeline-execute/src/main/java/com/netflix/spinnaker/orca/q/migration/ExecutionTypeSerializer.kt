package com.netflix.spinnaker.orca.q.migration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.ctg.pipeline.execute.model.Execution.ExecutionType
import com.ctg.pipeline.execute.model.Execution.ExecutionType.ORCHESTRATION
import com.ctg.pipeline.execute.model.Execution.ExecutionType.PIPELINE

internal const val PIPELINE_CLASS_NAME = "com.netflix.spinnaker.orca.pipeline.model.Pipeline"
internal const val ORCHESTRATION_CLASS_NAME = "com.netflix.spinnaker.orca.pipeline.model.Orchestration"

class ExecutionTypeDeserializer : JsonDeserializer<ExecutionType>() {
  override fun handledType(): Class<*> = ExecutionType::class.java

  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext
  ) = when (p.valueAsString) {
    PIPELINE_CLASS_NAME, PIPELINE.name -> PIPELINE
    ORCHESTRATION_CLASS_NAME, ORCHESTRATION.name -> ORCHESTRATION
    else -> throw InvalidFormatException(
      p,
      "Invalid value for ExecutionType",
      p.valueAsString,
      ExecutionType::class.java
    )
  }
}
