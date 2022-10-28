package org.toxsoft.core.tsgui.ved.api.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEnvironmentEdit} implementation.
 *
 * @author hazard157
 */
class VedEnvironmentEdit
    implements IVedEnvironmentEdit {

  private final VedFramework  framework;
  private final ITsGuiContext tsContext;

  private final VedDocumentEdit  doc;
  private final VedScreenManager screenManager;

  /**
   * Constructor.
   *
   * @param aFramework {@link VedFramework} - the creator
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEnvironmentEdit( VedFramework aFramework, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    framework = aFramework;
    tsContext = aContext;
    doc = new VedDocumentEdit();
    screenManager = new VedScreenManager( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironmentEdit
  //

  @Override
  public IVedFramework vedFramework() {
    return framework;
  }

  @Override
  public IVedDocumentEdit doc() {
    return doc;
  }

  @Override
  public VedScreenManager screenManager() {
    return screenManager;
  }

  @Override
  public void setDocumentData( IVedDocumentData aData ) {
    // TODO Auto-generated method stub

  }

  @Override
  public IVedDocumentData getDocumentData() {
    VedDocumentData dd = new VedDocumentData();
    // doc props
    dd.documentPropValues().setAll( doc.props() );
    // components
    for( IVedComponent e : doc().components().items() ) {
      dd.componentConfigs().add( VedEntityConfig.ofEntity( e ) );
    }
    // tailors & bindings
    for( IVedTailor e : doc().tailors().items() ) {
      dd.tailorConfigs().add( VedEntityConfig.ofEntity( e ) );
      IStridablesListEdit<IVedBindingCfg> cfgsList = new StridablesList<>();
      for( IVedBinding b : e.bindings() ) {
        cfgsList.add( VedBindingCfg.ofBinding( b ) );
      }
      dd.tailorBindingConfigsEdit().put( e.id(), cfgsList );
    }
    // actors
    for( IVedActor e : doc().actors().items() ) {
      dd.actorConfigs().add( VedEntityConfig.ofEntity( e ) );
    }
    // sections data is not used by VED
    return dd;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    screenManager.close();
    // TODO VedEnvironmentEdit.close()

  }

}
