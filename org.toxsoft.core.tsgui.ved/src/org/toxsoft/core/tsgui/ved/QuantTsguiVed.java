package org.toxsoft.core.tsgui.ved;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.glib.library.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.std.library.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantTsguiVed
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantTsguiVed() {
    super( QuantTsguiVed.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ITsguiVedConstants.init( aWinContext );
    // initialize VED
    IVedEnvironment vedEnv = VedUtils.createEnvironment();
    aWinContext.set( IVedEnvironment.class, vedEnv );
    vedEnv.libraryManager().registerLibrary( new VedStdLibraryShapes() );
    // M5
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new VedLibraryM5Model() );
    m5.addModel( new VedComponentProviderM5Model() );
  }

}
