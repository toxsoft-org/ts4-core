package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальное представление набора вершин.
 * <p>
 *
 * @author vs
 */
public interface IVedVertexSetView
    extends IScreenObject {

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
}
