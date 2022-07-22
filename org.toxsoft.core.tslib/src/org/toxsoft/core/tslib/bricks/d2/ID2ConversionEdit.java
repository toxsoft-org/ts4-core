package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link ID2Conversion}.
 *
 * @author hazard157
 */
public interface ID2ConversionEdit
    extends ID2Conversion {

  /**
   * Sets the zoom factor.
   *
   * @param aZoomFactor double - the zoom factor
   * @throws TsIllegalArgumentRtException invalid value
   */
  void setZoomFactor( double aZoomFactor );

  /**
   * An editable origin.
   * <p>
   * {@inheritDoc}
   */
  @Override
  ID2PointEdit origin();

  /**
   * An editable rotation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  ID2RotationEdit rotation();

}
