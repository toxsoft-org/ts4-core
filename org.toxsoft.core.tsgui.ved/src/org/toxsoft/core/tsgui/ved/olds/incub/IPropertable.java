package org.toxsoft.core.tsgui.ved.olds.incub;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Mixin interface of entities with properties.
 *
 * @author hazard157
 */
public interface IPropertable {

  /**
   * Returns the values of the properties.
   *
   * @return {@link IOptionSet} - props values
   */
  IOptionSet props();

  /**
   * Returns the meta information about properties.
   *
   * @return {@link IPropertableDef} - properties meta-information
   */
  IPropertableDef def();

}
