package org.toxsoft.core.tslib.math;

import org.toxsoft.core.tslib.av.impl.AvUtils;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * Common.
   */
  String FMT_ERR_AV_NOT_INTEGER = "Checked atomic value has type %s instead of " + AvUtils.DDID_INTEGER;

  /**
   * {@link LongRange}
   */
  String FMT_ERR_LONG_GT_MAX = "The value %d is over allowed range %d..%d";
  String FMT_ERR_LONG_LT_MIN = "The value %d is below allowed range %d..%d";

  /**
   * {@link IntRange}
   */
  String FMT_ERR_INT_GT_MAX = "The value %d is over allowed range %d..%d";
  String FMT_ERR_INT_LT_MIN = "The value %d is below allowed range %d..%d";

}
