package org.toxsoft.core.tslib.bricks.time;

/**
 * Интервал времени, используемый при запросе различных данных.
 * <p>
 * Интерфейс используется при запросе расположенных "разряженных" по времени данных.
 */
public interface IQueryInterval
    extends ITimeInterval {

  /**
   * Возвращает тип интервала, определяющий возврат данных, выходящих за границы интервала.
   *
   * @return {@link EQueryIntervalType} - тип интервала
   */
  EQueryIntervalType type();

}
