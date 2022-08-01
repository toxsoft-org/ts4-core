package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.bricks.d2.*;
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
   * Определяет, принадлежит ли точка в нормализованных координатах экранному объекту.<br>
   *
   * @param aX double - нормализованная x координата точки
   * @param aY double - нормализованная y координата точки
   * @return <b>true</b> - точка принадлежит объекту<br>
   *         <b>false</b> - точка находится вне объекта
   */
  boolean containsNormPoint( double aX, double aY );

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

  /**
   * Задает параметры преобразования координат.
   *
   * @param aConversion ID2Conversion - параметры преобразования координат
   */
  void setConversion( ID2Conversion aConversion );

  /**
   * Возвращает параметры преобразования координат.
   *
   * @return ID2Conversion - параметры преобразования координат
   */
  ID2Conversion getConversion();

}
