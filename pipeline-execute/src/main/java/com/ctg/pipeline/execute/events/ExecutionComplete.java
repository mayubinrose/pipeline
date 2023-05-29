package com.ctg.pipeline.execute.events;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.Execution.ExecutionType;
import com.ctg.pipeline.execute.model.ExecutionStatus;

public final class ExecutionComplete extends ExecutionEvent {
  private final ExecutionStatus status;

  public ExecutionComplete(
      @Nonnull Object source,
      @Nonnull ExecutionType executionType,
      @Nonnull String executionId,
      @Nonnull ExecutionStatus status) {
    super(source, executionType, executionId);
    this.status = status;
  }

  public @Nonnull ExecutionStatus getStatus() {
    return status;
  }
}
