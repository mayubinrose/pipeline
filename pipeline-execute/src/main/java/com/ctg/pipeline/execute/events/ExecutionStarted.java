package com.ctg.pipeline.execute.events;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.Execution.ExecutionType;

public final class ExecutionStarted extends ExecutionEvent {
  public ExecutionStarted(
      @Nonnull Object source, @Nonnull ExecutionType executionType, @Nonnull String executionId) {
    super(source, executionType, executionId);
  }
}
