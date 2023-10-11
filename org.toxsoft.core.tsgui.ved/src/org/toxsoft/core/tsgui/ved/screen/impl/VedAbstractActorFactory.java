package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedActorFactory} base implementation.
 *
 * @author hazard157
 */
public abstract class VedAbstractActorFactory
    extends VedAbstractItemFactory<VedAbstractActor>
    implements IVedActorFactory {

  /**
   * Constructor.
   *
   * @param aId String - the
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public VedAbstractActorFactory( String aId, Object... aIdsAndValues ) {
    super( aId, aIdsAndValues );
  }

  // ------------------------------------------------------------------------------------
  // IVedItemFactoryBase
  //

  @Override
  final public EVedItemKind kind() {
    return EVedItemKind.ACTOR;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  @Override
  protected abstract VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen );

}
