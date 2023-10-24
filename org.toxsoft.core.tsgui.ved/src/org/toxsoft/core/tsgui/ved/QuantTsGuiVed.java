package org.toxsoft.core.tsgui.ved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
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
    visFact.register( ViselLabel.FACTORY );
    visFact.register( ViselRoundRect.FACTORY );
    visFact.register( ViselCircleLamp.FACTORY );
    //
    VedActorFactoriesRegistry actFact = new VedActorFactoriesRegistry();
    aAppContext.set( IVedActorFactoriesRegistry.class, actFact );

  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ITsguiVedConstants.init( aWinContext );
    //
    ITsHdpiService hdpiService = aWinContext.get( ITsHdpiService.class );
    hdpiService.defineIconCategory( VED_EDITOR_PALETTE_ICON_SIZE_CATEGORY, VED_EDITOR_PALETTE_ICON_SIZE_SCALE );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new VedItemM5Model() );
  }

}
