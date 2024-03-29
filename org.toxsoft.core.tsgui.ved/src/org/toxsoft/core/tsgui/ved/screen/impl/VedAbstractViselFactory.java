package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedViselFactory} base implementation.
 *
 * @author hazard157
 */
public abstract class VedAbstractViselFactory
    extends VedAbstractItemFactory<VedAbstractVisel>
    implements IVedViselFactory {

  /**
   * Constructor.
   *
   * @param aId String - the
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public VedAbstractViselFactory( String aId, Object... aIdsAndValues ) {
    super( aId, aIdsAndValues );
  }

  // ------------------------------------------------------------------------------------
  // IVedItemFactoryBase
  //

  @Override
  final public EVedItemKind kind() {
    return EVedItemKind.VISEL;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  @Override
  protected abstract VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen );

}
