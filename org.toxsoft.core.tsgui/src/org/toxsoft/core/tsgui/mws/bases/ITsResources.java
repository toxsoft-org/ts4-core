package org.toxsoft.core.tsgui.mws.bases;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link MwsAbstractAddon}
   */
  String FMT_INFO_ADDON_STARTING  = "Addon       %s.init()";                    //$NON-NLS-1$
  String FMT_INFO_ADDON_INIT_APP  = "Addon       %s.initApp()";                 //$NON-NLS-1$
  String FMT_INFO_ADDON_INIT_WIN  = "Addon       %s.initWin()";                 //$NON-NLS-1$
  String FMT_INFO_ADDON_CLOSE_WIN = "Addon       %s.doBeforeMainWindowClose()"; //$NON-NLS-1$

  /**
   * {@link MwsAbstractProcessor}
   */
  String FMT_ERR_NO_E4_ELEM         = Messages.getString( "FMT_ERR_NO_E4_ELEM" );         //$NON-NLS-1$
  String FMT_ERR_AP_NO_OSGI_SERVICE = Messages.getString( "FMT_ERR_AP_NO_OSGI_SERVICE" ); //$NON-NLS-1$

  /**
   * {@link MwsActivator}
   */
  String FMT_INFO_ACTIVATOR_START = "Activator   %s: %s.start()";                     //$NON-NLS-1$
  String FMT_INFO_ACTIVATOR_STOP  = "Activator   %s: %s.stop()";                      //$NON-NLS-1$
  String FMT_ERR_NONEQ_PLUGIN_IDS = Messages.getString( "FMT_ERR_NONEQ_PLUGIN_IDS" ); //$NON-NLS-1$
  String FMT_ERR_NO_OSGI_SERVICE  = Messages.getString( "FMT_ERR_NO_OSGI_SERVICE" );  //$NON-NLS-1$
  String FMT_ERR_NON_SINGLETON    = Messages.getString( "FMT_ERR_NON_SINGLETON" );    //$NON-NLS-1$

  /**
   * {@link MwsWindowStaff}
   */
  String FMT_INFO_WIN_STAFF_INIT    = "WinStaff    %s: created"; //$NON-NLS-1$
  String FMT_INFO_WIN_STAFF_CLOSING = "WinStaff    %s: closing"; //$NON-NLS-1$

}
