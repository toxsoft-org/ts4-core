package org.toxsoft.core.tsgui.ved.editor.palette;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VED palette entry allows to create VED item with the specified initial configuration.
 * <p>
 * The entries are returned by the factory with method FIXME ???
 *
 * @author hazard157
 */
public interface IVedItemsPaletteEntry
    extends IStridableParameterized {

  /**
   * Creates new instance of the item configuration filled with default values.
   *
   * @param aItemId String - the item ID
   * @return {@link VedItemCfg} - created config instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  VedItemCfg makeCfg( String aItemId );

}
