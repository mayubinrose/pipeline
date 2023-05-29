package com.netflix.spinnaker.orca.q.handler

import com.ctg.pipeline.execute.model.AuthenticatedRequest
import com.ctg.pipeline.execute.model.ExecutionContext
import com.ctg.pipeline.execute.model.Stage
import com.ctg.pipeline.execute.model.User
import com.ctg.pipeline.execute.util.AuthenticatedStage
import com.ctg.pipeline.execute.util.StageNavigator

interface AuthenticationAware {

  val stageNavigator: StageNavigator

  fun Stage.withAuth(block: () -> Unit) {
    val authenticatedUser = stageNavigator
      .ancestors(this)
      .firstOrNull { it.stageBuilder is AuthenticatedStage }
      ?.let { (it.stageBuilder as AuthenticatedStage).authenticatedUser(it.stage).orElse(null) }

    val currentUser = authenticatedUser ?: User().apply {
      email = execution.authentication?.user
      allowedAccounts = execution.authentication?.allowedAccounts
    }

    try {
      ExecutionContext.set(ExecutionContext(
        execution.application,
        currentUser.username,
        execution.type.name.toLowerCase(),
        execution.id,
        this.id,
        execution.origin,
        this.startTime
      ))
      AuthenticatedRequest.propagate(block, false, currentUser).call()
    } finally {
      ExecutionContext.clear()
    }
  }
}
