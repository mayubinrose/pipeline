package com.ctg.pipeline.execute.model;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctg.pipeline.execute.util.ContextParameterProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OptionalStageSupport {

  private static final Logger log = LoggerFactory.getLogger(OptionalStageSupport.class);

  private static Map<String, Class<? extends OptionalStageEvaluator>> OPTIONAL_STAGE_TYPES =
      singletonMap("expression", ExpressionOptionalStageEvaluator.class);

  /**
   * A Stage is optional if it has an {@link OptionalStageEvaluator} in its context that evaluates
   * {@code false}.
   */
  public static boolean isOptional(
      Stage stage, ContextParameterProcessor contextParameterProcessor) {
    Map stageEnabled = (Map) stage.getContext().get("stageEnabled");
    String type = stageEnabled == null ? null : (String) stageEnabled.get("type");
    String optionalType = type == null ? null : type.toLowerCase();
    if (!OPTIONAL_STAGE_TYPES.containsKey(optionalType)) {
      if (stage.getSyntheticStageOwner() != null || stage.getParentStageId() != null) {
        Stage parentStage =
            stage.getExecution().getStages().stream()
                .filter(it -> it.getId().equals(stage.getParentStageId()))
                .findFirst()
                .orElseThrow(
                    () ->
                        new IllegalStateException(
                            format("stage %s not found", stage.getParentStageId())));
        return isOptional(parentStage, contextParameterProcessor);
      }
      return false;
    }

    if ("expression".equals(optionalType) && isNullOrEmpty(stageEnabled.get("expression"))) {
      log.warn(
          "No expression found on stage, treating as non-optional (executionId: {}, stageId: {}",
          stage.getExecution().getId(),
          stage.getId());
      return false;
    }

    try {
      return !stage
          .mapTo("/stageEnabled", OPTIONAL_STAGE_TYPES.get(optionalType))
          .evaluate(stage, contextParameterProcessor);
    } catch (InvalidExpression e) {
      log.warn(
          "{}, skipping stage (executionId: {}, stageId: {})",
          e.getMessage(),
          stage.getExecution().getId(),
          stage.getId());
      return true;
    }
  }

  /** Determines whether a stage is optional and should be skipped */
  private interface OptionalStageEvaluator {
    boolean evaluate(Stage stage, ContextParameterProcessor contextParameterProcessor);
  }

  /**
   * An {@link OptionalStageEvaluator} that will evaluate an expression against the current
   * execution.
   */
  private static class ExpressionOptionalStageEvaluator implements OptionalStageEvaluator {

    private String expression;

    public String getExpression() {
      return expression;
    }

    public void setExpression(String expression) {
      this.expression = expression;
    }

    @Override
    public boolean evaluate(Stage stage, ContextParameterProcessor contextParameterProcessor) {
      String expression =
          contextParameterProcessor
              .process(
                  singletonMap("expression", format("${%s}", this.expression)),
                  contextParameterProcessor.buildExecutionContext(stage),
                  true)
              .get("expression")
              .toString();

      Matcher matcher = Pattern.compile("\\$\\{(. *)}").matcher(expression);
      if (matcher.matches()) {
        expression = matcher.group(1);
      }

      if (!asList("true", "false").contains(expression.toLowerCase())) {
        // expression failed to evaluate successfully
        throw new InvalidExpression(
            format("Conditional Expression '%s' could not be evaluated", this.expression));
      }

      return Boolean.valueOf(expression);
    }
  }

  private static boolean isNullOrEmpty(Object object) {
    return object == null || object instanceof String && isBlank((String) object);
  }

  static class InvalidExpression extends RuntimeException {
    InvalidExpression(String message) {
      super(message);
    }
  }
}
