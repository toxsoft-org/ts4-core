package org.toxsoft.core.tsgui.mws.bases_old;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link MwsAbstractAddon}
   */
  String FMT_INFO_ADDON_STARTING = "Addon       %s.init()";
  String FMT_INFO_ADDON_INIT_APP = "Addon       %s.initApp()";
  String FMT_INFO_ADDON_INIT_WIN = "Addon       %s.initWin()";

  /**
   * {@link MwsAbstractProcessor}
   */
  // String FMT_ERR_NO_E4_ELEM = "Не найден %s с идентификтаором '%s' в E4-модели приложения";
  // String FMT_ERR_NO_OSGI_SERVICE = "Не найден OSGI сервс %s";
  String FMT_ERR_NO_E4_ELEM         = "%s with ID '%s' not found in application E4 model";
  String FMT_ERR_AP_NO_OSGI_SERVICE = "OSGI service %s not found";
  /**
   * {@link MwsActivator}
   */
  String FMT_INFO_ACTIVATOR_START   = "Activator   %s: %s.start()";
  String FMT_INFO_ACTIVATOR_STOP    = "Activator   %s: %s.stop()";
  String FMT_ERR_NONEQ_PLUGIN_IDS   = "Activator   %s: Программый ИД (%s) плагина не совпадает с фактическим %s";
  String FMT_ERR_NO_OSGI_SERVICE    = "Activator   %s: не найден OSGI сервис %s";
  String FMT_ERR_NON_SINGLETON      =
      "Activator   %s: не синглтон! в MAINFEST.MF поставьте This plug-in is a singleton";

}
