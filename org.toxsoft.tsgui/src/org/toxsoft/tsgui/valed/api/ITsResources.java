package org.toxsoft.tsgui.valed.api;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link IValedControlConstants}
   */
  String STR_N_EDITOR_FACTORY_NAME    = "Имя редактора";
  String STR_D_EDITOR_FACTORY_NAME    = "Регистрационное имя или имя класса фабрики для использования без регистрации";
  String STR_N_CREATE_UNEDITABLE      = "Нередактируемый?";
  String STR_D_CREATE_UNEDITABLE      = "Созданный контроль редактора будет показывать значение без возможности правки";
  String STR_N_TOOLTIP_TEXT           = "Подсказка";
  String STR_D_TOOLTIP_TEXT           = "Текст всплывающей подсказки";
  String STR_N_VALUE_VISUALS_PROVIDER = "Постащик визуала";
  String STR_D_VALUE_VISUALS_PROVIDER = "Ссылка на поставщик визуальногь предствления: текста, значков и т.п.";
  String STR_N_IS_WIDTH_FIXED         = "Фикс. ширина";
  String STR_D_IS_WIDTH_FIXED         = "Признак не желания контроля редактора занимать всю возможную ширину";
  String STR_N_IS_HEIGHT_FIXED        = "Фикс. высота";
  String STR_D_IS_HEIGHT_FIXED        = "Признак не желания контроля редактора занимать всю возможную высоту";
  String STR_N_VERTICAL_SPAN          = "Высота";
  String STR_D_VERTICAL_SPAN          = "Высота редактора (в единицах высоты однострочного поля ввода текста)";
  String STR_N_NO_FIELD_LABEL         = "Без метки?";
  String STR_D_NO_FIELD_LABEL         = "Признак скрытия подписи к редактору";

}
