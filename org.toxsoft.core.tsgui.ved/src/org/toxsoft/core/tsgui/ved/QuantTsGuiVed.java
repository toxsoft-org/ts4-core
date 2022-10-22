package org.toxsoft.core.tsgui.ved;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantTsGuiVed
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantTsGuiVed() {
    super( QuantTsGuiVed.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    TsInternalErrorRtException.checkNoNull( aAppContext.get( IVedFramework.class ) );
    IVedFramework vedFramework = VedUtils.createFramework();
    aAppContext.set( IVedFramework.class, vedFramework );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ITsguiVedConstants.init( aWinContext );

    // initialize VED
    // ITsGuiContext ctx = new TsGuiContext( aWinContext );
    // IVedEnvironment vedEnv = VedUtils.createEnvironment( ctx );
    // aWinContext.set( IVedEnvironment.class, vedEnv );
    // vedEnv.libraryManager().registerLibrary( new VedStdLibraryShapes() );

    // M5
    // IM5Domain m5 = aWinContext.get( IM5Domain.class );
    // m5.addModel( new VedLibraryM5Model() );
    // m5.addModel( new VedComponentProviderM5Model() );
    // m5.addModel( new VedComponentM5Model() );
  }

}
