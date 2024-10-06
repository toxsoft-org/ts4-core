package org.toxsoft.core.tsgui.mws.l10n;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsguiMwsSharedResources {

  String STR_DEF_APP_PREFS_BUNDLE   = Messages.getString( "STR_DEF_APP_PREFS_BUNDLE" );   //$NON-NLS-1$
  String STR_DEF_APP_PREFS_BUNDLE_D = Messages.getString( "STR_DEF_APP_PREFS_BUNDLE_D" ); //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Log messages are not localized
  //

  String FMT_LOG_INFO_APP_MAIN_ADDON_STARTING = "Addon       %s.init()";                  //$NON-NLS-1$
  String FMT_LOG_INFO_APP_MAIN_ADDON_INIT_APP = "Addon       %s.initApp()";               //$NON-NLS-1$
  String FMT_LOG_INFO_APP_MAIN_ADDON_INIT_WIN = "Addon       %s.initWin()";               //$NON-NLS-1$
  String FMT_LOG_WARN_INV_APP_INFO_FILE       = "Invalid appication information file %s"; //$NON-NLS-1$

}
