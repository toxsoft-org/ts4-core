package org.toxsoft.core.tsgui.utils;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link HmsUtils}
   */
  String FMT_ERR_SECS_IS_NEGATIVE     = "Количество секунд с начала (%d) не может быть отрицательным";
  String FMT_ERR_SECS_IS_OVER_MAX     = "Количество секунд с начала (%d) не может больше %d (%s чч:мм:сс)";
  String FMT_ERR_DURATION_IS_NEGATIVE = "Не допускается отрицательная длительность %d";
  String FMT_WARN_DURATION_IS_ZERO    = "Нулевая длительсность подозрительна";
  String FMT_ERR_DURATION_IS_OVER_MAX = "Длительность %d секунд больше допустимого максимума %d (%s ччч:мм:сс)";
  String MSG_ERR_INV_MM_SS_FORMAT     = "Неверный формат длительности ММ:СС";
  String MSG_ERR_INV_HH_MM_SS_FORMAT  = "Неверный формат длительности ЧЧ:ММ:СС";

}
