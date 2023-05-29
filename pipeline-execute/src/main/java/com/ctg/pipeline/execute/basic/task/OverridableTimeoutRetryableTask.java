package com.ctg.pipeline.execute.basic.task;

/**
 * A retryable task whose timeout is taken from the top level stage if that value has been
 * overridden.
 *
 * <p>These are typically wait/monitor stages
 */
public interface OverridableTimeoutRetryableTask extends RetryableTask {}
