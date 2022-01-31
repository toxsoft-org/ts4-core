package org.toxsoft.core.tsgui.widgets.mpv.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link MultiPartValueBase}
   */
  String FMT_ERR_VALUE_OUT_OF_RANGE = "Value %d for part '%s' is out of range %d...%d";

  /**
   * {@link Part}
   */
  String FMT_ERR_READ_PART_BEFORE_CHAR = "Part %s excepts %c leading char";
  String FMT_ERR_READ_PART_VALUE       = "Part %s excepts %d ASCII digits representing value";
  String FMT_ERR_READ_PART_AFTER_CHAR  = "Part %s excepts %c trailing char";

}
