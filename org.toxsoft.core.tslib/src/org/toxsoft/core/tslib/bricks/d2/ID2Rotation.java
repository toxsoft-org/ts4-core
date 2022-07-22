package org.toxsoft.core.tslib.bricks.d2;

/**
 * Information about rotation in 2D world.
 *
 * @author hazard157
 */
public interface ID2Rotation {

  /**
   * Returns the rotation center coordinates.
   *
   * @return {@link ID2Point} - rotation center coordinates
   */
  ID2Point pivotPoint();

  /**
   * Returns the rotation angle.
   *
   * @return {@link ID2Angle} - rotation angle
   */
  ID2Angle rotationAngle();

}
