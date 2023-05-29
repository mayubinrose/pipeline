package com.ctg.pipeline.execute.model;

public class ExecutionContext {
  private static final ThreadLocal<ExecutionContext> threadLocal = new ThreadLocal<>();

  private final String application;
  private final String authenticatedUser;
  private final String executionType;
  private final String executionId;
  private final String stageId;
  private final String origin;
  private final Long stageStartTime;

  public ExecutionContext(
      String application,
      String authenticatedUser,
      String executionType,
      String executionId,
      String stageId,
      String origin,
      Long stageStartTime) {
    this.application = application;
    this.authenticatedUser = authenticatedUser;
    this.executionType = executionType;
    this.executionId = executionId;
    this.stageId = stageId;
    this.origin = origin;
    this.stageStartTime = stageStartTime;
  }

  public static void set(ExecutionContext executionContext) {
    threadLocal.set(executionContext);
  }

  public static ExecutionContext get() {
    return threadLocal.get();
  }

  public static void clear() {
    threadLocal.remove();
  }

  public String getApplication() {
    return application;
  }

  public String getAuthenticatedUser() {
    return authenticatedUser;
  }

  public String getExecutionType() {
    return executionType;
  }

  public String getExecutionId() {
    return executionId;
  }

  public String getOrigin() {
    return origin;
  }

  public String getStageId() {
    return stageId;
  }

  public Long getStageStartTime() {
    return stageStartTime;
  }
}
