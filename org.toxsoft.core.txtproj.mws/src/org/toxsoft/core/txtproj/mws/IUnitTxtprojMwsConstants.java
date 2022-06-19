package org.toxsoft.core.txtproj.mws;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.txtproj.mws.IUnitTxtprojMwsResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.txtproj.lib.impl.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IUnitTxtprojMwsConstants {

  // ------------------------------------------------------------------------------------
  // e4

  String TOOLBARID_TXTPROJ     = "org.toxsoft.core.txtproj.toolbar";             //$NON-NLS-1$
  String MENUID_TXTPROJ        = "org.toxsoft.core.txtproj.menu";                //$NON-NLS-1$
  String CMDCATEGID_TXTPROJ    = "org.toxsoft.core.txtproj.cmdcateg";            //$NON-NLS-1$
  String CMDID_PROJECT_NEW     = "org.toxsoft.core.txtproj.cmd.project_new";     //$NON-NLS-1$
  String CMDID_PROJECT_OPEN    = "org.toxsoft.core.txtproj.cmd.project_open";    //$NON-NLS-1$
  String CMDID_PROJECT_SAVE    = "org.toxsoft.core.txtproj.cmd.project_save";    //$NON-NLS-1$
  String CMDID_PROJECT_SAVE_AS = "org.toxsoft.core.txtproj.cmd.project_save_as"; //$NON-NLS-1$
  String BTNID_PROJECT_NEW     = "org.toxsoft.core.txtproj.btn.project_new";     //$NON-NLS-1$
  String BTNID_PROJECT_OPEN    = "org.toxsoft.core.txtproj.btn.project_open";    //$NON-NLS-1$
  String BTNID_PROJECT_SAVE    = "org.toxsoft.core.txtproj.btn.project_save";    //$NON-NLS-1$
  String BTNID_PROJECT_SAVE_AS = "org.toxsoft.core.txtproj.btn.project_save_as"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons
  String PREFIX_OF_ICON_FIELD_NAME = "ICON_"; //$NON-NLS-1$
  // String ICON_WELCOME_01 = "welcome-01"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Command line args
  // Command line may be specified in application starting script or at manual start

  /**
   * Command line argument to specify path to the project file to be loaded at application startup.<br>
   * Argument follows simple command line rules as described in {@link ProgramArgs}.
   */
  String CMDLINE_ARG_PROJ_PATH = "tsproject"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Unit config
  // Unit config option values must be set as MWS context parameters in application EXE plugin's Activator.doStart()

  /**
   * Default project file format info.
   */
  TsProjectFileFormatInfo DEFAULT_PROJECT_FILE_FORMAT_INFO =
      new TsProjectFileFormatInfo( "org.toxsoft.tsproject.generic", 1 ); //$NON-NLS-1$

  /**
   * Default project file format info as atomic value.
   */
  IAtomicValue DEFAULT_PROJECT_FILE_FORMAT_INFO_AV = AvUtils.avValobj( DEFAULT_PROJECT_FILE_FORMAT_INFO, //
      TsProjectFileFormatInfoKeeper.KEEPER, TsProjectFileFormatInfoKeeper.KEEPER_ID );

  /**
   * Project file format info. <br>
   * Default value: {@link #DEFAULT_PROJECT_FILE_FORMAT_INFO}<br>
   * This is option for MWS context {@link IMwsOsgiService#mwsContext()} parameters.
   */
  IDataDef OPDEF_PROJECT_FILE_FORMAT_INFO = DataDef.create( "org.toxsoft.unit.txtproj.ProFileFormatInfo", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_PROJECT_FILE_FORMAT_INFO, //
      TSID_DESCRIPTION, STR_D_PROJECT_FILE_FORMAT_INFO, //
      TSID_DEFAULT_VALUE, DEFAULT_PROJECT_FILE_FORMAT_INFO_AV, //
      TSID_KEEPER_ID, TsProjectFileFormatInfoKeeper.KEEPER_ID //
  );

  /**
   * Show project commands (new, open, save, save as) in application main menu.<br>
   * Default value: <code>true</code><br>
   * This is option for MWS context {@link IMwsOsgiService#mwsContext()} parameters.
   */
  IDataDef OPDEF_SHOW_CMD_IN_MENU = DataDef.create( "org.toxsoft.unit.txtproj.ShowCmdInMenu", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_SHOW_CMD_IN_MENU, //
      TSID_DESCRIPTION, STR_D_SHOW_CMD_IN_MENU, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Show project commands (new, open, save, save as) in toolbar {@link #TOOLBARID_TXTPROJ}.<br>
   * Default value: <code>true</code><br>
   * This is option for MWS context {@link IMwsOsgiService#mwsContext()} parameters.
   */
  IDataDef OPDEF_SHOW_CMD_IN_TOOLBAR = DataDef.create( "org.toxsoft.unit.txtproj.ShowCmdInToolbar", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_SHOW_CMD_IN_TOOLBAR, //
      TSID_DESCRIPTION, STR_D_SHOW_CMD_IN_TOOLBAR, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Place project commands in "File" menu, not in separate menu {@link #MENUID_TXTPROJ}.<br>
   * Default value: <code>true</code><br>
   * This is option for MWS context {@link IMwsOsgiService#mwsContext()} parameters.
   */
  IDataDef OPDEF_ALWAYS_USE_FILE_MENU = DataDef.create( "org.toxsoft.unit.txtproj.AlwaysUseFileMenu", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_ALWAYS_USE_FILE_MENU, //
      TSID_DESCRIPTION, STR_D_ALWAYS_USE_FILE_MENU, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Load project from file (if specified) when plugin intes, not after all plugins initialization. <br>
   * Default value: <code>false</code><br>
   * This is option for MWS context {@link IMwsOsgiService#mwsContext()} parameters.
   */
  IDataDef OPDEF_IMMEDIATE_LOAD_PROJ = DataDef.create( "org.toxsoft.unit.txtproj.ImmediateLoadProject", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IMMEDIATE_LOAD_PROJ, //
      TSID_DESCRIPTION, STR_D_IMMEDIATE_LOAD_PROJ, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Determines if windows title will containt loaded project file and application names. <br>
   * Default value: <code>true</code><br>
   * This is option for MWS context {@link IMwsOsgiService#mwsContext()} parameters.
   */
  IDataDef OPDEF_IS_WINDOWS_TITLE_BOUND = DataDef.create( "org.toxsoft.unit.txtproj.IsWindowsTitleBound", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_WINDOWS_TITLE_BOUND, //
      TSID_DESCRIPTION, STR_D_IS_WINDOWS_TITLE_BOUND, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Determines if toolbar will contain reduced number of buttons (only Open and Save). <br>
   * Default value: <code>true</code><br>
   * This is option for MWS context {@link IMwsOsgiService#mwsContext()} parameters.
   */
  IDataDef OPDEF_IS_REDUCED_TOOLBAR_BUTTONS =
      DataDef.create( "org.toxsoft.unit.txtproj.IsReducedToolbarButtons", BOOLEAN, //$NON-NLS-1$
          TSID_NAME, STR_N_IS_REDUCED_TOOLBAR_BUTTONS, //
          TSID_DESCRIPTION, STR_D_IS_REDUCED_TOOLBAR_BUTTONS, //
          TSID_DEFAULT_VALUE, AV_TRUE //
      );

  static void init( IEclipseContext aWinContext ) {
    // icons
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IUnitTxtprojMwsConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
