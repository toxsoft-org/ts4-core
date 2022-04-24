package org.toxsoft.core.tsgui;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.colors.impl.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.quants.progargs.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.valed.*;

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
    registerQuant( new QuantValed() );
    registerQuant( new QuantM5() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // FIXME IAppGuiSettingsService agss = new AppGuiSettingsService( aAppContext );
    // aAppContext.set( IAppGuiSettingsService.class, agss );
    aAppContext.set( ITsHdpiService.class, new TsHdpiService( aAppContext ) );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    Display display = aWinContext.get( Display.class );
    aWinContext.set( ITsIconManager.class, new TsIconManager( aWinContext ) );
    aWinContext.set( ITsImageManager.class, new TsImageManager( aWinContext ) );
    aWinContext.set( ITsColorManager.class, new TsColorManager( display ) );
    aWinContext.set( ITsFontManager.class, new TsFontManager( display ) );
    IAnimationSupport as = new AnimationSupport( display );
    aWinContext.set( IAnimationSupport.class, as );
    as.resume();
  }

}
