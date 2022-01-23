package org.toxsoft.tsgui.valed.controls.time;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link ValedTimestampMpv}, {@link ValedLocalTimeMpv}, {@link ValedLocalDateTimeMpv}
   */
  String STR_N_MPV_TIME_LEN = "Точность";
  String STR_D_MPV_TIME_LEN = "Вариант редактирования времени с точностью до минут/секунд/миллисекунд";

  /**
   * {@link ValedSecsDurationMpv}
   */
  String STR_N_IS_HOURS_PART   = "ЧЧ?";
  String STR_D_IS_HOURS_PART   = "Признак наличия части ЧЧ в редаторе ЧЧ:ММ:СС или ЧЧ:ММ";
  String STR_N_IS_SECONDS_PART = "СС?";
  String STR_D_IS_SECONDS_PART = "Признак наличия части СС в редаторе ЧЧ:ММ:СС или ММ:СС";

}
