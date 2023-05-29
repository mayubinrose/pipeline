package com.ctg.pipeline.execute.queue.listeners

import org.springframework.context.event.ApplicationListenerMethodAdapter
import java.lang.reflect.Method

class InspectableApplicationListenerMethodAdapter(
  beanName: String,
  targetClass: Class<*>,
  private val method: Method
) : ApplicationListenerMethodAdapter(beanName, targetClass, method) {

  fun getMethod() = method
}
