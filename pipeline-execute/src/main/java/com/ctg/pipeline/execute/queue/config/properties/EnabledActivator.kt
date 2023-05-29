package com.ctg.pipeline.execute.queue.config.properties

import com.netflix.spinnaker.q.Activator

class EnabledActivator(override val enabled: Boolean = true) : Activator