package org.toxsoft.core.tslib.bricks.time;

import org.toxsoft.core.tslib.bricks.time.impl.TimeUtils;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link EQueryIntervalType}
   */
  String STR_N_QIT_OSOE = "(интервал)";
  String STR_D_QIT_OSOE = "Интерал запроса с откритым началом и окончанием";
  String STR_N_QIT_OSCE = "(интервал]";
  String STR_D_QIT_OSCE = "Интерал запроса с откритым началом и закрытым окончанием";
  String STR_N_QIT_CSOE = "[интервал)";
  String STR_D_QIT_CSOE = "Интерал запроса с закритым началом и открытым окончанием";
  String STR_N_QIT_CSCE = "[интервал]";
  String STR_D_QIT_CSCE = "Интерал запроса с закритым началом и окончанием";

  /**
   * {@link TimeUtils}
   */
  String FMT_INTERVAL_TO_STRING        = "startTime = %s, endTime = %s";
  String MSG_ERR_WRONG_TIMESTAMP       = "Недопустимый формат метки времени";
  String FMT_ERR_END_TIME_BEFORE_START = "Окончание интервала (%tc) раньше начала (%tc)";

}
