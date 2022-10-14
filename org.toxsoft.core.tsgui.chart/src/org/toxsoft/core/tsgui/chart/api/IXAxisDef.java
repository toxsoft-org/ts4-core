package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Информация для создания X-шкалы (шкалы времени).
 * 
 * @author goga
 */
public interface IXAxisDef
    extends IAxisDef {

  /**
   * Возвращает начальный интервал времени X-шкалы.
   * 
   * @return {@link ITimeInterval} - начальный интервал времени X-шкалы
   */
  ITimeInterval initialTimeInterval();

  /**
   * Возвращает начальную цену деления.
   * 
   * @return {@link ETimeUnit} - начальная цена деления
   */
  ETimeUnit initialTimeUnit();

  /**
   * Возвращает допустимые цены деления при масштабировании шкалы.
   * 
   * @return IList&lt;{@link ETimeUnit}&gt; - упорядоченный список цен деления при масштабировании шкалы
   */
  IList<ETimeUnit> allowedTimeUnits();

}
