package com.ctg.pipeline.execute.util;

import com.ctg.pipeline.execute.model.Stage;
import com.ctg.pipeline.execute.model.User;

import java.util.Optional;


/**
 * This interface allows an implementing StageDefinitionBuilder to override the default pipeline
 * authentication context.
 */
public interface AuthenticatedStage {
  Optional<User> authenticatedUser(Stage stage);
}
