package com.ctg.pipeline.execute.basic.stage;

import java.time.Duration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.stereotype.Component;

import com.ctg.pipeline.execute.basic.task.WaitTask;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.util.StageDefinitionBuilder;
import com.ctg.pipeline.execute.util.TaskNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class WaitStage implements StageDefinitionBuilder {

  public static String STAGE_TYPE = "wait";

  @Override
  public void taskGraph(@Nonnull Stage stage, TaskNode.Builder builder) {
    builder.withTask("wait", WaitTask.class);
  }

  public static final class WaitStageContext {
    private final Long waitTime;
    private final boolean skipRemainingWait;

    @JsonCreator
    public WaitStageContext(
        @JsonProperty("waitTime") @Nullable Long waitTime,
        @JsonProperty("skipRemainingWait") @Nullable Boolean skipRemainingWait) {
      this.waitTime = waitTime;
      this.skipRemainingWait = skipRemainingWait == null ? false : skipRemainingWait;
    }

    public WaitStageContext(@Nonnull Long waitTime) {
      this(waitTime, false);
    }

    @JsonIgnore
    public Duration getWaitDuration() {
      return waitTime == null ? Duration.ZERO : Duration.ofSeconds(waitTime);
    }

    public boolean isSkipRemainingWait() {
      return skipRemainingWait;
    }
  }
}
