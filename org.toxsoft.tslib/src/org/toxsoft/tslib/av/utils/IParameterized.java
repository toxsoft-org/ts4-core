package org.toxsoft.tslib.av.utils;

import org.toxsoft.tslib.av.opset.IOptionSet;

/**
 * Mixin interface for entities with {@link IOptionSet} parameters.
 *
 * @author hazard157
 */
public interface IParameterized {

  /**
   * Return the parameters.
   *
   * @return {@link IOptionSet} - the parameters
   */
  IOptionSet params();

}
