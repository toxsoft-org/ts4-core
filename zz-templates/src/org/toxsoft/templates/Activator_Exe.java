package org.toxsoft.templates;

import static org.toxsoft.templates.l10n.ITsXxxYyySharedResources.*;

import java.io.*;
import java.time.*;

import org.eclipse.osgi.service.environment.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.progargs.*;

/**
 * The activator template for the EXE plugin (plugin that launches the MWS application).
 * <p>
 * <b>Important</b>: after copy-pasting this file from the templates project it is mandatory to replace value of the
 * {@link #PLUGIN_ID} constant with the ID of the destination project.
 *
 * @author hazard157
 */
public class Activator_Exe
    extends MwsActivator {

  // ------------------------------------------------------------------------------------
  // Application info

  /**
   * Full ID of the application (an IDpath).
   */
  public static final String APP_ID = "org.toxsoft.templates"; //$NON-NLS-1$

  /**
   * Application alias is an IDname usually matching application *.product file name.
   */
  public static final String APP_ALIAS = "xxx"; //$NON-NLS-1$

  /**
   * The application version (not a version of the plugin).
   */
  public static final TsVersion APP_VERSION = new TsVersion( 32, 0, 2023, Month.JULY, 19 );

  /**
   * Application information displayed in the dialog invoked by Help/About menu.
   */
  public static final ITsApplicationInfo APP_INFO =
      new TsApplicationInfo( APP_ID, STR_APP_INFO, STR_APP_INFO_D, APP_ALIAS, APP_VERSION );

  /**
   * The plugin ID (for Java static imports).
   */
  public static final String PLUGIN_ID = "com.hazard157.psx33.exe"; //$NON-NLS-1$

  /**
   * Command line argument with configuration file name.
   */
  public static final String CMDLINE_ARG_CFG_FILE_NAME = "config"; //$NON-NLS-1$

  /**
   * Default configuration file name (located in the startup directory).
   */
  public static final String DEFAULT_CFG_FILE_NAME = APP_ALIAS + ".cfg"; //$NON-NLS-1$

  private static Activator_Exe instance = null;

  /**
   * Constructor.
   */
  public Activator_Exe() {
    super( PLUGIN_ID );
    checkInstance( instance );
    instance = this;
  }

  @Override
  protected void doStart() {
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    mws.setAppInfo( APP_INFO );
    // application preferences will be stored in the config file
    EnvironmentInfo envInfo = getOsgiService( EnvironmentInfo.class );
    ProgramArgs pa = new ProgramArgs( envInfo.getCommandLineArgs() );
    String cfgFileName = pa.getArgValue( CMDLINE_ARG_CFG_FILE_NAME, DEFAULT_CFG_FILE_NAME );
    File cfgFile = new File( cfgFileName );
    AbstractAppPreferencesStorage apStorage = new AppPreferencesConfigIniStorage( cfgFile );
    mws.context().put( AbstractAppPreferencesStorage.class, apStorage );
  }

  /**
   * Returns the reference to the activator singleton.
   *
   * @return {@link Activator_Exe} - the activator singleton
   */
  public static Activator_Exe getInstance() {
    return instance;
  }

}
