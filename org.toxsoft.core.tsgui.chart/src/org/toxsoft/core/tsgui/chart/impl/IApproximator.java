package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.temporal.*;

/**
 * Аппроксиматор значения между двумя точками.
 * <p>
 * <b>Мотивация:</b><br>
 * Аппроксимированное значение между двумя точками может быть найдено различными способами. Например если точки
 * соединяются линий или ступенькой эти способы будут разными. Поэтому введен данный интерфейс, чтобы развязаться от
 * конкретной реализации.
 *
 * @author vs
 */
public interface IApproximator {

  /**
   * @param aLeftValue ITemporalAtomicValue - левая точка
   * @param aRightValue ITemporalAtomicValue - правая точка
   * @param aTimeStamp long момент времени, гарантированно лежащей между левой и правой
   * @return IAtomicValue - аппроксимированное значение
   */
  IAtomicValue approximate( ITemporalAtomicValue aLeftValue, ITemporalAtomicValue aRightValue, long aTimeStamp );
}
