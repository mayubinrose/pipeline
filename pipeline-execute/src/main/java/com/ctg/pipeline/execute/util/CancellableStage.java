package com.ctg.pipeline.execute.util;

import com.ctg.pipeline.execute.model.Stage;

import java.util.Map;


public interface CancellableStage {
  Result cancel(Stage stage);

  class Result {
    private final String stageId;
    private final Map details;

    public Result(Stage stage, Map details) {
      this.stageId = stage.getId();
      this.details = details;
    }

    public final String getStageId() {
      return stageId;
    }

    public final Map getDetails() {
      return details;
    }
  }
}
