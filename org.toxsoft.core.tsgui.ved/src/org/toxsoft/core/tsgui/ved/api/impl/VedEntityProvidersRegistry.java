package org.toxsoft.core.tsgui.ved.api.impl;

import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEntityProvidersRegistry} implementation.
 *
 * @author hazard157
 */
class VedEntityProvidersRegistry
    implements IVedEntityProvidersRegistry {

  private final IStridablesListEdit<IVedEntityProvider> providersList = new StridablesList<>();

  private final EVedEntityKind kind;

  /**
   * Consytructor.
   *
   * @param aKind {@link EVedEntityKind} - entity kind
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEntityProvidersRegistry( EVedEntityKind aKind ) {
    TsNullArgumentRtException.checkNull( aKind );
    kind = aKind;
  }

  // ------------------------------------------------------------------------------------
  // IVedEntityProvidersRegistry
  //

  @Override
  public EVedEntityKind entityKind() {
    return kind;
  }

  @Override
  public IStridablesList<IVedEntityProvider> providers() {
    return providersList;
  }

  @Override
  public void registerProvider( IVedEntityProvider aProvider ) {
    TsNullArgumentRtException.checkNull( aProvider );
    TsIllegalArgumentRtException.checkTrue( aProvider.entityKind() != kind );
    TsItemAlreadyExistsRtException.checkTrue( providersList.hasKey( aProvider.id() ) );
    providersList.add( aProvider );
  }

}
