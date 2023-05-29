package com.ctg.pipeline.execute.config;

import com.ctg.pipeline.echo.api.IEchoService;
import com.ctg.pipeline.execute.events.ExecutionEvent;
import com.ctg.pipeline.execute.events.ExecutionListenerAdapter;
import com.ctg.pipeline.execute.events.StageListenerAdapter;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.ctg.pipeline.execute.queue.listeners.echo.EchoNotifyingExecutionListener;
import com.ctg.pipeline.execute.queue.listeners.echo.EchoNotifyingStageListener;
import com.ctg.pipeline.execute.util.ContextParameterProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EchoConfiguration {

  @Autowired
  ObjectMapper objectMapper;


  @Bean
  EchoNotifyingStageListener echoNotifyingStageExecutionListener(IEchoService echoService, ExecutionRepository repository, ContextParameterProcessor contextParameterProcessor) {
    return new EchoNotifyingStageListener(echoService, repository, contextParameterProcessor);
  }

  @Bean
  ApplicationListener<ExecutionEvent> echoNotifyingStageExecutionListenerAdapter(EchoNotifyingStageListener echoNotifyingStageListener, ExecutionRepository repository) {
    return new StageListenerAdapter(echoNotifyingStageListener, repository);
  }

  @Bean
  EchoNotifyingExecutionListener echoNotifyingPipelineExecutionListener(
          IEchoService echoService,
          ObjectMapper objectMapper,
          ContextParameterProcessor contextParameterProcessor) {
    return new EchoNotifyingExecutionListener(echoService, objectMapper, contextParameterProcessor);
  }

  @Bean
  ApplicationListener<ExecutionEvent> echoNotifyingPipelineExecutionListenerAdapter(EchoNotifyingExecutionListener echoNotifyingExecutionListener, ExecutionRepository repository) {
    return new ExecutionListenerAdapter(echoNotifyingExecutionListener, repository);
  }
}
