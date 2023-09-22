package org.toxsoft.core.tsgui.ved.api.cfg;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * Configuration data of the screen consisting of VISELs.
 *
 * @author hazard157
 */
public sealed interface IVedScreenCfg
    extends IStridableParameterized permits VedScreenCfg {

  /**
   * Returns the configuration data of all VISELs in the screen.
   *
   * @return {@link IStridablesList}&lt;{@link IVedItemCfg}&gt; - the VISEL configurations list
   */
  IStridablesList<IVedItemCfg> viselCfgs();

  /**
   * Returns the configuration data of all actors in the screen.
   *
   * @return {@link IStridablesList}&lt;{@link IVedItemCfg}&gt; - the actors configurations list
   */
  IStridablesList<IVedItemCfg> actorCfgs();

  /**
   * The drawing area (canvas) parameters.
   *
   * @return {@link IVedCanvasCfg} - canvas configuration data
   */
  IVedCanvasCfg canvasCfg();

  /**
   * Arbitrary additional data stored for this screen.
   *
   * @return {@link IKeepablesStorageRo} - extra data
   */
  IKeepablesStorageRo extraData();

}
