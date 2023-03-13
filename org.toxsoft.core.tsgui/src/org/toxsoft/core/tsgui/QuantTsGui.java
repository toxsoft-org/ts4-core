package org.toxsoft.core.tsgui;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.colors.impl.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.quants.progargs.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.mws.services.timers.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * Library initialization quant.
 *
 * @author hazard157
 */
public class QuantTsGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantTsGui() {
    super( QuantTsGui.class.getSimpleName() );
    TsGuiUtils.initializeTsGuiCore();
    registerQuant( new QuantProgramArgs() );
    registerQuant( new QuantM5() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    aAppContext.set( ITsHdpiService.class, new TsHdpiService( aAppContext ) );
    //
    ValedControlFactoriesRegistry registry = new ValedControlFactoriesRegistry();
    aAppContext.set( IValedControlFactoriesRegistry.class, registry );
    aAppContext.set( ValedControlFactoriesRegistry.class, registry );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    Display display = aWinContext.get( Display.class );
    aWinContext.set( ITsIconManager.class, new TsIconManager( aWinContext ) );
    ITsImageManager imageManager = new TsImageManager( aWinContext );
    aWinContext.set( ITsImageManager.class, imageManager );
    ITsGuiContext ctx1 = new TsGuiContext( aWinContext );
    // HERE may set up timer service periods
    ITsGuiTimersService timerService = new TsGuiTimersService( ctx1 );
    aWinContext.set( ITsGuiTimersService.class, timerService );
    aWinContext.set( ITsColorManager.class, new TsColorManager( display ) );
    aWinContext.set( ITsFontManager.class, new TsFontManager( display ) );
    aWinContext.set( ITsCursorManager.class, new TsCursorManager( aWinContext ) );
    IAnimationSupport as = new AnimationSupport( display );
    aWinContext.set( IAnimationSupport.class, as );
    as.resume();
  }

  @Override
  protected void doCloseWin( MWindow aWindow ) {
    IEclipseContext winContext = aWindow.getContext();
    ITsImageManager imageManager = winContext.get( ITsImageManager.class );
    if( imageManager != null ) {
      imageManager.clearCache();
      imageManager = null;
    }
    TsGuiTimersService timerService = (TsGuiTimersService)winContext.get( ITsGuiTimersService.class );
    if( timerService != null ) {
      timerService.close();
      timerService = null;
    }
  }

  @Override
  protected void doClose() {
    // nop
  }

}
