package org.toxsoft.core.tsgui.ved.core.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
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
  private final VedScreenManager   screenManager;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEnvironment( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
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
  // IVedEnvironment
  //

  @Override
  public IVedDataModel dataModel() {
    return dataModel;
  }

  @Override
  public VedScreenManager screenManager() {
    return screenManager;
  }

  @Override
  public IVedLibraryManager libraryManager() {
    return libraryManager;
  }

}
