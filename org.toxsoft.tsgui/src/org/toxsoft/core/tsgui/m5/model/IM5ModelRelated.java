package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.IM5Model;

/**
 * Mixin interface indicates that class is bind (related) to the specified M5-model.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5ModelRelated<T> {

  /**
   * Returns the model of the entities.
   *
   * @return {@link IM5Model} - the related M5-model
   */
  IM5Model<T> model();

}
