package com.ctg.pipeline.execute.queue.listeners

import com.ctg.pipeline.execute.queue.listeners.annotations.Sync
import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ApplicationEventMulticaster
import org.springframework.core.ResolvableType

/**
 * Supports sync & async event listeners. Listeners are treated as asynchronous unless
 * explicitly marked as synchronous via the {@code Sync} annotation.
 */
class DelegatingApplicationEventMulticaster(
  private val syncApplicationEventMulticaster: ApplicationEventMulticaster,
  private val asyncApplicationEventMulticaster: ApplicationEventMulticaster
) : ApplicationEventMulticaster, BeanFactoryAware, BeanClassLoaderAware {

  override fun multicastEvent(event: ApplicationEvent?) {
    asyncApplicationEventMulticaster.multicastEvent(event)
    syncApplicationEventMulticaster.multicastEvent(event)
  }

  override fun multicastEvent(event: ApplicationEvent?, eventType: ResolvableType?) {
    asyncApplicationEventMulticaster.multicastEvent(event, eventType)
    syncApplicationEventMulticaster.multicastEvent(event, eventType)
  }

  override fun addApplicationListener(listener: ApplicationListener<*>) {
    if (isSynchronous(listener)) {
      syncApplicationEventMulticaster.addApplicationListener(listener)
    } else {
      asyncApplicationEventMulticaster.addApplicationListener(listener)
    }
  }

  private fun isSynchronous(listener: ApplicationListener<*>): Boolean {
    if (listener.javaClass.getAnnotation(Sync::class.java) != null) {
      return true
    }
    if (listener is InspectableApplicationListenerMethodAdapter &&
      listener.getMethod().getAnnotation(Sync::class.java) != null) {
      return true
    }
    return false
  }

  override fun addApplicationListenerBean(listenerBeanName: String) {
    // Bean-name based listeners are async-only.
    asyncApplicationEventMulticaster.addApplicationListenerBean(listenerBeanName)
  }

  override fun removeApplicationListener(listener: ApplicationListener<*>) {
    asyncApplicationEventMulticaster.removeApplicationListener(listener)
    syncApplicationEventMulticaster.removeApplicationListener(listener)
  }

  override fun removeAllListeners() {
    asyncApplicationEventMulticaster.removeAllListeners()
    syncApplicationEventMulticaster.removeAllListeners()
  }

  override fun removeApplicationListenerBean(listenerBeanName: String) {
    // Bean-name based listeners are async-only.
    asyncApplicationEventMulticaster.removeApplicationListenerBean(listenerBeanName)
  }

  override fun setBeanFactory(beanFactory: BeanFactory?) {
    if (asyncApplicationEventMulticaster is BeanFactoryAware) {
      asyncApplicationEventMulticaster.setBeanFactory(beanFactory)
    }
    if (syncApplicationEventMulticaster is BeanFactoryAware) {
      syncApplicationEventMulticaster.setBeanFactory(beanFactory)
    }
  }

  override fun setBeanClassLoader(classLoader: ClassLoader?) {
    if (asyncApplicationEventMulticaster is BeanClassLoaderAware) {
      asyncApplicationEventMulticaster.setBeanClassLoader(classLoader)
    }
    if (syncApplicationEventMulticaster is BeanClassLoaderAware) {
      syncApplicationEventMulticaster.setBeanClassLoader(classLoader)
    }
  }
}
