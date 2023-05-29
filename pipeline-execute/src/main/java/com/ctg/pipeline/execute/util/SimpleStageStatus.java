package com.ctg.pipeline.execute.util;

import com.netflix.spinnaker.kork.annotations.Beta;

@Beta
public enum SimpleStageStatus {
  TERMINAL,
  RUNNING,
  SUCCEEDED,
  NOT_STARTED
}
