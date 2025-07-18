package org.toxsoft.core.tsgui.ved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.m5.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tsgui.ved.valeds.*;
import org.toxsoft.core.tslib.utils.valobj.*;

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
    TsValobjUtils.registerKeeper( EButtonViselState.KEEPER_ID, EButtonViselState.KEEPER );

    VedViselFactoriesRegistry visFact = new VedViselFactoriesRegistry();
    aAppContext.set( IVedViselFactoriesRegistry.class, visFact );
    visFact.register( ViselLine.FACTORY );
    visFact.register( ViselBezier.FACTORY );
    visFact.register( ViselCubicSpline.FACTORY );
    visFact.register( ViselRectangle.FACTORY );
    visFact.register( ViselEllipse.FACTORY );
    visFact.register( ViselLabel.FACTORY );
    visFact.register( ViselMultiLineLabel.FACTORY );
    visFact.register( ViselRoundRect.FACTORY );
    visFact.register( ViselCircleLamp.FACTORY );
    visFact.register( ViselButton.FACTORY );
    visFact.register( ViselCheckbox.FACTORY );
    visFact.register( ViselLinearGauge.FACTORY );
    visFact.register( ViselImage.FACTORY );
    visFact.register( ViselBaloon.FACTORY );
    visFact.register( ViselGroupbox.FACTORY );
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
    IValedControlFactoriesRegistry vcfRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedVedViselIdSelector.FACTORY );
    vcfRegistry.registerFactory( ValedVedViselAvStringIdSelector.FACTORY );
    vcfRegistry.registerFactory( ValedMultiLineTextEditor.FACTORY );
    vcfRegistry.registerFactory( ValedAvMultiLineTextEditor.FACTORY );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new VedItemM5Model() );
    m5.addModel( new VedViselM5Model() );
    m5.addModel( new VedActorM5Model() );
  }

}
