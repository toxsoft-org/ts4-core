package org.toxsoft.core.tsgui.ved.api.cfg;

import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Configuration of the canvas - the screen drawing area.
 *
 * @author hazard157
 */
public sealed interface IVedCanvasCfg
    extends IParameterized permits VedCanvasCfg {

  /**
   * Returns the screen background drawing parameters.
   *
   * @return {@link TsFillInfo} - background drawing parameters
   */
  TsFillInfo fillInfo();

  /**
   * Returns the screen size - the width and height.
   * <p>
   * TODO what is the normalized units if the coordinates/lengths ???
   *
   * @return {@link ID2Point} - screen size in the normalized units
   */
  ID2Point size();

}
