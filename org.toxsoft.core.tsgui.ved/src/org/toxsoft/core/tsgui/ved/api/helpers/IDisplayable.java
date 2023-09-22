package org.toxsoft.core.tsgui.ved.api.helpers;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Отображаемый элемент.
 *
 * @author vs
 */
public interface IDisplayable {

  /**
   * Отображает (отрисовывает) элемент.<br>
   *
   * @param aGc GC - графический контекст
   */
  void paint( GC aGc );

  /**
   * Возвращает описывающий прямоугольник.<br>
   *
   * @return Rectangle - описывающий прямоугольник
   */
  ITsRectangle bounds();

}
