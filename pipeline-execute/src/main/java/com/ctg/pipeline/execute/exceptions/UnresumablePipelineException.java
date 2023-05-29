package com.ctg.pipeline.execute.exceptions;

public class UnresumablePipelineException extends IllegalStateException {
  public UnresumablePipelineException(String message) {
    super(message);
  }

  public UnresumablePipelineException(String message, Throwable cause) {
    super(message, cause);
  }
}
