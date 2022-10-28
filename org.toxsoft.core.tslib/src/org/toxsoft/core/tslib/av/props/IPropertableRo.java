package org.toxsoft.core.tslib.av.props;

/**
 * Mixin interface of entities with read-only properties.
 *
 * @author hazard157
 */
public interface IPropertableRo {

  /**
   * Returns the values of the properties.
   *
   * @return {@link IPropertiesSetRo} - read-only properties set
   */
  IPropertiesSetRo props();

}
