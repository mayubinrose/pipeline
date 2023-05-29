package com.ctg.pipeline.execute.exceptions;

public class TimeoutException extends RuntimeException {
  public TimeoutException(String message) {
    super(message);
  }
}
