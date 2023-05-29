package com.ctg.pipeline.execute.config;

import com.ctg.pipeline.execute.util.NoDiscoveryApplicationStatusPublisher;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import java.lang.management.ManagementFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class DiscoveryPollingConfiguration {

  @Configuration
  @ConditionalOnMissingBean(DiscoveryClient.class)
  public static class NoDiscoveryConfiguration {
    @Autowired ApplicationEventPublisher publisher;

    @Value("${spring.application.name:orca}")
    String appName;

    @Bean
    public ApplicationListener<ContextRefreshedEvent> discoveryStatusPoller() {
      return new NoDiscoveryApplicationStatusPublisher(publisher);
    }

    @Bean
    public String currentInstanceId() {
      return ManagementFactory.getRuntimeMXBean().getName();
    }
  }

  @Configuration
  @ConditionalOnBean(DiscoveryClient.class)
  public static class DiscoveryConfiguration {
    @Bean
    public String currentInstanceId(InstanceInfo instanceInfo) {
      return instanceInfo.getInstanceId();
    }
  }
}
