package com.ctg.pipeline.execute.util;

import static com.ctg.pipeline.execute.model.Execution.ExecutionType.PIPELINE;
import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.*;

import com.ctg.pipeline.execute.model.Execution;
import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.StageContext;
import com.ctg.pipeline.execute.model.UserConfiguredUrlRestrictions;
import com.ctg.pipeline.execute.pipeline.expressions.PipelineExpressionEvaluator.SpelEvaluatorVersion;
import com.ctg.pipeline.execute.pipeline.expressions.functions.*;
import com.ctg.pipeline.execute.pipeline.expressions.PipelineExpressionEvaluator;
import com.ctg.pipeline.execute.queue.model.*;
import com.netflix.spinnaker.kork.expressions.ExpressionEvaluationSummary;
import com.netflix.spinnaker.kork.expressions.ExpressionFunctionProvider;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.netflix.spinnaker.kork.dynamicconfig.DynamicConfigService;

/**
 * Common methods for dealing with passing context parameters used by both Script and Jenkins stages
 */
public class ContextParameterProcessor {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private static final ObjectMapper mapper = ExecuteObjectMapper.getInstance();

  private PipelineExpressionEvaluator expressionEvaluator;
  private DynamicConfigService dynamicConfigService;

  @VisibleForTesting
  public ContextParameterProcessor() {
    this(
        Arrays.asList(
            new ArtifactExpressionFunctionProvider(),
            new DeployedServerGroupsExpressionFunctionProvider(),
            new ManifestLabelValueExpressionFunctionProvider(),
            new StageExpressionFunctionProvider(),
            new UrlExpressionFunctionProvider(new UserConfiguredUrlRestrictions.Builder().build())),
        new DefaultPluginManager(),
        DynamicConfigService.NOOP);
  }

  public ContextParameterProcessor(
      List<ExpressionFunctionProvider> expressionFunctionProviders,
      PluginManager pluginManager,
      DynamicConfigService dynamicConfigService) {
    this.expressionEvaluator =
        new PipelineExpressionEvaluator(expressionFunctionProviders, pluginManager);
    this.dynamicConfigService = dynamicConfigService;
  }

  public Map<String, Object> process(
      Map<String, Object> source, Map<String, Object> context, boolean allowUnknownKeys) {
    ExpressionEvaluationSummary summary = new ExpressionEvaluationSummary();

    return process(source, context, allowUnknownKeys, summary);
  }

  /**
   * Process pipeline to evaluate spel expressions. Note that 'stages' key is not processed if we
   * are using spel v4
   */
  public Map<String, Object> processPipeline(
      Map<String, Object> pipeline, Map<String, Object> context, boolean allowUnknownKeys) {

    final String spelEvaluatorKey = "spelEvaluator";

    ExpressionEvaluationSummary summary = new ExpressionEvaluationSummary();
    SpelEvaluatorVersion spelEvaluatorVersion =
        getEffectiveSpelVersionToUse((String) pipeline.get(spelEvaluatorKey));

    Object stages = null;
    if (SpelEvaluatorVersion.V4.equals(spelEvaluatorVersion)) {
      stages = pipeline.remove("stages");
    }

    Map<String, Object> processedPipeline = process(pipeline, context, allowUnknownKeys, summary);

    if (SpelEvaluatorVersion.V4.equals(spelEvaluatorVersion)) {
      processedPipeline.put("stages", stages);
    }

    return processedPipeline;
  }

  public Map<String, Object> process(
      Map<String, Object> source,
      Map<String, Object> context,
      boolean allowUnknownKeys,
      ExpressionEvaluationSummary summary) {

    if (source == null) {
      return null;
    }

    if (source.isEmpty()) {
      return new HashMap<>();
    }

    Map<String, Object> result =
        expressionEvaluator.evaluate(source, precomputeValues(context), summary, allowUnknownKeys);

    if (summary.getTotalEvaluated() > 0 && context.containsKey("execution")) {
      log.info("Evaluated {}", summary);
    }

    if (summary.getFailureCount() > 0) {
      result.put(
          PipelineExpressionEvaluator.SUMMARY,
          mapper.convertValue(summary.getExpressionResult(), Map.class));
    }

    return result;
  }

  /**
   * Builds a context for the SpEL evaluator to use while processing a stage This involves merging
   * the following into a map - the stage context - execution object (if PIPELINE) - trigger object
   * (if PIPELINE)
   *
   * @param stage Stage to build context for
   * @return StageContext (really a map) for the merged context
   */
  public StageContext buildExecutionContext(Stage stage) {
    Map<String, Object> augmentedContext = new HashMap<>(stage.getContext());
    Execution execution = stage.getExecution();

    if (execution.getType() == PIPELINE) {
      augmentedContext.putAll(buildExecutionContext(execution));

      // MPTv2 uses templateVariables which used to be expanded at pipeline creation time.
      // With SpEL V4 we don't preprocess the whole pipeline anymore, hence we append
      // "templateVariables" to the SpEL
      // evaluation context here so that those vars can be processed and expanded when the stage
      // runs
      SpelEvaluatorVersion spelEvaluatorVersion =
          getEffectiveSpelVersionToUse((String) execution.getSpelEvaluator());

      if (SpelEvaluatorVersion.V4.equals(spelEvaluatorVersion)) {
        Map templatedVariables = execution.getTemplateVariables();
        if (templatedVariables != null && !templatedVariables.isEmpty()) {
          augmentedContext.put("templateVariables", templatedVariables);
        }
      }
    }

    return new StageContext(stage, augmentedContext);
  }

  /**
   * Builds a context for the SpEL evaluator to use while processing an execution This involves
   * merging the following into a map - execution object (if PIPELINE) - trigger object (if
   * PIPELINE)
   *
   * @param execution Execution to build context for
   * @return Map of the merged context
   */
  public Map<String, Object> buildExecutionContext(Execution execution) {
    Map<String, Object> executionContext = new HashMap<>();

    executionContext.put("execution", execution);
    executionContext.put(
        "trigger",
        mapper.convertValue(execution.getTrigger(), new TypeReference<Map<String, Object>>() {}));

    return executionContext;
  }

  public static boolean containsExpression(String value) {
    return isNotEmpty(value) && value.contains("${");
  }

  public SpelEvaluatorVersion getEffectiveSpelVersionToUse(String executionSpelVersion) {
    String override =
        dynamicConfigService.getConfig(String.class, "expression.spel-version-override", "");

    if (Strings.isNullOrEmpty(override)) {
      return SpelEvaluatorVersion.fromStringKey(executionSpelVersion);
    }

    return SpelEvaluatorVersion.fromStringKey(override);
  }

  private Map<String, Object> precomputeValues(Map<String, Object> context) {
    // Copy the data over so we don't mutate the original context!
    if (context instanceof StageContext) {
      context = new StageContext((StageContext) context);
    } else {
      context = new HashMap<>(context);
    }

    Object rawTrigger = context.get("trigger");
    Trigger trigger;
    if (rawTrigger != null && !(rawTrigger instanceof Trigger)) {
      trigger = mapper.convertValue(rawTrigger, Trigger.class);
    } else {
      trigger = (Trigger) rawTrigger;
    }

    if (trigger != null && !trigger.getParameters().isEmpty()) {
      context.put("parameters", trigger.getParameters());
    } else {
      if (!context.containsKey("parameters")) {
        context.put("parameters", EMPTY_MAP);
      }
    }

    if (context.get("buildInfo") instanceof BuildInfo) {
      context.put(
          "scmInfo",
          Optional.ofNullable((BuildInfo) context.get("buildInfo"))
              .map(BuildInfo::getScm)
              .orElse(null));
    }

    if (context.get("scmInfo") == null && trigger instanceof JenkinsTrigger) {
      context.put(
          "scmInfo",
          Optional.ofNullable(((JenkinsTrigger) trigger).getBuildInfo())
              .map(BuildInfo::getScm)
              .orElse(emptyList()));
    }
    if (context.get("scmInfo") == null && trigger instanceof ConcourseTrigger) {
      context.put(
          "scmInfo",
          Optional.ofNullable(((ConcourseTrigger) trigger).getBuildInfo())
              .map(BuildInfo::getScm)
              .orElse(emptyList()));
    }
    if (context.get("scmInfo") != null && ((List) context.get("scmInfo")).size() >= 2) {
      List<SourceControl> scmInfos = (List<SourceControl>) context.get("scmInfo");
      SourceControl scmInfo =
          scmInfos.stream()
              .filter(it -> !"master".equals(it.getBranch()) && !"develop".equals(it.getBranch()))
              .findFirst()
              .orElseGet(() -> scmInfos.get(0));
      context.put("scmInfo", scmInfo);
    } else if (context.get("scmInfo") != null && !((List) context.get("scmInfo")).isEmpty()) {
      context.put("scmInfo", ((List) context.get("scmInfo")).get(0));
    } else {
      context.put("scmInfo", null);
    }

    return context;
  }
}
