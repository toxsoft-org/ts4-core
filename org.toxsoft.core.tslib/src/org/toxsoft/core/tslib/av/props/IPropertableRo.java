package org.toxsoft.core.tslib.av.props;

/**
 * Mixin interface of entities with read-only properties.
 *
 * @author hazard157
 * @param <S> - event source type, the entity characterized by the properties
 */
public interface IPropertableRo<S> {

  /**
   * Returns the values of the properties.
   *
   * @return {@link IPropertiesSetRo} - read-only properties set
   */
  IPropertiesSetRo<S> props();

}
