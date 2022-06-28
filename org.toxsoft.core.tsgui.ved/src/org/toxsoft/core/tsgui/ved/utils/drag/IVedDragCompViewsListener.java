package org.toxsoft.core.tsgui.ved.utils.drag;

import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Слушатель события перетаскивания фигур.
 * <p>
 *
 * @author vs
 */
public interface IVedDragCompViewsListener {

  /**
   * Вызывается в момент, кода необходимо переместить фигуры в новое положение.
   *
   * @param aDx double - смещение по оси X
   * @param aDy double - смещение по оси Y
   * @param aShapes IStridablesList&lt;IVedComponentView> - список перемещаемых представлений
   * @param aDragState ETsDragState - состояние "перетаскивания"
   */
  void onShapesDrag( double aDx, double aDy, IStridablesList<IVedComponentView> aShapes, ETsDragState aDragState );
}
