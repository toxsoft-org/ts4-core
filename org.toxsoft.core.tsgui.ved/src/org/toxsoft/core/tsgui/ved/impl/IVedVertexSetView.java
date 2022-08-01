package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальное представление набора вершин.
 * <p>
 *
 * @author vs
 */
public interface IVedVertexSetView {

  /**
   * Возвращает описывающий прямоугольник.<br>
   *
   * @return Rectangle - описывающий прямоугольник
   */
  Rectangle bounds();

  /**
   * Возвращает список вершин.<br>
   *
   * @return IStridablesList&lt;? extends IVedVertex> - список вершин
   */
  IStridablesList<? extends IVedVertex> listVertexes();

  /**
   * Добаляет вершину к набору.<br>
   *
   * @param aVertex IVedVertex - добавляемая вершина
   * @throws TsItemAlreadyExistsRtException - если вершина с таким идентификатором уже существует
   */
  void addVertex( IVedVertex aVertex );

  /**
   * Задает прямоугольник ограничивающий фигуру для редактирования которой с создается набор вершин.
   *
   * @param aRect ITsRectangle - прямоугольник ограничивающий фигуру для редактирования которой с создается набор вершин
   */
  void init( ITsRectangle aRect );

  /**
   * @param aDx double - смещение по X
   * @param aDy double - смещение по Y
   * @param aVertexId String - ИД вершины
   */
  void update( double aDx, double aDy, String aVertexId );

  /**
   * Возвращает признак видимости набора вершин.<br>
   *
   * @return <b>true</b> - набор вершин видим<br>
   *         <b>false</b> - набор вершин не видим
   */
  boolean visible();

  /**
   * Задает признак видимости набора вершин <b>true</b> - набор вершин видим.<br>
   *
   * @param aVisible <b>true</b><br>
   *          <b>false</b>
   */
  void setVisible( boolean aVisible );

  /**
   * Отрисовывает набор вершин.<br>
   *
   * @param aGc GC - графический контекст
   */
  void paint( GC aGc );
}
