package org.toxsoft.core.tslib.av.props;

/**
 * Mixin interface of entities with properties.
 *
 * @author hazard157
 * @param <S> - event source type, the entity characterized by the properties
 */
public interface IPropertable<S>
    extends IPropertableRo<S> {

  /**
   * Returns the values of the properties.
   *
   * @return {@link IPropertiesSet} - editable properties set
   */
  @Override
  IPropertiesSet<S> props();

}
