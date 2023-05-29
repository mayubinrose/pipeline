package com.ctg.pipeline.execute.basic.task;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.basic.stage.WaitStage;
import com.ctg.pipeline.execute.model.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WaitTask implements RetryableTask {

  private final Clock clock;

  @Autowired
  public WaitTask(Clock clock) {
    this.clock = clock;
  }

  @Override
  public @Nonnull TaskResult execute(@Nonnull Stage stage) {
    WaitStage.WaitStageContext context = stage.mapTo(WaitStage.WaitStageContext.class);

    Instant now = clock.instant();

    if (context.isSkipRemainingWait()) {
      return TaskResult.SUCCEEDED;
    } else if (stage.getStartTime() != null
        && Instant.ofEpochMilli(stage.getStartTime())
            .plus(context.getWaitDuration())
            .isBefore(now)) {
      return TaskResult.SUCCEEDED;
    } else {
      return TaskResult.RUNNING;
    }
  }

  @Override
  public long getBackoffPeriod() {
    return 1_000;
  }

  @Override
  public long getDynamicBackoffPeriod(Stage stage, Duration taskDuration) {
    WaitStage.WaitStageContext context = stage.mapTo(WaitStage.WaitStageContext.class);

    if (context.isSkipRemainingWait()) {
      return 0L;
    }

    // Return a backoff time that reflects the requested waitTime
    if (stage.getStartTime() != null) {
      Instant now = clock.instant();
      Instant completion =
          Instant.ofEpochMilli(stage.getStartTime()).plus(context.getWaitDuration());

      if (completion.isAfter(now)) {
        return completion.toEpochMilli() - now.toEpochMilli();
      }
    }
    return getBackoffPeriod();
  }

  @Override
  public long getTimeout() {
    return Long.MAX_VALUE;
  }
}
