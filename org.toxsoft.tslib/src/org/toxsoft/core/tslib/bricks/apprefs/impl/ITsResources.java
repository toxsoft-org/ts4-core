package org.toxsoft.core.tslib.bricks.apprefs.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link AppPreferencesOsRegistryStorage}
   */
  String FMT_ERR_NON_ROOT_PREFS_PATH = "Путь (\"%s\") к узлу настроек в реестре должен начинаться с симовла '/'";
  // String FMT_ERR_INV_OPTION_CLASS = "Тип %s опции %s не соответствует ожидаемому типу %s";
  // String FMT_ERR_INV_OPTION_ATOMIC_TYPE = "Атомарный тип %s опции %s не соответствует ожидаемому типу %s";

  /**
   * AppPreferencesConfigIniStorage
   */
  String FMT_ERR_NONEMPTY_AFTER_SECTNAME = "После названия [%s] в строке ничего не должно быть";
  String FMT_ERR_NONEMPTY_AFTER_VALUE    = "После значения %s в строке ничего не должно быть";

}
