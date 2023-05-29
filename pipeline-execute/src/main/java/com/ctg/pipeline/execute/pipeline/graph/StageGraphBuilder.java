package com.ctg.pipeline.execute.pipeline.graph;

import static com.ctg.pipeline.execute.model.SyntheticStageOwner.STAGE_AFTER;
import static com.ctg.pipeline.execute.model.SyntheticStageOwner.STAGE_BEFORE;
import static java.lang.String.format;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.SyntheticStageOwner;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

public class StageGraphBuilder {

  private final Stage parent;
  private final SyntheticStageOwner type;
  private final MutableGraph<Stage> graph =
      GraphBuilder.directed().build(); // TODO: is this actually useful?
  private final Optional<Stage> requiredPrefix;
  private @Nullable Stage lastAdded = null;

  private StageGraphBuilder(
      Stage parent, SyntheticStageOwner type, Optional<Stage> requiredPrefix) {
    this.parent = parent;
    this.type = type;
    this.requiredPrefix = requiredPrefix;
    this.requiredPrefix.ifPresent(this::add);
  }

  /** Create a new builder for the before stages of {@code parent}. */
  public static @Nonnull StageGraphBuilder beforeStages(@Nonnull Stage parent) {
    return new StageGraphBuilder(parent, STAGE_BEFORE, Optional.empty());
  }

  /** Create a new builder for the before stages of {@code parent}. */
  public static @Nonnull StageGraphBuilder beforeStages(
      @Nonnull Stage parent, @Nullable Stage requiredPrefix) {
    return new StageGraphBuilder(parent, STAGE_BEFORE, Optional.ofNullable(requiredPrefix));
  }

  /** Create a new builder for the after stages of {@code parent}. */
  public static @Nonnull StageGraphBuilder afterStages(@Nonnull Stage parent) {
    return new StageGraphBuilder(parent, STAGE_AFTER, Optional.empty());
  }

  public @Nonnull Stage add(@Nonnull Consumer<Stage> init) {
    Stage stage = newStage(init);
    add(stage);
    return stage;
  }

  public void add(@Nonnull Stage stage) {
    stage.setExecution(parent.getExecution());
    stage.setParentStageId(parent.getId());
    stage.setSyntheticStageOwner(type);
    if (graph.addNode(stage)) {
      stage.setRefId(generateRefId());
    }
    lastAdded = stage;
  }

  public @Nonnull Stage connect(@Nonnull Stage previous, @Nonnull Consumer<Stage> init) {
    Stage stage = add(init);
    connect(previous, stage);
    return stage;
  }

  public void connect(@Nonnull Stage previous, @Nonnull Stage next) {
    add(previous);
    add(next);
    Set<String> requisiteStageRefIds = new HashSet<>(next.getRequisiteStageRefIds());
    requisiteStageRefIds.add(previous.getRefId());
    next.setRequisiteStageRefIds(requisiteStageRefIds);
    graph.putEdge(previous, next);
  }

  public @Nonnull Stage append(@Nonnull Consumer<Stage> init) {
    if (lastAdded == null) {
      return add(init);
    } else {
      return connect(lastAdded, init);
    }
  }

  public void append(@Nonnull Stage stage) {
    if (lastAdded == null) {
      add(stage);
    } else {
      connect(lastAdded, stage);
    }
  }

  public @Nonnull Iterable<Stage> build() {
    requiredPrefix.ifPresent(
        prefix ->
            graph
                .nodes()
                .forEach(
                    it -> {
                      if (it != prefix && it.getRequisiteStageRefIds().isEmpty()) {
                        connect(prefix, it);
                      }
                    }));
    return graph.nodes();
  }

  private String generateRefId() {
    long offset =
        parent.getExecution().getStages().stream()
            .filter(
                i ->
                    parent.getId().equals(i.getParentStageId())
                        && type == i.getSyntheticStageOwner())
            .count();

    return format(
        "%s%s%d",
        parent.getRefId(), type == STAGE_BEFORE ? "<" : ">", offset + graph.nodes().size());
  }

  private Stage newStage(Consumer<Stage> init) {
    Stage stage = new Stage();
    init.accept(stage);
    return stage;
  }
}
