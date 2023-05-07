package org.toxsoft.core.tslib.bricks.d2.helpers;

import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * An editable extension of {@link ID2GeomData}.
 *
 * @author hazard157
 */
public sealed interface ID2GeomDataEdit
    extends ID2GeomData
    permits D2GeomDataEdit {

  /**
   * Returns the location of the figure on the plane.
   *
   * @return {@link ID2Point} - the editable location of the local origin on the global plane
   */
  @Override
  ID2PointEdit location();

  /**
   * Returns the figure conversion information.
   * <p>
   * Coordinates of the {@link ID2Conversion#origin()} are given in the figures local coordinates.
   *
   * @return {@link ID2Conversion} - the editable figure conversion information
   */
  @Override
  ID2ConversionEdit conversion();

}
