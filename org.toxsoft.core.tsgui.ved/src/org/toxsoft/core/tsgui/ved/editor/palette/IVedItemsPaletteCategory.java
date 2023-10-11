package org.toxsoft.core.tsgui.ved.editor.palette;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * VED palette category is optional means to visually group entries in the palette panel.
 *
 * @author hazard157
 */
public interface IVedItemsPaletteCategory
    extends IStridableParameterized {

  /**
   * Returns entries in this category.
   *
   * @return {@link IStridablesList}&lt;{@link IVedItemsPaletteEntry}&gt; - the entries list
   */
  IStridablesList<IVedItemsPaletteEntry> listEntries();

}
