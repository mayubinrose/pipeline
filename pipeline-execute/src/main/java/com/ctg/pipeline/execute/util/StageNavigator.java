package com.ctg.pipeline.execute.util;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctg.pipeline.execute.model.Stage;

@Component
public class StageNavigator {
  private final Map<String, StageDefinitionBuilder> stageDefinitionBuilders;

  @Autowired
  public StageNavigator(Collection<StageDefinitionBuilder> stageDefinitionBuilders) {
    this.stageDefinitionBuilders =
        stageDefinitionBuilders.stream()
            .collect(toMap(StageDefinitionBuilder::getType, Function.identity()));
  }

  /**
   * As per `Stage.ancestors` except this method returns tuples of the stages and their
   * `StageDefinitionBuilder`.
   */
  public List<Result> ancestors(Stage startingStage) {
    return startingStage.ancestors().stream()
        .map(it -> new Result(it, stageDefinitionBuilders.get(it.getType())))
        .collect(toList());
  }

  public static class Result {
    private final Stage stage;
    private final StageDefinitionBuilder stageBuilder;

    Result(Stage stage, StageDefinitionBuilder stageBuilder) {
      this.stage = stage;
      this.stageBuilder = stageBuilder;
    }

    public Stage getStage() {
      return stage;
    }

    public StageDefinitionBuilder getStageBuilder() {
      return stageBuilder;
    }
  }
}
