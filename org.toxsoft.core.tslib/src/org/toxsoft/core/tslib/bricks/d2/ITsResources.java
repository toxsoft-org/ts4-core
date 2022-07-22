package org.toxsoft.core.tslib.bricks.d2;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String FMT_ERR_INV_ZOOM_VALUE    = "Zoom value must be finite double in range %f..%f but not %f";
  String FMT_ERR_INV_COOR_VALUE    = "Coordinate value must be finite double but not %s";
  String FMT_ERR_INV_LENGTH_VALUE  = "Length value must be non-negative finite double but not %s";
  String FMT_ERR_INV_RADIANS_VALUE = "Angle radians value must be finite double but not %s";
  String FMT_ERR_INV_DEGREES_VALUE = "Angle degrees value must be finite double but not %s";

}
