package org.toxsoft.core.tsgui.ved;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.m5.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;

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
    VedViselFactoriesRegistry visFact = new VedViselFactoriesRegistry();
    aAppContext.set( IVedViselFactoriesRegistry.class, visFact );
    visFact.register( ViselRectangle.FACTORY );
    //
    VedActorFactoriesRegistry actFact = new VedActorFactoriesRegistry();
    aAppContext.set( IVedActorFactoriesRegistry.class, actFact );

  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ITsguiVedConstants.init( aWinContext );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new VedItemM5Model() );
  }

}
