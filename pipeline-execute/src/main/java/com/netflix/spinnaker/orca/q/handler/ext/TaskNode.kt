package com.ctg.pipeline.execute.queue.orca.handler.ext

import com.ctg.pipeline.execute.basic.task.Task
import com.ctg.pipeline.execute.util.TaskNode


inline fun <reified T : Task> TaskNode.Builder.withTask(name: String) =
  withTask(name, T::class.java)
