package com.ctg.pipeline.execute.exceptions;

public class UnpausablePipelineException extends IllegalStateException {
  public UnpausablePipelineException(String message) {
    super(message);
  }

  public UnpausablePipelineException(String message, Throwable cause) {
    super(message, cause);
  }
}
