package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link ID2Point}.
 *
 * @author hazard157
 */
public sealed interface ID2PointEdit
    extends ID2Point permits D2PointEdit {

  /**
   * Sets X coordinate.
   *
   * @param aX double - X coordinate
   * @throws TsIllegalArgumentRtException invalid double number
   */
  void setX( double aX );

  /**
   * Sets Y coordinate.
   *
   * @param aY double - Y coordinate
   * @throws TsIllegalArgumentRtException invalid double number
   */
  void setY( double aY );

  /**
   * Sets new coordinates of point.
   *
   * @param aX double - X coordinate
   * @param aY double - Y coordinate
   * @throws TsIllegalArgumentRtException invalid double number
   */
  void setPoint( double aX, double aY );

  /**
   * Sets new coordinates of point.
   *
   * @param aPoint {@link ID2Point} - source point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setPoint( ID2Point aPoint );

  /**
   * Shifts point on specified amount.
   *
   * @param aDeltaX double - value to shift X coordinate
   * @param aDeltaY double - value to shift Y coordinate
   * @throws TsIllegalArgumentRtException invalid double number
   */
  void shiftOn( double aDeltaX, double aDeltaY );

}
