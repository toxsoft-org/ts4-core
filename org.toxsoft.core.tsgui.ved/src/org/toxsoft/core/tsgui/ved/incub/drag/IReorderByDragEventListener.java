package org.toxsoft.core.tsgui.ved.incub.drag;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;

/**
 * Слушатель события перетаскивания элемента коллекции внутри viewer'а.
 * <p>
 *
 * @author vs
 * @param <T> - entity class
 */
public interface IReorderByDragEventListener<T> {

  /**
   * Генерируется при завершении перетаскивания элемента списка {@aSource}.
   *
   * @param aViewer IM5CollectionViewer - viewer
   * @param aSourceNode ITsNode - перетаскиваемый узел
   * @param aTargetNode ITsNode - узел на который было совершено перетаскивание
   * @param aPlace {@link ECollectionDropPlace} - место относительно {@aTarget}
   */
  void onReorderRequest( IM5CollectionViewer<T> aViewer, ITsNode aSourceNode, ITsNode aTargetNode,
      ECollectionDropPlace aPlace );
}
