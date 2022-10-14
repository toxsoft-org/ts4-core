package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Отрисовщик шкалы.
 *
 * @author vs
 */
public interface IG2AxisRenderer {

  /**
   * Возвращает предпочтительные размеры области для рисования без учета заголовка.
   *
   * @param aGc GC - графический контекст
   * @param aHorHint int - размер по горизонтали в пикселях, который можно учесть или SWT.DEFAULT если горизонтальный
   *          размер не имеет значения
   * @param aVertHint int - размер по вертикали в пикселях, который можно учесть или SWT.DEFAULT если вертикальный
   *          размер не имеет значения
   * @return Point TsPoint - предпочтительные размеры области для рисования
   */
  ITsPoint prefSize( GC aGc, int aHorHint, int aVertHint );

  /**
   * Возвращает размеры, требуемые для отрисовки заголовка шкалы.
   *
   * @param aGc GC - графический контекст
   * @return TsPoint - размеры заголовка шкалы
   */
  ITsPoint requiredTitleSize( GC aGc );

  /**
   * Ориентация заголовка по вертикали или по горизонтали.
   *
   * @return ETsOrientation - ориентация заголовка
   */
  ETsOrientation titleOrientation();

  /**
   * Рисует задний план шкалы, на переданном холсте.
   *
   * @param aGc GC - графический контекст
   * @param aBounds TsRectangle - границы области
   * @param aCardinalPoint EBorderLayoutPlacement - положение на плоскости
   */
  void drawBackground( GC aGc, ITsRectangle aBounds, EBorderLayoutPlacement aCardinalPoint );

  /**
   * Рисует шкалу, на переданном холсте.
   *
   * @param aGc GC - графический контекст
   * @param aBounds TsRectangle - границы области
   * @param aCardinalPoint EBorderLayoutPlacement - положение на плоскости
   */
  void drawAxis( GC aGc, ITsRectangle aBounds, EBorderLayoutPlacement aCardinalPoint );

}
