package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link ID2Rotation}.
 *
 * @author hazard157
 */
public sealed interface ID2RotationEdit
    extends ID2Rotation permits D2RotationEdit {

  /**
   * Returns an editable rotation center coordinates.
   *
   * @return {@link ID2Point} - editable rotation center coordinates
   */
  @Override
  ID2PointEdit pivotPoint();

  /**
   * Returns an editable rotation angle.
   *
   * @return {@link ID2Angle} - editable rotation angle
   */
  @Override
  ID2AngleEdit rotationAngle();

  /**
   * Sets the rotation information from the source.
   *
   * @param aSource {@link ID2Rotation} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setRotation( ID2Rotation aSource );

}
