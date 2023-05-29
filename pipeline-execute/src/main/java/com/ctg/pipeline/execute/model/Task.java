package com.ctg.pipeline.execute.model;

import static com.ctg.pipeline.execute.model.ExecutionStatus.NOT_STARTED;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/** A "task" is a component piece of a stage */
public class Task {
  private String id;

  private String log;

  public String getLog() {
    return log;
  }

  public void setLog(String log) {
    this.log = log;
  }

  public @Nonnull String getId() {
    return id;
  }

  public void setId(@Nonnull String id) {
    this.id = id;
  }

  private String implementingClass;

  public @Nonnull String getImplementingClass() {
    return implementingClass;
  }

  public void setImplementingClass(@Nonnull String implementingClass) {
    this.implementingClass = implementingClass;
  }

  private String name;

  public @Nonnull String getName() {
    return name;
  }

  public void setName(@Nonnull String name) {
    this.name = name;
  }

  private Long startTime;

  public @Nullable Long getStartTime() {
    return startTime;
  }

  public void setStartTime(@Nullable Long startTime) {
    this.startTime = startTime;
  }

  private Long endTime;

  public @Nullable Long getEndTime() {
    return endTime;
  }

  public void setEndTime(@Nullable Long endTime) {
    this.endTime = endTime;
  }

  private ExecutionStatus status = NOT_STARTED;

  public @Nonnull ExecutionStatus getStatus() {
    return status;
  }

  public void setStatus(@Nonnull ExecutionStatus status) {
    this.status = status;
  }

  private boolean stageStart;

  public boolean isStageStart() {
    return stageStart;
  }

  public void setStageStart(boolean stageStart) {
    this.stageStart = stageStart;
  }

  private boolean stageEnd;

  public boolean isStageEnd() {
    return stageEnd;
  }

  public void setStageEnd(boolean stageEnd) {
    this.stageEnd = stageEnd;
  }

  private boolean loopStart;

  public boolean isLoopStart() {
    return loopStart;
  }

  public void setLoopStart(boolean loopStart) {
    this.loopStart = loopStart;
  }

  private boolean loopEnd;

  public boolean isLoopEnd() {
    return loopEnd;
  }

  public void setLoopEnd(boolean loopEnd) {
    this.loopEnd = loopEnd;
  }

  private List<String> logs =new ArrayList<String>();

  public List<String> getLogs() {
    return logs;
  }

  public void setLogs(List<String> logs) {
    this.logs = logs;
  }
}
