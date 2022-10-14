package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Отрисовщик графика состояний.
 *
 * @author vs
 */
interface IG2StateGraphicRenderer {

  /**
   * Отрисовывает график состояния.
   *
   * @param aGc GC - графичесский контекст
   * @param aY int - экранная координата Y в пикселях
   * @param aSegments IList - список отрезков, каждый из которых соотвествует одному состоянию
   * @param aBounds TsRectangle - границы области (холста - канвы) для рисования графика
   */
  void drawGraphic( GC aGc, int aY, IList<StateGraphSegment> aSegments, ITsRectangle aBounds );

  /**
   * Вычисляет высоту графика исходя из параметров отрисовки.
   *
   * @param aGc GC - графичесский контекст (не null)
   * @return int - высота области которая требуется графику для отображения.
   * @throws TsNullArgumentRtException если aGc null
   */
  int calcHeight( GC aGc );

  /**
   * Название состояния (например, "Вкл", "Закрыт" и т.д. ).
   *
   * @param aIdx int - номер состояния
   * @return название состояния
   */
  String stateName( int aIdx );
}
