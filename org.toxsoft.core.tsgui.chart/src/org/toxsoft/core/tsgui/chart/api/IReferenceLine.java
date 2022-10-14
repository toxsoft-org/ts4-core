package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Опрная линия графика.
 * <p>
 *
 * @author vs
 */
public interface IReferenceLine
    extends IStridable {

  /**
   * Значение по шкале Y, соответствующее опорной линии.
   *
   * @return IAtomicValue - значение по шкале Y, соответствующее опорной линии
   */
  IAtomicValue refValue();

  /**
   * Возвращает параметры отрисовки опорной линии.
   *
   * @return IG2Params - параметры отрисовки опорной линии
   */
  IG2Params rendererInfo();

}
