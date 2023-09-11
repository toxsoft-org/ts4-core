package org.toxsoft.core.tslib.av.utils;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Mixin interface for entities with {@link IOptionSet} parameters.
 *
 * @author hazard157
 */
public interface IParameterized {

  /**
   * Returns the parameters - the value of the options.
   *
   * @return {@link IOptionSet} - the parameters
   */
  IOptionSet params();

}
