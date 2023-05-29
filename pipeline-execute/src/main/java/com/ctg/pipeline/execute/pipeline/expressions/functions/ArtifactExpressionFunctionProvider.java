package com.ctg.pipeline.execute.pipeline.expressions.functions;

import java.util.function.Predicate;

import com.ctg.pipeline.execute.model.Execution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import com.netflix.spinnaker.kork.artifacts.model.Artifact;
import com.netflix.spinnaker.kork.artifacts.model.ExpectedArtifact;
import com.netflix.spinnaker.kork.expressions.ExpressionFunctionProvider;
import com.netflix.spinnaker.kork.expressions.SpelHelperFunctionException;

import static java.lang.String.format;

@Component
public class ArtifactExpressionFunctionProvider implements ExpressionFunctionProvider {
  @Nullable
  @Override
  public String getNamespace() {
    return null;
  }

  @NotNull
  @Override
  public Functions getFunctions() {
    return new Functions(
        new FunctionDefinition(
            "triggerResolvedArtifact",
            "Looks up the an artifact in current execution given its name. If multiple artifacts are found, only 1 will be returned.",
            new FunctionParameter(
                Execution.class, "execution", "The execution to search for artifacts"),
            new FunctionParameter(String.class, "name", "The name of the resolved artifact")),
        new FunctionDefinition(
            "triggerResolvedArtifactByType",
            "Looks up the an artifact in current execution given its type. If multiple artifacts are found, only 1 will be returned.",
            new FunctionParameter(
                Execution.class, "execution", "The execution to search for artifacts"),
            new FunctionParameter(String.class, "type", "The type of the resolved artifact")));
  }

  public static Artifact triggerResolvedArtifact(Execution execution, String name) {
    return triggerResolvedArtifactBy(execution, name, artifact -> name.equals(artifact.getName()));
  }

  public static Artifact triggerResolvedArtifactByType(Execution execution, String type) {
    return triggerResolvedArtifactBy(execution, type, artifact -> type.equals(artifact.getType()));
  }

  private static Artifact triggerResolvedArtifactBy(
      Execution execution, String nameOrType, Predicate<Artifact> predicate) {
    return execution.getTrigger().getResolvedExpectedArtifacts().stream()
        .filter(
            expectedArtifact ->
                expectedArtifact.getBoundArtifact() != null
                    && predicate.test(expectedArtifact.getBoundArtifact()))
        .findFirst()
        .map(ExpectedArtifact::getBoundArtifact)
        .orElseThrow(
            () ->
                new SpelHelperFunctionException(
                    format(
                        "Unable to locate resolved artifact %s in trigger execution %s.",
                        nameOrType, execution.getId())));
  }
}
