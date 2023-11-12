package org.toxsoft.core.tsgui.valed.controls.graphics;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
// TODO долокализовать строки
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

  /**
   * {@link PanelTsImageDescriptorEditor}
   */
  String DLG_T_IMAGE_DESCRIPTOR   = "Параметры дескриптора изображения";
  String STR_MSG_IMAGE_DESCRIPTOR = "Задайте требуемые значения и нажмите \"OK\"";
  String STR_L_IMG_SOURCE_KIND    = "Источник: ";
  String STR_IMG_SOURCE_KIND      = Messages.getString( "STR_IMG_SOURCE_KIND" );   //$NON-NLS-1$
  String STR_IMG_SOURCE_KIND_D    = Messages.getString( "STR_IMG_SOURCE_KIND_D" ); //$NON-NLS-1$
  String STR_B_IMG_DESCR_EDIT     = "Изменить...";

  /**
   * {@link PanelTsImageFillInfo}
   */
  String DLG_T_IMAGE_FILL_INFO   = "Параметры заливки изображением";
  String STR_MSG_IMAGE_FILL_INFO = "Задайте требуемые значения и нажмите \"OK\"";
  String STR_L_FILL_IMAGE_KIND   = "Тип заливки изображением: ";
  String STR_L_IMAGE_SIZE        = "Размер: ";
  String STR_L_IMAGE_DESCRIPTION = "Описание: ";
  String STR_G_PREVIEW           = "Предпросмотр";

  /**
   * {@link ValedD2Angle}
   */
  String DLG_T_ANGLE_VALUE = "Величина угла";
  String DLG_T_ANGLE_UNIT  = "Единица измерения угла";

}
