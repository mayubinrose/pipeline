package com.ctg.pipeline.execute.queue.model

/**
 * Defines properties that are common across different types of source code triggers.
 */
interface SourceCodeTrigger : Trigger {
  val source: String
  val project: String
  val branch: String
  val slug: String
  val hash: String
}
