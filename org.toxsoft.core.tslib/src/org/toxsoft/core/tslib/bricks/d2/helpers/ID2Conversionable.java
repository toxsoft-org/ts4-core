package org.toxsoft.core.tslib.bricks.d2.helpers;

import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Mixin interface of entities supporting converson of one coordinate space to another.
 *
 * @author hazard157
 */
public interface ID2Conversionable {

  /**
   * Returns current conversion of one coordinate space to another.
   *
   * @return {@link ID2Conversion} - current conversion parameters
   */
  ID2Conversion getConversion();

  /**
   * Sets coordinate speces conversion parameters .
   *
   * @param aConversion {@link ID2Conversion} - conversion parameters
   */
  void setConversion( ID2Conversion aConversion );

}
