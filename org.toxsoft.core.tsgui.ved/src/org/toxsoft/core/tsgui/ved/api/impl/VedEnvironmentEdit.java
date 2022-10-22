package org.toxsoft.core.tsgui.ved.api.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
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

  private final VedDocumentEdit doc;

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
  public IVedScreenManager screenManager() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDocumentData( IVedDocumentData aData ) {
    // TODO Auto-generated method stub

  }

  @Override
  public IVedDocumentData getDocumentData() {
    // TODO Auto-generated method stub
  }

}
