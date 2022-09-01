package org.toxsoft.core.tsgui.valed.controls.graphics;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  String DLG_T_FONT_SELECT    = Messages.getString( "DLG_T_FONT_SELECT" );    //$NON-NLS-1$
  String STR_MSG_DEFAULT_FONT = Messages.getString( "STR_MSG_DEFAULT_FONT" ); //$NON-NLS-1$

  String DLG_T_COLOR_SELECT    = Messages.getString( "DLG_T_COLOR_SELECT" );    //$NON-NLS-1$
  String STR_MSG_DEFAULT_COLOR = Messages.getString( "STR_MSG_DEFAULT_COLOR" ); //$NON-NLS-1$

  /**
   * {@link ValedTsBorderInfo}
   */
  String DLG_T_BORDER_INFO   = "Параметры границы";
  String STR_MSG_BORDER_INFO = "Задайте требуемые значения и нажмите \"OK\"";

  /**
   * {@link RgbaSelector}
   */
  String STR_L_COLOR_PARAMS = "Параметры цвета";

  /**
   * {@link PanelRgbaSelector}
   */
  String DLG_T_COLOR_INFO   = "Выбор цвета";
  String STR_MSG_COLOR_INFO = "Задайте требуемые значения и нажмите \"OK\"";

  /**
   * {@link PanelColorFillInfo}
   */
  String STR_B_CHOOSE_COLOR = "Выбрать цвет...";

  /**
   * {@link PanelGradientFillInfo}
   */
  String STR_L_GRADIENT_TYPE = "Тип градиента: ";

  /**
   * {@link PanelTsFillInfoSelector}
   */
  String DLG_T_FILL_INFO   = "Параметры заливки";
  String STR_MSG_FILL_INFO = "Задайте требуемые значения и нажмите \"OK\"";
  String STR_L_FILL_TYPE   = "Тип заливки: ";

  /**
   * {@link PanelTsLineInfoEditor}
   */
  String DLG_T_LINE_INFO   = "Параметры линии";
  String STR_MSG_LINE_INFO = "Задайте требуемые значения и нажмите \"OK\"";
  String STR_L_THICKNESS   = "толщина: ";
  String STR_L_LINE_TYPE   = "тип линии: ";
  String STR_L_CAP_TYPE    = "тип окончания: ";
  String STR_L_JOIN_TYPE   = "тип соедиения: ";
}
