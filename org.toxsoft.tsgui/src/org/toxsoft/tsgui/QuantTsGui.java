package org.toxsoft.tsgui;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.tsgui.graphics.colors.ITsColorManager;
import org.toxsoft.tsgui.graphics.colors.impl.TsColorManager;
import org.toxsoft.tsgui.graphics.fonts.ITsFontManager;
import org.toxsoft.tsgui.graphics.fonts.impl.TsFontManager;
import org.toxsoft.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.tsgui.graphics.icons.impl.TsIconManager;
import org.toxsoft.tsgui.graphics.image.ITsImageManager;
import org.toxsoft.tsgui.graphics.image.impl.TsImageManager;
import org.toxsoft.tsgui.m5.QuantM5;
import org.toxsoft.tsgui.mws.quants.progargs.QuantProgramArgs;
import org.toxsoft.tsgui.mws.services.hdpi.ITsHdpiService;
import org.toxsoft.tsgui.mws.services.hdpi.TsHdpiService;
import org.toxsoft.tsgui.utils.TsGuiUtils;
import org.toxsoft.tsgui.utils.anim.AnimationSupport;
import org.toxsoft.tsgui.utils.anim.IAnimationSupport;
import org.toxsoft.tsgui.valed.QuantValed;

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
