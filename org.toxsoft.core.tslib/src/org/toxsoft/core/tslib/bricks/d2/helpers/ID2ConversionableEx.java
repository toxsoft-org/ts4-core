package org.toxsoft.core.tslib.bricks.d2.helpers;

import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * {@link ID2Conversionable} extension with notifications.
 *
 * @author hazard157
 */
public interface ID2ConversionableEx
    extends ID2Conversionable {

  /**
   * Returns {@link #getConversion()} change eventer.
   *
   * @return {@link IGenericChangeEventer} - the eventer
   */
  IGenericChangeEventer conversionChangeEventer();

}
