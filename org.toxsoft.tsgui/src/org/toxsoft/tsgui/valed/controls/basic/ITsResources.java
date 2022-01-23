package org.toxsoft.tsgui.valed.controls.basic;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link ValedComboSelector}
   */
  String STR_N_ITEMS_PROVIDER = "Элементы";
  String STR_D_ITEMS_PROVIDER = "Поставщик элементов для выбора из выпадающего списка";

  /**
   * {@link ValedDoubleSpinner}
   */
  String STR_N_DOUBLE_SPINNER_FLOATING_DIGITS = "После запятой";
  String STR_D_DOUBLE_SPINNER_FLOATING_DIGITS = "Количество знаков после запятой";
  String STR_N_DOUBLE_SPINNER_STEP            = "Шаг";
  String STR_D_DOUBLE_SPINNER_STEP            = "Шаг изменения значения стрелкой";
  String STR_N_DOUBLE_SPINNER_PAGE_STEP       = "Скачок";
  String STR_D_DOUBLE_SPINNER_PAGE_STEP       = "Шаг изменения значения клавишами PageUp/PageDown";
  String FMT_ERR_INV_FLOATING_TEXT            = "Неверный формат представления вещественного числа";

  /**
   * {@link ValedIntegerSpinner}
   */
  String STR_N_INT_SPINNER_STEP      = "Шаг";
  String STR_D_INT_SPINNER_STEP      = "Шаг изменения значения стрелкой";
  String STR_N_INT_SPINNER_PAGE_STEP = "Скачок";
  String STR_D_INT_SPINNER_PAGE_STEP = "Шаг изменения значения клавишами PageUp/PageDown";

  /**
   * {@link ValedStringText}
   */
  String STR_N_IS_MULTI_LINE = "Многострочный?";
  String STR_D_IS_MULTI_LINE = "Признак редактирования многострочного текста";

  // /**
  // * {@link ValedTimestampMpv}, {@link ValedLocalTimeMpv}
  // */
  // String STR_N_MPV_TIME_LEN = "Точность";
  // String STR_D_MPV_TIME_LEN = "Вариант редактирования времени с точностью до минут/секунд/миллисекунд";

}
