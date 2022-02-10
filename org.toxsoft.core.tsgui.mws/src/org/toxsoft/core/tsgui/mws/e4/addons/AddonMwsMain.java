package org.toxsoft.core.tsgui.mws.e4.addons;

import static org.toxsoft.core.tsgui.mws.e4.addons.ITsResources.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.toxsoft.core.tsgui.QuantTsGui;
import org.toxsoft.core.tsgui.bricks.quant.IQuant;
import org.toxsoft.core.tsgui.bricks.quant.QuantBase;
import org.toxsoft.core.tsgui.mws.bases.IMainWindowLifeCylceListener;
import org.toxsoft.core.tsgui.mws.bases.MwsMainWindowStaff;
import org.toxsoft.core.tsgui.mws.osgi.IMwsOsgiService;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.tsgui.mws.services.e4helper.TsE4Helper;
import org.toxsoft.core.tslib.bricks.apprefs.IAppPreferences;
import org.toxsoft.core.tslib.bricks.apprefs.IPrefBundle;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * Main addon of MWS based application.
 * <p>
 * This is very firs addon executed among all other addons of plugins making MWS application.
 *
 * @author hazard157
 */
public class AddonMwsMain
    implements IMainWindowLifeCylceListener {

  @Inject
  IMwsOsgiService mwsService;

  private final String nameForLog;
  private final IQuant quantManager;

  /**
   * Constructor.
   */
  public AddonMwsMain() {
    nameForLog = this.getClass().getSimpleName();
    quantManager = new QuantBase( "BuiltinQuant." + AddonMwsMain.class.getSimpleName() ); //$NON-NLS-1$
  }

  @PostConstruct
  final void init( IEclipseContext aAppContext ) {
    LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_STARTING, nameForLog );
    try {
      initAppPrefs( aAppContext );
      // prepare MwsMainWindowStaff for MwsAbstractPart
      MwsMainWindowStaff mainWindowStaff = new MwsMainWindowStaff( this );
      aAppContext.set( MwsMainWindowStaff.class, mainWindowStaff );
      // HERE registration and starting builtin quants
      quantManager.registerQuant( new QuantTsGui() );
      // HERE registration and starting user-specified quants
      for( IQuant q : mwsService.listQuants() ) {
        quantManager.registerQuant( q );
      }
      quantManager.initApp( aAppContext );
      LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_INIT_APP, nameForLog );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // IMainWindowLifeCylceListener
  //

  @Override
  final public void beforeMainWindowOpen( IEclipseContext aWinContext, MWindow aWindow ) {
    try {
      LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_INIT_WIN, nameForLog );
      ITsE4Helper e4Helper = new TsE4Helper( aWinContext );
      aWinContext.set( ITsE4Helper.class, e4Helper );
      quantManager.initWin( aWinContext );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  @Override
  final public boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
    try {
      return quantManager.canCloseMainWindow( aWinContext, aWindow );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      return true;
    }
  }

  @Override
  public void beforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
    try {
      quantManager.close();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void initAppPrefs( IEclipseContext aAppContext ) {
    // initialize app preferences service
    IAppPreferences appPrefs = aAppContext.get( IAppPreferences.class );
    if( appPrefs == null ) {
      AbstractAppPreferencesStorage storage = mwsService.context().find( AbstractAppPreferencesStorage.class );
      if( storage == null ) {
        String prefsNodeName = "/" + mwsService.appInfo().id().replace( '.', '/' ); //$NON-NLS-1$
        storage = new AppPreferencesOsRegistryStorage( prefsNodeName );
      }
      appPrefs = new AppPreferences( storage );
    }
    aAppContext.set( IAppPreferences.class, appPrefs );
    // create default prefs bundle
    IPrefBundle defBundle =
        appPrefs.defineBundle( mwsService.appInfo().id(), STR_N_DEF_APP_PREFS_BUNDLE, STR_D_DEF_APP_PREFS_BUNDLE );
    aAppContext.set( IPrefBundle.class, defBundle );
  }

}
