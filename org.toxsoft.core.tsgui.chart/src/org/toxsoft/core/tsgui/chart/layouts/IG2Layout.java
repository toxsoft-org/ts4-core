package org.toxsoft.core.tsgui.chart.layouts;

import ru.toxsoft.tsgui.chart.impl.IG2ChartArea;

/**
 * Базовый интерфейс всех видов раскладок графиков.
 *
 * @author goga
 */
public interface IG2Layout {

  // TODO видимо, в базовом интерфейсе не нужно больше ничего
  // Ха-ха

  /**
   * Возвращает область графической компоненты на которой находится курсор мыши.
   *
   * @param aX int - Х координата курсора относительно левого верхнего угла границы компоненты в пикселях
   * @param aY int - Y координата курсора относительно левого верхнего угла границы компоненты в пикселях
   * @return IG2ChartArea -область графической компоненты на которой находится курсор мыши или null
   */
  IG2ChartArea areaUnderCursor( int aX, int aY );

}
