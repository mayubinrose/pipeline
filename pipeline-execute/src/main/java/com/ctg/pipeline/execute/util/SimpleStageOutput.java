package com.ctg.pipeline.execute.util;

import com.netflix.spinnaker.kork.annotations.Beta;

import lombok.Data;

@Beta
@Data
public class SimpleStageOutput<T, U> {
  private SimpleStageStatus status;
  private T output;
  private U context;
}
