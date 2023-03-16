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
  String FMT_INFO_ADDON_STARTING = "Addon       %s.init()";    //$NON-NLS-1$
  String FMT_INFO_ADDON_INIT_APP = "Addon       %s.initApp()"; //$NON-NLS-1$
  String FMT_INFO_ADDON_INIT_WIN = "Addon       %s.initWin()"; //$NON-NLS-1$

  /**
   * {@link MwsAbstractProcessor}
   */
  String FMT_ERR_NO_E4_ELEM         = "%s with ID '%s' not found in application E4 model";
  String FMT_ERR_AP_NO_OSGI_SERVICE = "OSGI service %s not found";

  /**
   * {@link MwsActivator}
   */
  String FMT_INFO_ACTIVATOR_START = "Activator   %s: %s.start()"; //$NON-NLS-1$
  String FMT_INFO_ACTIVATOR_STOP  = "Activator   %s: %s.stop()";  //$NON-NLS-1$

  String FMT_ERR_NONEQ_PLUGIN_IDS = "Activator   %s: Программый ИД (%s) плагина не совпадает с фактическим %s";
  String FMT_ERR_NO_OSGI_SERVICE  = "Activator   %s: не найден OSGI сервис %s";
  String FMT_ERR_NON_SINGLETON    = "Activator   %s: не синглтон! в MAINFEST.MF поставьте This plug-in is a singleton";

}
