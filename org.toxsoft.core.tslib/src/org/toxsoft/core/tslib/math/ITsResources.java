package org.toxsoft.core.tslib.math;

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
  String FMT_ERR_NOT_EXPECTED_AT = "Checked atomic value has type %s instead of %s";

  /**
   * {@link DoubleRange}
   */
  String FMT_ERR_DOUBLE_GT_MAX     = "The value %f is over allowed range %.2g .. %.2g";
  String FMT_ERR_DOUBLE_LT_MIN     = "The value %f is below allowed range %.2g .. %.2g";
  String FMT_ERR_DOUBLE_NOT_FINITE = "The value %f is not a finite number";

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
