package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Интерфейс отрисовщика выделения компонент на {@link VedScreen}.
 * <p>
 *
 * @author vs
 */
public interface IVedScreenSelectionDecorator
    extends IVedViewDecorator, IParameterized {

  /**
   * Помещает ИД компоненты в список скрытых, для всех компонент в этом списке выделение рисоваться не будет.
   *
   * @param aViewId - ИД представления компоненты
   */
  void hideSelection( String aViewId );

  /**
   * Если ИД находится в списке скрытых представлений, то удаляет его оттуда, если ИД в списке отсутствует, то ничего не
   * делает.
   *
   * @param aViewId - ИД представления компоненты
   */
  void showSelection( String aViewId );

  /**
   * Очищает список скрытых ИДов кмпонент. Это те компоненты для которых выделение не рисуется.
   */
  void showAll();

  @Override
  default void paintBefore( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // по умолчанию выделение рисуется после, а не до
  }

}
