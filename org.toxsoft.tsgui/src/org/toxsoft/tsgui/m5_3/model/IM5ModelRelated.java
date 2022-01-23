package org.toxsoft.tsgui.m5_3.model;

import org.toxsoft.tsgui.m5_3.IM5Model;

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
