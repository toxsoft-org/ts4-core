package org.toxsoft.core.tsgui.ved.incub.lpd;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Live panel data is intended to store information in permament storage using {@link IKeepableEntity} interface.
 *
 * @author hazard157
 */
public interface ILpdContainer
    extends IKeepableEntity {

  /**
   * Returns panel configguration options values.
   *
   * @return {@link IOptionSetEdit} - editable panel configuration
   */
  IOptionSetEdit panelCfg();

  /**
   * Returns components configuraions.
   *
   * @return {@link IListEdit}&lt;{@link ILpdComponentInfo}&gt; - editable list of components configuraions
   */
  IListEdit<ILpdComponentInfo> componentConfigs();

}
