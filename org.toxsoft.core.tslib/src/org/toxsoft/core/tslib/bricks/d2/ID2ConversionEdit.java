package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link ID2Conversion}.
 *
 * @author hazard157
 */
public sealed interface ID2ConversionEdit
    extends ID2Conversion permits D2ConversionEdit {

  /**
   * An editable rotation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  ID2AngleEdit rotation();

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
   * Copies conversion parameters.
   *
   * @param aSource {@link ID2Conversion} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setConversion( ID2Conversion aSource );

}
