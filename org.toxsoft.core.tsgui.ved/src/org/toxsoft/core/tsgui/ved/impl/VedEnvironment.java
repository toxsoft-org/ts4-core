package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEnvironment} implementation.
 *
 * @author hazard157
 */
class VedEnvironment
    implements IVedEnvironment {

  private final ITsGuiContext      tsContext;
  private final IVedLibraryManager libraryManager = new VedLibraryManager();
  private final IVedDataModel      dataModel      = new VedDataModel();

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEnvironment( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @Override
  public IVedDataModel dataModel() {
    return dataModel;
  }

  @Override
  public IVedScreenManager screenManager() {
    // TODO реализовать VedEnvironment.screenManager()
    throw new TsUnderDevelopmentRtException( "VedEnvironment.screenManager()" );
  }

  @Override
  public IVedLibraryManager libraryManager() {
    return libraryManager;
  }

}
