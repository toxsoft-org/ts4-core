package org.toxsoft.core.tsgui.ved.editor.palette;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * The VED palette entry allows to create VED item with the specified initial configuration.
 * <p>
 * The entries are returned by the factory with method {@link IVedItemFactoryBase#paletteEntries()}.
 *
 * @author hazard157
 */
public interface IVedItemsPaletteEntry
    extends IStridableParameterized {

  /**
   * Returns item configuration filled with default values.
   *
   * @return {@link IVedItemCfg} - configuration of the item
   */
  IVedItemCfg itemCfg();

}
