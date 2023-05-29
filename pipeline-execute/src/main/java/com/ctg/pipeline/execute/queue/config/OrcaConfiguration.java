package com.ctg.pipeline.execute.queue.config;

import com.ctg.pipeline.execute.basic.task.Task;
import com.ctg.pipeline.execute.basic.task.TaskResolver;
import com.ctg.pipeline.execute.events.ExecutionEvent;
import com.ctg.pipeline.execute.events.ExecutionListenerAdapter;
import com.ctg.pipeline.execute.exceptions.DefaultExceptionHandler;
import com.ctg.pipeline.execute.model.UserConfiguredUrlRestrictions;
import com.ctg.pipeline.execute.pipeline.persistence.ExecutionRepository;
import com.ctg.pipeline.execute.queue.commands.ForceExecutionCancellationCommand;
import com.ctg.pipeline.execute.queue.listeners.*;
import com.ctg.pipeline.execute.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.spectator.api.Registry;
import com.netflix.spinnaker.kork.core.RetrySupport;
import com.netflix.spinnaker.kork.dynamicconfig.DynamicConfigService;
import com.netflix.spinnaker.kork.expressions.ExpressionFunctionProvider;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.springframework.context.annotation.AnnotationConfigUtils.EVENT_LISTENER_FACTORY_BEAN_NAME;

@Configuration
@ComponentScan({
        "com.ctg.pipeline.execute", "com.netflix.spinnaker.orca.q", "com.netflix.spinnaker.orca.telemetry"
})
//@Import({
//  PreprocessorConfiguration.class,
//  PluginsAutoConfiguration.class,
//})
@EnableConfigurationProperties
public class OrcaConfiguration {
  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean
  public Duration minInactivity() {
    return Duration.of(3, MINUTES);
  }

  @Bean(destroyMethod = "")
  public Scheduler scheduler() {
    return Schedulers.io();
  }

  @Bean
  public ObjectMapper mapper() {
    return ExecuteObjectMapper.getInstance();
  }

  @Bean
  @Order
  public DefaultExceptionHandler defaultExceptionHandler() {
    return new DefaultExceptionHandler();
  }

  @Bean
  public ExecutionCleanupListener executionCleanupListener() {
    return new ExecutionCleanupListener();
  }

  @Bean
  public ApplicationListener<ExecutionEvent> executionCleanupListenerAdapter(
          ExecutionListener executionCleanupListener, ExecutionRepository repository) {
    return new ExecutionListenerAdapter(executionCleanupListener, repository);
  }

//  @Bean
//  @ConditionalOnProperty(value = "jar-diffs.enabled", matchIfMissing = false)
//  public ComparableLooseVersion comparableLooseVersion() {
//    return new DefaultComparableLooseVersion();
//  }

//  @Bean
//  @ConfigurationProperties("user-configured-url-restrictions")
//  public UserConfiguredUrlRestrictions.Builder userConfiguredUrlRestrictionProperties() {
//    return new UserConfiguredUrlRestrictions.Builder();
//  }

  @Bean
  UserConfiguredUrlRestrictions userConfiguredUrlRestrictions(
      UserConfiguredUrlRestrictions.Builder userConfiguredUrlRestrictionProperties) {
    return userConfiguredUrlRestrictionProperties.build();
  }

  @Bean
  public ContextParameterProcessor contextParameterProcessor(
      List<ExpressionFunctionProvider> expressionFunctionProviders,
      @Autowired(required = false)PluginManager pluginManager,
      DynamicConfigService dynamicConfigService) {
    return new ContextParameterProcessor(
        expressionFunctionProviders, pluginManager, dynamicConfigService);
  }

  @Bean
  public ApplicationListener<ExecutionEvent> onCompleteMetricExecutionListenerAdapter(
      Registry registry, ExecutionRepository repository) {
    return new ExecutionListenerAdapter(new MetricsExecutionListener(registry), repository);
  }

  @Bean
  @ConditionalOnMissingBean(StageDefinitionBuilderFactory.class)
  public StageDefinitionBuilderFactory stageDefinitionBuilderFactory(StageResolver stageResolver) {
    return new DefaultStageDefinitionBuilderFactory(stageResolver);
  }

  @Bean
  public RetrySupport retrySupport() {
    return new RetrySupport();
  }

  @Bean
  public ApplicationEventMulticaster applicationEventMulticaster(
      @Qualifier("applicationEventTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
    // TODO rz - Add error handlers
    SimpleApplicationEventMulticaster async = new SimpleApplicationEventMulticaster();
    async.setTaskExecutor(taskExecutor);
    SimpleApplicationEventMulticaster sync = new SimpleApplicationEventMulticaster();

    return new DelegatingApplicationEventMulticaster(sync, async);
  }

  @Bean
  public ThreadPoolTaskExecutor applicationEventTaskExecutor() {
    ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
    threadPool.setThreadNamePrefix("events-");
    threadPool.setCorePoolSize(20);
    threadPool.setMaxPoolSize(20);
    return threadPool;
  }

  @Bean
  public ThreadPoolTaskExecutor cancellableStageExecutor() {
    ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
    threadPool.setThreadNamePrefix("cancel-");
    threadPool.setCorePoolSize(5);
    threadPool.setMaxPoolSize(10);
    threadPool.setQueueCapacity(20);
    return threadPool;
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix("scheduler-");
    scheduler.setPoolSize(10);
    return scheduler;
  }

  @Bean
  public TaskResolver taskResolver(Collection<Task> tasks) {
    return new TaskResolver(tasks, true);
  }

  @Bean
  public StageResolver stageResolver(
      Collection<StageDefinitionBuilder> stageDefinitionBuilders,
      Optional<List<SimpleStage>> simpleStages) {
    List<SimpleStage> stages = simpleStages.orElseGet(ArrayList::new);
    return new StageResolver(stageDefinitionBuilders, stages);
  }

  @Bean(name = EVENT_LISTENER_FACTORY_BEAN_NAME)
  public EventListenerFactory eventListenerFactory() {
    return new InspectableEventListenerFactory();
  }

  @Bean
  public ForceExecutionCancellationCommand forceExecutionCancellationCommand(
      ExecutionRepository executionRepository, Clock clock) {
    return new ForceExecutionCancellationCommand(executionRepository, clock);
  }

  @Bean
  public CompoundExecutionOperator compoundExecutionOperator(
      ExecutionRepository repository, ExecutionRunner runner, RetrySupport retrySupport) {
    return new CompoundExecutionOperator(repository, runner, retrySupport);
  }
}
