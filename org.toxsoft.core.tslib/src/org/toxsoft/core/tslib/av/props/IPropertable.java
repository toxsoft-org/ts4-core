package org.toxsoft.core.tslib.av.props;

/**
 * Mixin interface of entities with properties.
 *
 * @author hazard157
 */
public interface IPropertable
    extends IPropertableRo {

  /**
   * Returns the values of the properties.
   *
   * @return {@link IPropertiesSet} - editable properties set
   */
  @Override
  IPropertiesSet props();

}
