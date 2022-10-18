package org.toxsoft.core.tsgui.ved.api.cfgdata;

import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Tailor config data.
 *
 * @author hazard157
 */
public interface IVedTailorCfg
    extends IVedEntityConfig {

  /**
   * Returns the config data of the tailor bindings to the components.
   *
   * @return {@link IStridablesList}&lt;{@link IVedBindingCfg}&gt; - bindings config data
   */
  IStridablesList<IVedBindingCfg> bindingConfigs();

}
