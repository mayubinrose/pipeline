package com.ctg.pipeline.execute.util;

import org.pf4j.ExtensionPoint;

import com.netflix.spinnaker.kork.annotations.Beta;

/**
 * This should be used by plugin developers to create plugin stages.
 *
 * @param <T> is a class plugin developers will create to have a concrete class that has all of the
 *     fields that are required for the stage to run.
 */
@Beta
public interface SimpleStage<T> extends ExtensionPoint {
  /**
   * When this stage runs, the execute method gets called. It takes in a class that is created that
   * has the data needed by the stage. It returns a class that contains the status of the stage,
   * outputs and context
   *
   * @param simpleStageInput
   * @return status, outputs, and context of the executed stage
   */
  SimpleStageOutput execute(SimpleStageInput<T> simpleStageInput);

  /** @return name of the stage */
  String getName();
}
