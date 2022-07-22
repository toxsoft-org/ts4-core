package org.toxsoft.core.tslib.bricks.d2;

/**
 * Coordinates conversion rules between two coordinates system.
 * <p>
 * Covertion from coordinate system A to B is performad as:
 * <ol>
 * <li>A is zoomed (from origin) by {@link #zoomFactor()}, where zoom of 1.0 means no zoom;</li>
 * <li>origin, the point (0.0,0.0) of A will be placed at {@link #origin()} coordinates on B;</li>
 * <li>B will be rotated as specified by {@link #rotation()}.</li>
 * </ol>
 *
 * @author hazard157
 */
public interface ID2Conversion {

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

  /**
   * Returns the rotation parameters.
   *
   * @return {@link ID2Rotation} - the rotation parameters
   */
  ID2Rotation rotation();

}
