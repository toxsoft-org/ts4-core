package org.toxsoft.core.tsgui.mws.e4.addons;

import static org.toxsoft.core.tsgui.mws.e4.addons.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import javax.annotation.*;
import javax.inject.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Main addon of MWS based application.
 * <p>
 * This is very firs addon executed among all other addons of plugins making MWS application.
 *
 * @author hazard157
 */
public class AddonMwsMain {

  @Inject
  IMwsOsgiService mwsService;

  private final String    nameForLog;
  private MApplication    e4App      = null;
  private IEclipseContext appContext = null;

  /**
   * Constructor.
   */
  public AddonMwsMain() {
    nameForLog = this.getClass().getSimpleName();
  }

  @PostConstruct
  final void init( MApplication aApplication ) {
    LoggerUtils.defaultLogger().info( FMT_INFO_APP_MAIN_ADDON_STARTING, nameForLog );
    try {
      TsNullArgumentRtException.checkNull( aApplication );
      e4App = aApplication;
      appContext = aApplication.getContext();
      TsInternalErrorRtException.checkNull( appContext );
      initAppPrefs( appContext );
      MwaApplicationStaff appStaff = new MwaApplicationStaff( e4App );
      appContext.set( MwaApplicationStaff.class, appStaff );
      // FIXME appStaff. init APP() ???
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
