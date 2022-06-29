package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Отображаемый на экране объект.
 * <p>
 * <b>Мотивация:</b><br>
 * Для реализации функциональности обработчиков мыши инструментов редактора необходим класс, реализующий достаточно
 * ограниченный набор методов. Наличие "экранных" объектов позволяет иметь одну реализацию обработчиков мыши и событий
 * перетаскивания, практически для всех задач редактора.
 *
 * @author vs
 */
public interface IScreenObject
    extends IStridable {

  /**
   * Отрисовывает экранный объект.<br>
   *
   * @param aGc GC - графический контекст
   */
  void paint( GC aGc );

  /**
   * Возвращает "суть" экранного объекта.
   *
   * @param <T> - тип объекта составляющего "суть" экранного
   * @return T - "суть" экранного объекта
   */
  <T> T entity();

  /**
   * Возвращает описывающий прямоугольник.<br>
   *
   * @return Rectangle - описывающий прямоугольник
   */
  Rectangle bounds();

  /**
   * Определяет, принадлежит ли точка экранному объекту.<br>
   *
   * @param aX double - x координата точки
   * @param aY double - y координата точки
   * @return <b>true</b> - точка принадлежит объекту<br>
   *         <b>false</b> - точка находится вне объекта
   */
  boolean contains( double aX, double aY );

  /**
   * Возвращает тип курсора мыши, когда он находится над объектом.<br>
   *
   * @return ECursorType - тип курсора мыши, когда он находится над объектом
   */
  ECursorType cursorType();

  /**
   * Возвращает признак видимости объекта.<br>
   *
   * @return <b>true</b> - объект видим<br>
   *         <b>false</b> - объект не видим
   */
  boolean visible();

  /**
   * Задает признак видимости объекта <b>true</b> - объект видим.<br>
   *
   * @param aVisible <b>true</b><br>
   *          <b>false</b>
   */
  void setVisible( boolean aVisible );
}
