package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.av.utils.*;

/**
 * Вершина - чувствительная для мыши отображаемая область экрана.
 * <p>
 * Предназначена для формирования "активной" грницы инструментами редактора. Например для прямоугольной границы захват и
 * перетаскивание одной из вершин прямоугольника может приводить к изменению размеров подчиненной компоненты.<br>
 *
 * @author vs
 */
public interface IVedVertex
    extends IScreenObject, IParameterized {

  @Override
  ECursorType cursorType();

  /**
   * Задает цвет рисования.
   *
   * @param aColor Color - цвет рисования
   */
  void setForeground( Color aColor );

  /**
   * Задает цвет заливки.
   *
   * @param aColor Color - цвет заливки
   */
  void setBackground( Color aColor );

  /**
   * Возвращает цвет рисования.
   *
   * @return Color - цвет рисования
   */
  Color foregroundColor();

  /**
   * Возвращает цвет фона.
   *
   * @return Color - цвет фона
   */
  Color backgroundColor();
}
