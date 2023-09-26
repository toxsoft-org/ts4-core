package org.toxsoft.core.tsgui.ved;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tsgui.ved.impl.*;

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
    aAppContext.set( IVedViselFactoriesRegistry.class, new VedViselFactoriesRegistry() );
    aAppContext.set( IVedActorFactoriesRegistry.class, new VedActorFactoriesRegistry() );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ITsguiVedConstants.init( aWinContext );
  }

}
