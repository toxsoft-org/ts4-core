package org.toxsoft.core.tsgui.ved.zver1.extra.tools;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Вершина - чувствительная для мыши отображаемая область экрана.
 * <p>
 * Предназначена для формирования "активной" грницы инструментами редактора. Например для прямоугольной границы захват и
 * перетаскивание одной из вершин прямоугольника может приводить к изменению размеров подчиненной компоненты.<br>
 *
 * @author vs
 */
public interface IVedVertex
    extends IStridable, IParameterized {

  /**
   * Возвращает описывающий прямоугольник.<br>
   *
   * @return Rectangle - описывающий прямоугольник
   */
  Rectangle bounds();

  /**
   * Тип курсора мыши, когда он находится над вершиной.
   *
   * @return ECursorType - тип курсора мыши, когда он находится над вершиной
   */
  ECursorType cursorType();

  /**
   * Отрисовывает вершину.<br>
   *
   * @param aGc GC - графический контекст
   */
  void paint( GC aGc );

  /**
   * Определяет, принадлежит ли точка в экранных координатах экранному объекту.<br>
   *
   * @param aX int - экранная x координата точки
   * @param aY int - экранная y координата точки
   * @return <b>true</b> - точка принадлежит объекту<br>
   *         <b>false</b> - точка находится вне объекта
   */
  boolean containsScreenPoint( int aX, int aY );

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
