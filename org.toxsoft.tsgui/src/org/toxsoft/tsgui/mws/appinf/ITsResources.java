package org.toxsoft.tsgui.mws.appinf;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link TsApplicationInfo}
   */
  String DEFAULT_TSAPP_NAME            = "<<название приложения>>";
  String DEFAULT_TSAPP_DESCRIPTION     = "<<краткое описание приложения>>";
  String FMT_WARN_SHORT_DEPLAPP_ID     = "Идентификатор приложения '%s' - рекомендуется формат 'com.company.appname'";
  String FMT_ERR_SHORT_ALIAS           = "Слишком короткий (длина меньше %d) псевдоним '%s'";
  String FMT_ERR_LONG_ALIAS            = "Слишком длинный (длина более %d) псевдоним '%s'";
  String FMR_WARN_ALIAS_HAS_UPPERCASE  = "Рекомендуем в псевдониме '%s' использовать только прописные буквы";
  String FMR_WARN_ALIAS_HAS_UNDERSCORE = "Рекомендуем в псевдониме '%s' не использовать символ подчеркивания";

}
