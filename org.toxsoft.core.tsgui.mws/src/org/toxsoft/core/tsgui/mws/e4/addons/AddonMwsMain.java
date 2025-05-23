package org.toxsoft.core.tsgui.mws.e4.addons;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.mws.l10n.ITsguiMwsSharedResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.*;

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
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import jakarta.annotation.*;
import jakarta.inject.*;

/**
 * Main addon of MWS based application.
 * <p>
 * This is very first addon executed among all other addons of plugins making MWS application.
 * <p>
 * The MWS initialization and working sequence:
 * <ul>
 * <li>{@link AddonMwsMain#init(MApplication)} is called:</li>
 * <ul>
 * <li>subscribes to the {@link MTrimmedWindow} creation events;</li>
 * <li>initializes {@link IAppPreferences} storage;</li>
 * <li>TODO</li>
 * <li>xxx;</li>
 * <li>xxx;</li>
 * <li>zzz.</li>
 * </ul>
 * <li>xxx;</li>
 * <li>zzz.</li>
 * </ul>
 *
 * @author hazard157
 */
public class AddonMwsMain {

  @Inject
  IMwsOsgiService mwsService;

  private final String nameForLog;

  private IApplicationWideQuantManager appWideQuantManager = null;

  /**
   * Constructor.
   */
  public AddonMwsMain() {
    nameForLog = this.getClass().getSimpleName();
  }

  @PostConstruct
  final void init( MApplication aApplication ) {
    LoggerUtils.defaultLogger().info( FMT_LOG_INFO_APP_MAIN_ADDON_STARTING, nameForLog );
    try {
      TsNullArgumentRtException.checkNull( aApplication );
      IEclipseContext appContext = aApplication.getContext();
      TsInternalErrorRtException.checkNull( appContext );
      // subscribe to windows event to handle windows lifecycle
      IEventBroker eventBroker = appContext.get( IEventBroker.class );
      eventBroker.subscribe( UIEvents.Context.TOPIC_ALL, windowsContextChangeEventHandler );
      // initialize preferences storage
      initAppPrefs( appContext );
      // initialize application-wide quants registry
      IMwsOsgiService mwsOsgiService = appContext.get( IMwsOsgiService.class );
      appWideQuantManager = mwsOsgiService.context().get( IApplicationWideQuantManager.class );
      appContext.set( IApplicationWideQuantManager.class, appWideQuantManager );
      appWideQuantManager.initApp( appContext );
      LoggerUtils.defaultLogger().info( FMT_LOG_INFO_APP_MAIN_ADDON_INIT_APP, nameForLog );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  @PreDestroy
  final void close() {
    if( appWideQuantManager != null ) {
      appWideQuantManager.close();
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private final EventHandler windowsContextChangeEventHandler = aEvent -> {
    // check it is context set event (either to the new instance on opening or to null on closing window)
    if( hasPropWithValue( aEvent, "AttName", "context" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
      if( hasPropWithValue( aEvent, "EventType", "SET" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
        Object changedElem = aEvent.getProperty( "ChangedElement" ); //$NON-NLS-1$
        // check that we're handling windows (not part, perspective, etc) context event
        if( changedElem instanceof MTrimmedWindow mainWin ) {
          boolean isWindowsOpenEvent = aEvent.containsProperty( "NewValue" ); //$NON-NLS-1$
          if( isWindowsOpenEvent ) {
            @SuppressWarnings( "unused" )
            MwsWindowStaff winStaff = new MwsWindowStaff( mainWin );
          }
        }
      }
    }
  };

  /**
   * Checks that specified {@link String} attribute of the event has the specified value.
   *
   * @param aEvent {@link Event} - the event
   * @param aPropName String - the property name
   * @param aPropValue String - the expected value of the property
   * @return boolean - <code>true</code> if property exists and has the specified value
   */
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
    // create default preferences bundle
    IPrefBundle defBundle = appPrefs.defineBundle( mwsService.appInfo().id(), OptionSetUtils.createOpSet( //
        TSID_NAME, STR_DEF_APP_PREFS_BUNDLE, //
        TSID_DESCRIPTION, STR_DEF_APP_PREFS_BUNDLE_D, //
        TSID_ICON_ID, ICONID_TSAPP_WINDOWS_ICON //
    ) );
    aAppContext.set( IPrefBundle.class, defBundle );
  }

}
