package com.ctg.pipeline.execute.queue.orca.handler.ext

import com.ctg.pipeline.execute.model.Execution


/**
 * @return the initial stages of the execution.
 */
fun Execution.initialStages() =
  stages
    .filter { it.isInitial() }
