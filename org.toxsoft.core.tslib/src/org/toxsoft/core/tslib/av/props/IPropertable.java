package org.toxsoft.core.tslib.av.props;

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
   * @return {@link IOptionSet} - editable properties set
   */
  IPropertiesSet props();

}
