package com.ctg.pipeline.execute.pipeline.persistence.jedis;

import static com.ctg.pipeline.execute.model.SyntheticStageOwner.STAGE_AFTER;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.Stage;

public class ExecutionRepositoryUtil {

  /**
   * Ensure proper stage ordering even when stageIndex is an unsorted set or absent.
   *
   * <p>Necessary for ensuring API responses and the UI render stages in the order of their
   * execution.
   */
  public static void sortStagesByReference(
          @Nonnull Execution execution, @Nonnull List<Stage> stages) {
    if (!execution.getStages().isEmpty()) {
      throw new StagesAlreadySorted();
    }

    if (stages.stream().map(Stage::getRefId).allMatch(Objects::nonNull)) {
      execution
          .getStages()
          .addAll(
              stages.stream()
                  .filter(s -> s.getParentStageId() == null)
                  .sorted(Comparator.comparing(Stage::getRefId))
                  .collect(Collectors.toList()));

      stages.stream()
          .filter(s -> s.getParentStageId() != null)
          .sorted(Comparator.comparing(Stage::getRefId))
          .forEach(
              s -> {
                Integer index = execution.getStages().indexOf(s.getParent());
                if (s.getSyntheticStageOwner() == STAGE_AFTER) {
                  index++;
                }
                execution.getStages().add(index, s);
              });
    } else {
      execution.getStages().addAll(stages);
    }
  }

  private static class StagesAlreadySorted extends IllegalStateException {
    StagesAlreadySorted() {
      super("Execution already has stages defined");
    }
  }
}
