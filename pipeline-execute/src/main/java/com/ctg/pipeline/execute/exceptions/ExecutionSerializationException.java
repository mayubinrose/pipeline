package com.ctg.pipeline.execute.exceptions;

public class ExecutionSerializationException extends RuntimeException {
  public ExecutionSerializationException(String message) {
    super(message);
  }

  public ExecutionSerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
