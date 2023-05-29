package com.ctg.pipeline.execute.exceptions;

public class StageSerializationException extends RuntimeException {
  public StageSerializationException(String message) {
    super(message);
  }

  public StageSerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
