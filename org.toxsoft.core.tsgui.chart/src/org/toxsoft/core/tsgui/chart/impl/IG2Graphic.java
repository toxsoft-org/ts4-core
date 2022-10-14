package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Визуальное представление графика.
 * <p>
 *
 * @author vs
 */
public interface IG2Graphic {

  /**
   * Информация для создания и отображения графика.
   *
   * @return IPlotDef информация для создания и отображения графика
   */
  IPlotDef plotDef();

  /**
   * Отрисовывает график.
   *
   * @param aGc GC - графический контекст
   * @param aClientRect TsRectangle - область отрисовки графика
   */
  void draw( GC aGc, ITsRectangle aClientRect );

  /**
   * Рисует представление графика - то как он выглядит (без учета реальных данных) и что визуально может
   * идентифицировать его.
   * <p>
   * <b>Мотивация:</b><br>
   * Данное представление может быть использовано для формирования легенды и визуального отображения значений визира.
   * <br>
   * <b>На заметку:</b><br>
   * Не выход за переданные границы является областью отвественности реализации. Фон области отрисовывать не надо.
   *
   * @param aGc GC - графический контекст (не null)
   * @param aBounds TsRectangle - границы области, в которой должно уместиться представление (не null)
   * @throws TsNullArgumentRtException если любой аргумент null
   */
  void drawRepresentation( GC aGc, ITsRectangle aBounds );

  /**
   * Возвращает значение для переданного момента времени.
   * <p>
   * В случае если в наборе данных значение для, переданного момента времени, отсутствует, возвращается либо
   * аппроксимированное значение, либо {@link ITemporalAtomicValue#NULL}
   *
   * @param aTimestamp long - момент времени в миллисекундах
   * @return ITemporalAtomicValue - значение в запрашиваемый момент времени м.б. {@link ITemporalAtomicValue#NULL}
   */
  ITemporalAtomicValue valueAt( long aTimestamp );

  /**
   * Возвращает строковое представление переданного значения.
   *
   * @param aValue ITemporalAtomicValue - значение
   * @return String - строковое представление переданного значения
   * @throws TsNullArgumentRtException если aValue null
   */
  String valueToString( ITemporalAtomicValue aValue );

  boolean isVisible();

  void setVisible( boolean aVisible );

  void setYAxisView( YAxisView aAxis );
}
