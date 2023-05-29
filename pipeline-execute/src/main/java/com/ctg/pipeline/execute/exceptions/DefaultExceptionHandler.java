package com.ctg.pipeline.execute.exceptions;

import static java.lang.String.format;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.google.common.base.Throwables;

@Order(LOWEST_PRECEDENCE)
public class DefaultExceptionHandler implements ExceptionHandler {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public boolean handles(Exception e) {
    return true;
  }

  @Override
  public ExceptionHandler.Response handle(String taskName, Exception e) {
    Map<String, Object> exceptionDetails =
        ExceptionHandler.responseDetails(
            "Unexpected Task Failure", Collections.singletonList(e.getMessage()));
    exceptionDetails.put("stackTrace", Throwables.getStackTraceAsString(e));
    log.warn(format("Error occurred during task %s", taskName), e);
    return new ExceptionHandler.Response(
        e.getClass().getSimpleName(), taskName, exceptionDetails, false);
  }
}
