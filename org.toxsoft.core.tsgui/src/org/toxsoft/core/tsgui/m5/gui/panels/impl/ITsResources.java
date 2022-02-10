package org.toxsoft.core.tsgui.m5.gui.panels.impl;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link M5DefaultEntityFilterPanel}
   */
  String TXT_M_TEXT = "Enter text to search";
  String TXT_P_TEXT = "Filtering criteria text string";

  /**
   * {@link M5EntityPanelWithValeds}
   */
  String FMT_ERR_NO_FACTORY_IN_PARAMS  = "Поле %s: в параметрах не задан класс фабрики редактора";
  String FMT_CANT_SET_FIELD_VALUE      = "Поле %s: при присвоении занчения возникла ошибка %s";
  String FMT_ERR_FIELD_VALIDATION_FAIL = "Поле %s: %s";

}
