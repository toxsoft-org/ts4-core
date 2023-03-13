package org.toxsoft.core.tsgui.mws.e4.addons;

import static org.toxsoft.core.tsgui.mws.e4.addons.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.*;

import javax.annotation.*;
import javax.inject.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.services.events.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.*;
import org.osgi.service.event.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.utils.*;
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
      // subscribe to windows event to handle windows lifecycle
      IEventBroker eventBroker = appContext.get( IEventBroker.class );
      eventBroker.subscribe( UIEvents.Context.TOPIC_ALL, windowsContextChangeEventHandler );
      // initialize MWS
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

  private final EventHandler windowsContextChangeEventHandler = aEvent -> {
    if( hasPropWithValue( aEvent, "AttName", "context" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
      if( hasPropWithValue( aEvent, "EventType", "SET" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
        Object rawMainWin = aEvent.getProperty( "ChangedElement" ); //$NON-NLS-1$
        MTrimmedWindow mainWin = MTrimmedWindow.class.cast( rawMainWin );

        IEclipseContext winContext = IEclipseContext.class.cast( aEvent.getProperty( "NewValue" ) ); //$NON-NLS-1$
        MwaWindowStaff winStaff;
        if( winContext != null ) { // windows creation and opening
          winStaff = new MwaWindowStaff( mainWin );
        }
        else { // window closing
          IEclipseContext winContext = aEventIEclipseContext.class.cast( aEvent.getProperty( "NewValue" ) ); //$NON-NLS-1$

        }

        // TODO AddonMwsMain.subscribeToWindowsLifecycleOsgiEvents()

      }
    }
  };

  /**
   * Determines if event is trimmed window context initialization.
   * <p>
   * This event is used to determine if new application window is opening.
   *
   * @param aEvent {@link Event} - the event
   * @return {@link MTrimmedWindow} - the window or <code>null</code> if this is not window init event
   */
  private static MTrimmedWindow checkMainWindowContextSetEvent( Event aEvent ) {
    if( hasPropWithValue( aEvent, "AttName", "context" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
      if( hasPropWithValue( aEvent, "EventType", "SET" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
        Object rawMainWin = aEvent.getProperty( "ChangedElement" ); //$NON-NLS-1$
        IEclipseContext winContext = IEclipseContext.class.cast( aEvent.getProperty( "NewValue" ) ); //$NON-NLS-1$
        MTrimmedWindow mainWin = MTrimmedWindow.class.cast( rawMainWin );

        // DEBUG
        if( winContext != null ) { // winContext == null happens when closing window
          mainWin = winContext.get( MTrimmedWindow.class );
          TsTestUtils.pl( "---------------------------" );
          TsTestUtils.pl( "MWindow from context = %s", mainWin );
          TsTestUtils.pl( "---------------------------" );
        }

        return mainWin;
      }
    }
    return null;
  }

  private static boolean hasPropWithValue( Event aEvent, String aPropName, String aPropValue ) {
    if( aEvent.containsProperty( aPropName ) ) {
      String propStr = Objects.toString( aEvent.getProperty( aPropName ) );
      return propStr.startsWith( aPropValue );
    }
    return false;
  }

  /**
   * Initializes {@link IAppPreferences} and puts it in the context.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
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
