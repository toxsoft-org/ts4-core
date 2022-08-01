package org.toxsoft.core.tslib.bricks.d2;

/**
 * Coordinates conversion rules between two coordinates system.
 * <p>
 * Covertion from coordinate system N (normalized coordinates system) to S (screen coordinates system) is performad as:
 * <ol>
 * <li>N will be rotated around origin point by {@link #rotation()} angle.</li>
 * <li>rotated N will be zoomed (from origin) by {@link #zoomFactor()}, where zoom of 1.0 means no zoom;</li>
 * <li>origin, the point (0.0,0.0) of N will be placed at {@link #origin()} coordinates on S;</li>
 * </ol>
 * Note that both normal and screen coordinate systems has Y axis directed from top to bottom (as it is usual for
 * computer display coordinates).
 *
 * @author hazard157
 */
public sealed interface ID2Conversion permits ID2ConversionEdit,D2Conversion {

  /**
   * No conversion parameters singleton.
   */
  ID2Conversion NONE = new D2Conversion( ID2Angle.ZERO, 1.0, ID2Point.ZERO );

  /**
   * Returns the rotation angle.
   *
   * @return {@link ID2Angle} - the rotation angle
   */
  ID2Angle rotation();

  /**
   * Returns the zoom factor where 1.0 means no zoom.
   *
   * @return double - the zoom factor
   */
  double zoomFactor();

  /**
   * Returns the coordinates of origin in after conversion.
   *
   * @return {@link ID2Point} - source origin coordinates on target
   */
  ID2Point origin();

}
