package com.netflix.spinnaker.orca.q.migration

import com.ctg.pipeline.execute.basic.task.TaskResolver
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class TaskTypeDeserializer(
  private val taskResolver: TaskResolver
) : JsonDeserializer<Class<*>>() {
  override fun deserialize(
    p: JsonParser,
    ctxt: DeserializationContext
  ) = if (p.parsingContext.currentName == "taskType") {
    taskResolver.getTaskClass(p.valueAsString)
  } else {
    Class.forName(p.valueAsString)
  }
}
