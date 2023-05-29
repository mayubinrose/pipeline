package com.ctg.pipeline.execute.util;

import static com.ctg.pipeline.execute.util.TaskNode.Builder;
import static com.ctg.pipeline.execute.util.TaskNode.GraphType.FULL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.SyntheticStageOwner;
import com.ctg.pipeline.execute.pipeline.graph.StageGraphBuilder;
import com.google.common.base.CaseFormat;
import com.netflix.spinnaker.kork.dynamicconfig.DynamicConfigService;
import com.netflix.spinnaker.kork.expressions.ExpressionEvaluationSummary;

public interface StageDefinitionBuilder {

  default @Nonnull TaskNode.TaskGraph buildTaskGraph(@Nonnull Stage stage) {
    Builder graphBuilder = Builder(FULL);
    taskGraph(stage, graphBuilder);
    return graphBuilder.build();
  }

  default void taskGraph(@Nonnull Stage stage, @Nonnull Builder builder) {}

  /**
   * Implement this method to define any stages that should run before any tasks in this stage as
   * part of a composed workflow.
   */
  default void beforeStages(@Nonnull Stage parent, @Nonnull StageGraphBuilder graph) {}

  /**
   * Implement this method to define any stages that should run after any tasks in this stage as
   * part of a composed workflow.
   */
  default void afterStages(@Nonnull Stage parent, @Nonnull StageGraphBuilder graph) {}

  /**
   * Implement this method to define any stages that should run in response to a failure in tasks,
   * before or after stages.
   */
  default void onFailureStages(@Nonnull Stage stage, @Nonnull StageGraphBuilder graph) {}

  /** @return the stage type this builder handles. */
  default @Nonnull String getType() {
    return getType(this.getClass());
  }

  /**
   * Allows the stage to process SpEL expression in its own context in a custom way
   *
   * @return true to continue processing, false to stop generic processing of expressions
   */
  default boolean processExpressions(
      @Nonnull Stage stage,
      @Nonnull ContextParameterProcessor contextParameterProcessor,
      @Nonnull ExpressionEvaluationSummary summary) {
    return true;
  }

  /** Implementations can override this if they need any special cleanup on restart. */
  default void prepareStageForRestart(@Nonnull Stage stage) {}

  static String getType(Class<? extends StageDefinitionBuilder> clazz) {
    String className = clazz.getSimpleName();
    return className.substring(0, 1).toLowerCase()
        + className
            .substring(1)
            .replaceFirst("StageDefinitionBuilder$", "")
            .replaceFirst("Stage$", "");
  }

  static @Nonnull Stage newStage(
      @Nonnull Execution execution,
      @Nonnull String type,
      @Nullable String name,
      @Nonnull Map<String, Object> context,
      @Nullable Stage parent,
      @Nullable SyntheticStageOwner stageOwner) {
    Stage stage = new Stage(execution, type, name, context);
    if (parent != null) {
      stage.setParentStageId(parent.getId());
    }
    stage.setSyntheticStageOwner(stageOwner);
    return stage;
  }

  /** Return true if the stage can be manually skipped from the API. */
  default boolean canManuallySkip() {
    return false;
  }

  default boolean isForceCacheRefreshEnabled(DynamicConfigService dynamicConfigService) {
    String className = getClass().getSimpleName();

    try {
      return dynamicConfigService.isEnabled(
          String.format(
              "stages.%s.force-cache-refresh",
              CaseFormat.LOWER_CAMEL.to(
                  CaseFormat.LOWER_HYPHEN,
                  Character.toLowerCase(className.charAt(0)) + className.substring(1))),
          true);
    } catch (Exception e) {
      return true;
    }
  }

  default Collection<String> aliases() {
    if (getClass().isAnnotationPresent(Aliases.class)) {
      return Arrays.asList(getClass().getAnnotation(Aliases.class).value());
    }

    return Collections.emptyList();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @interface Aliases {
    String[] value() default {};
  }
}
