package org.toxsoft.core.tslib.bricks.strid.idgen;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link SimpleStridGenerator}
   */
  String FMT_ERR_SG_NEGATIVE_COUNTER  = "Counter (%d) can not be negative number";
  String FMT_ERR_SG_COUNTER_MAX       = "Counter value (%d) is too big, no more STRIDs may be generated";
  String FMT_ERR_SG_NON_IDNAME_PREFIX = "Prefix '%s' must be an IDname";

  /**
   * {@link UuidStridGenerator}
   */
  String FMT_ERR_UU_NON_IDPATH_PREFIX = "Prefix '%s' must be an IDpath";

}
