package org.toxsoft.core.tsgui.ved.utils.drag;

import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.extra.tools.impl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Слушатель события перетаскивания экранных объектов.
 * <p>
 *
 * @author vs
 */
public interface IVedDragObjectsListener {

  /**
   * Вызывается в момент, кода необходимо переместить фигуры в новое положение.
   *
   * @param aDx double - смещение по оси X
   * @param aDy double - смещение по оси Y
   * @param aShapes IList&lt;IScreenObject> - список перемещаемых представлений
   * @param aDragState ETsDragState - состояние "перетаскивания"
   */
  void onShapesDrag( double aDx, double aDy, IList<IScreenObject> aShapes, ETsDragState aDragState );
}
