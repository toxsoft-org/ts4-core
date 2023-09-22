package org.toxsoft.core.tsgui.ved.zver2.api.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.zver2.api.*;
import org.toxsoft.core.tsgui.ved.zver2.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.zver2.api.entity.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * {@link IVedFramework} implementation.
 *
 * @author hazard157
 */
class VedFramework
    implements IVedFramework {

  private final IMapEdit<EVedEntityKind, IVedEntityProvidersRegistry> registriesMap = new ElemMap<>();

  /**
   * Constructor.
   */
  public VedFramework() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IVedFramework
  //

  @Override
  public IVedEntityProvidersRegistry getEntityProvidersRegistry( EVedEntityKind aKind ) {
    return registriesMap.getByKey( aKind );
  }

  @Override
  public IVedEnvironment createEnvironment( ITsGuiContext aContext, IVedDocumentData aDocumentData ) {
    IVedEnvironmentEdit vedEnv = new VedEnvironmentEdit( this, aContext );
    vedEnv.setDocumentData( aDocumentData );
    return vedEnv;
  }

  @Override
  public IVedEnvironmentEdit createEnvironmentEdit( ITsGuiContext aContext ) {
    return new VedEnvironmentEdit( this, aContext );
  }

}
