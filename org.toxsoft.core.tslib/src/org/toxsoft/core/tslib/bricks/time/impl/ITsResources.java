package org.toxsoft.core.tslib.bricks.time.impl;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link TimeUtils}
   */
  String FMT_INTERVAL_TO_STRING        = "startTime = %s, endTime = %s";
  String MSG_ERR_WRONG_TIMESTAMP       = "Недопустимый формат метки времени";
  String FMT_ERR_END_TIME_BEFORE_START = "Окончание интервала (%tc) раньше начала (%tc)";

}
