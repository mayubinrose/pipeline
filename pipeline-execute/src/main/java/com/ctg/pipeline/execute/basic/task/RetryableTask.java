package com.ctg.pipeline.execute.basic.task;

import java.time.Duration;

import com.ctg.pipeline.execute.model.Stage;

/**
 * A retryable task defines its backoff period (the period between delays) and its timeout (the
 * total period of the task)
 */
public interface RetryableTask extends Task {
  long getBackoffPeriod();

  long getTimeout();

  default long getDynamicTimeout(Stage stage) {
    return getTimeout();
  }

  default long getDynamicBackoffPeriod(Duration taskDuration) {
    return getBackoffPeriod();
  }

  default long getDynamicBackoffPeriod(Stage stage, Duration taskDuration) {
    return getDynamicBackoffPeriod(taskDuration);
  }
}
