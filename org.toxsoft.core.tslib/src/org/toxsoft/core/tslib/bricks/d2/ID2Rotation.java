package org.toxsoft.core.tslib.bricks.d2;

/**
 * Information about rotation in 2D world.
 *
 * @author hazard157
 */
public sealed interface ID2Rotation permits ID2RotationEdit,D2Rotation {

  /**
   * No rotation singleton.
   */
  ID2Rotation NONE = new D2Rotation( 0, 0, 0, true );

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
