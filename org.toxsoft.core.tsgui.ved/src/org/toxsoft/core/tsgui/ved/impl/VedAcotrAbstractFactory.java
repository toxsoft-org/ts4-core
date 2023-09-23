package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedActorFactory} base implementation.
 *
 * @author hazard157
 */
public abstract class VedAcotrAbstractFactory
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
  public VedAcotrAbstractFactory( String aId, Object... aIdsAndValues ) {
    super( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  @Override
  protected abstract VedAbstractActor doCreate( IVedItemCfg aCfg, IVedEnvironment aEnv );

}
