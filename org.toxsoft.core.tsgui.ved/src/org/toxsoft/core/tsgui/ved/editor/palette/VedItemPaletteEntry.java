package org.toxsoft.core.tsgui.ved.editor.palette;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedItemsPaletteEntry} immutable implementation.
 *
 * @author hazard157
 */
public final class VedItemPaletteEntry
    extends StridableParameterized
    implements IVedItemsPaletteEntry {

  private final IVedItemCfg itemCfg;

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aCfg {@link IVedItemCfg} - the configuration of the item to be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedItemPaletteEntry( String aId, IOptionSet aParams, IVedItemCfg aCfg ) {
    super( aId, aParams );
    TsNullArgumentRtException.checkNull( aCfg );
    itemCfg = aCfg;
  }

  // ------------------------------------------------------------------------------------
  // IVedItemsPaletteEntry
  //

  @Override
  public IVedItemCfg itemCfg() {
    return itemCfg;
  }

}
