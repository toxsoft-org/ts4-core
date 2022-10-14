package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Отрисовщик фона.
 *
 * @author vs
 */
interface IBackgroundRenderer {

  /**
   * Рисует фон.
   *
   * @param aGc GC - графический контекст
   * @param aBounds TsRectangle - прямоугольник для рисования
   * @param aPlace EBorderLayoutPlacement - место (например, шкала слева - WEST)
   * @param aFocused boolean - признак того имеет ли элемент фокус (да если true)
   * @param aSelected boolean - признак того является ли элемент выделенным (да если true)
   */
  void drawBackground( GC aGc, ITsRectangle aBounds, EBorderLayoutPlacement aPlace, boolean aFocused,
      boolean aSelected );

}
