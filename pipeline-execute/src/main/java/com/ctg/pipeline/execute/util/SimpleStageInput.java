package com.ctg.pipeline.execute.util;

import com.netflix.spinnaker.kork.annotations.Beta;

import lombok.Data;

@Beta
@Data
public class SimpleStageInput<T> {
  private T value;

  public SimpleStageInput(T value) {
    this.value = value;
  }
}
