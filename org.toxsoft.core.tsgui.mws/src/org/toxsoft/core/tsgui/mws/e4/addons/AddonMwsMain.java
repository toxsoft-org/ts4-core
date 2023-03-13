package org.toxsoft.core.tsgui.mws.e4.addons;

import static org.toxsoft.core.tsgui.mws.e4.addons.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import javax.annotation.*;
import javax.inject.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Main addon of MWS based application.
 * <p>
 * This is very firs addon executed among all other addons of plugins making MWS application.
 *
 * @author hazard157
 */
public class AddonMwsMain {

  IMainWindowLifeCylceListener windowsInterceptor = new IMainWindowLifeCylceListener() {

    @Override
    final public void beforeMainWindowOpen( IEclipseContext aWinContext, MWindow aWindow ) {
      try {
        LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_INIT_WIN, nameForLog );
        ITsE4Helper e4Helper = new TsE4Helper( aWinContext );
        aWinContext.set( ITsE4Helper.class, e4Helper );
        // init quants
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

  };

  @Inject
  IMwsOsgiService mwsService;

  private final String nameForLog;
  private final IQuant quantManager;

  /**
   * Constructor.
   */
  public AddonMwsMain() {
    nameForLog = this.getClass().getSimpleName();
    quantManager = new QuantBase( super.getClass().getName() );
  }

  @PostConstruct
  final void init( IEclipseContext aAppContext ) {
    LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_STARTING, nameForLog );
    try {
      initAppPrefs( aAppContext );
      quantManager.registerQuant( new QuantTsGui() );
      quantManager.initApp( aAppContext );
      LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_INIT_APP, nameForLog );
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
    IPrefBundle defBundle = appPrefs.defineBundle( mwsService.appInfo().id(), OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_DEF_APP_PREFS_BUNDLE, //
        TSID_DESCRIPTION, STR_D_DEF_APP_PREFS_BUNDLE//
    ) );
    aAppContext.set( IPrefBundle.class, defBundle );
  }

}
