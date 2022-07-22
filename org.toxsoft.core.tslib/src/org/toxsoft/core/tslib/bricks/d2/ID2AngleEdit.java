package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link ID2Angle}.
 *
 * @author hazard157
 */
public sealed interface ID2AngleEdit
    extends ID2Angle permits D2AngleEdit {

  /**
   * Sets angle as ridians.
   *
   * @param aRadians double - angle in radians
   * @throws TsIllegalArgumentRtException invalud value
   */
  void setRad( double aRadians );

  /**
   * Changes angle on specified amount of ridians.
   *
   * @param aDeltaRadians double - angle in radians
   * @throws TsIllegalArgumentRtException invalud value
   */
  void changeRad( double aDeltaRadians );

  /**
   * Sets angle as ridians.
   *
   * @param aDegrees double - angle in degrees
   * @throws TsIllegalArgumentRtException invalud value
   */
  void setDeg( double aDegrees );

  /**
   * Changes angle on specified amount of ridians.
   *
   * @param aDeltaDegrees double - angle in degrees
   * @throws TsIllegalArgumentRtException invalud value
   */
  void changeDeg( double aDeltaDegrees );

  /**
   * Sets angle from the source.
   *
   * @param aAngle {@link ID2Angle} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setAngle( ID2Angle aAngle );

}
