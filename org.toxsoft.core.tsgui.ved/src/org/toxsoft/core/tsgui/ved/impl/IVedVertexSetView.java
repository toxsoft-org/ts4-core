package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Визуальное представление набора вершин.
 * <p>
 *
 * @author vs
 */
public interface IVedVertexSetView
    extends ID2Conversionable {

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
   * Инициализирует набор вершин, передавая ему список представлений компонент.<br>
   *
   * @param aCompViews IStridablesList&lt;IVedComponentView> - список представлений компонент
   */
  void init( IStridablesList<IVedComponentView> aCompViews );

  /**
   * Возвращает список компонент, переданный ему в методе {@link #init(IStridablesList)}.<br>
   *
   * @return IStridablesList&lt;IVedComponentView> - список компонент, с которыми он был инициализирован
   */
  IStridablesList<IVedComponentView> componentViews();

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
