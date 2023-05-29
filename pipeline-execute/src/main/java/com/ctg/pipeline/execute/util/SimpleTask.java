package com.ctg.pipeline.execute.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ctg.pipeline.execute.basic.task.Task;
import com.ctg.pipeline.execute.basic.task.TaskResult;
import com.ctg.pipeline.execute.exceptions.NoSuchStageException;
import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.netflix.spinnaker.kork.plugins.proxy.ExtensionClassProvider;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SimpleTask implements Task {
  private SimpleStage simpleStage;

  SimpleTask(@Nullable SimpleStage simpleStage) {
    this.simpleStage = simpleStage;
  }

  private SimpleStageInput getStageInput(Stage stage, ObjectMapper objectMapper) {
    try {
      Class<?> extensionClass = ExtensionClassProvider.getExtensionClass(simpleStage);
      List<Class<?>> cArg = Arrays.asList(SimpleStageInput.class);
      Method method = extensionClass.getMethod("execute", cArg.toArray(new Class[0]));
      Type inputType = ResolvableType.forMethodParameter(method, 0).getGeneric().getType();
      Map<TypeVariable, Type> typeVariableMap =
          GenericTypeResolver.getTypeVariableMap(extensionClass);
      Class<?> resolvedType = GenericTypeResolver.resolveType(inputType, typeVariableMap);

      return new SimpleStageInput(objectMapper.convertValue(stage.getContext(), resolvedType));
    } catch (NoSuchMethodException exeception) {
      throw new NoSuchStageException(exeception.getMessage());
    }
  }

  @Nonnull
  public TaskResult execute(@Nonnull Stage stage) {
    ObjectMapper objectMapper = ExecuteObjectMapper.newInstance();
    SimpleStageInput simpleStageInput = getStageInput(stage, objectMapper);
    SimpleStageOutput output = simpleStage.execute(simpleStageInput);
    ExecutionStatus status =
        ExecutionStatus.valueOf(
            Optional.ofNullable(output.getStatus()).orElse(SimpleStageStatus.RUNNING).toString());

    return TaskResult.builder(status)
        .context(
            output.getContext() == null
                ? new HashMap<>()
                : objectMapper.convertValue(
                    Optional.ofNullable(output.getContext()).orElse(Collections.emptyMap()),
                    Map.class))
        .outputs(
            output.getOutput() == null
                ? new HashMap<>()
                : objectMapper.convertValue(
                    Optional.ofNullable(output.getOutput()).orElse(Collections.emptyMap()),
                    Map.class))
        .build();
  }
}
