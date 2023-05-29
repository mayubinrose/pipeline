package com.ctg.pipeline.execute.util;

import static com.netflix.appinfo.InstanceInfo.InstanceStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.StatusChangeEvent;
import com.netflix.spinnaker.kork.eureka.RemoteStatusChangedEvent;

public class NoDiscoveryApplicationStatusPublisher
    implements ApplicationListener<ContextRefreshedEvent> {

  private final ApplicationEventPublisher publisher;
  private final Logger log = LoggerFactory.getLogger(NoDiscoveryApplicationStatusPublisher.class);

  private static InstanceStatus instanceStatus = UNKNOWN;

  public NoDiscoveryApplicationStatusPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.warn("No discovery client is available, assuming application is up");
    setInstanceStatus(UP);
  }

  private void setInstanceStatus(InstanceStatus current) {
    InstanceStatus previous = instanceStatus;
    instanceStatus = current;
    publisher.publishEvent(new RemoteStatusChangedEvent(new StatusChangeEvent(previous, current)));
  }

  public void setInstanceEnabled(boolean enabled) {
    setInstanceStatus(enabled ? UP : OUT_OF_SERVICE);
  }
}
