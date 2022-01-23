package org.toxsoft.tsgui.graphics.colors.impl;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link TsColorManager}
   */
  String FMT_ERR_NO_COLOR_BY_NAME     = "Цвет с именем '%s' не определен";
  String FMT_ERR_INV_RGB_NAME         = "rgb-имя '%s' цвета должен иметь формат \"RRRxGGGxBBB\"";
  String FMT_ERR_INV_RGB_NAME_VALS    = "В rgb-имени '%s' значения R,G,B должны бить в пределах 0..255";
  String FMT_ERR_COLOR_ALREADY_EXISTS = "Под именем '%s' уже зарегистрирован цвет %st";
  String MSG_ERR_INV_RGB_VALS         = "Значения цветовых составляющих R,G,B должны бить в пределах 0..255";

}
