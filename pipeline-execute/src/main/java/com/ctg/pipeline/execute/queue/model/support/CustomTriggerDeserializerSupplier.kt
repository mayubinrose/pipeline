package com.ctg.pipeline.execute.queue.model.support

import com.ctg.pipeline.execute.queue.model.Trigger
import com.fasterxml.jackson.databind.JsonNode

/**
 * Provides a [predicate] & [deserializer] pair for custom trigger types.
 *
 * The [predicate] will return true if the [deserializer] should be used
 * for the provided JsonNode. If more than one [predicate] returns true,
 * the first supplier will be chosen.
 */
interface CustomTriggerDeserializerSupplier {
  val predicate: (node: JsonNode) -> Boolean
  val deserializer: (node: JsonNode) -> Trigger
}
