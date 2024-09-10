package org.toxsoft.core.tslib.bricks.d2;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String FMT_ERR_INV_ZOOM_VALUE    = "Zoom value must be finite double in range %s but not %.3g";
  String FMT_ERR_INV_COOR_VALUE    = "Coordinate value must be finite double but not %s";
  String FMT_ERR_INV_LENGTH_VALUE  = "Length value must be non-negative finite double but not %s";
  String FMT_ERR_INV_RADIANS_VALUE = "Angle radians value must be finite double but not %s";
  String FMT_ERR_INV_DEGREES_VALUE = "Angle degrees value must be finite double but not %s";

  String STR_EQ_Q1   = "Quadrant 1";
  String STR_EQ_Q1_D = "The right-top quadrant with angles 0°..90° (0..π/2)";
  String STR_EQ_Q2   = "Quadrant 2";
  String STR_EQ_Q2_D = "The left-top quadrant with angles 90°..180° (π/2..π)";
  String STR_EQ_Q3   = "Quadrant 3";
  String STR_EQ_Q3_D = "The left-bottom quadrant with angles 180°..270° (π..2*π/3)";
  String STR_EQ_Q4   = "Quadrant 4";
  String STR_EQ_Q4_D = "The right-bottom  quadrant with angles 270°..360° (2*π/3..2*π)";

}
