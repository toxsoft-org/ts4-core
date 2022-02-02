package org.toxsoft.core.tslib.bricks.ctx;

/**
 * Mixin interface of entities with context {@link ITsContext}.
 *
 * @author hazard157
 */
public interface ITsContextable {

  /**
   * Returns the context of the entity.
   *
   * @return {@link ITsContext} - the context of the entity
   */
  ITsContext tsContext();

}
