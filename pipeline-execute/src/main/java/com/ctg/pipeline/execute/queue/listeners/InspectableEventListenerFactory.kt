package com.ctg.pipeline.execute.queue.listeners

import org.springframework.context.ApplicationListener
import org.springframework.context.event.DefaultEventListenerFactory
import java.lang.reflect.Method

class InspectableEventListenerFactory : DefaultEventListenerFactory() {

  override fun createApplicationListener(beanName: String, type: Class<*>, method: Method): ApplicationListener<*> {
    return InspectableApplicationListenerMethodAdapter(beanName, type, method)
  }
}
