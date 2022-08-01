package org.toxsoft.core.tsgui.ved.olds;

import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Слушатель обытий "перетаскивания" 2d-объектов.
 * <p>
 *
 * @author vs
 */
public interface IDragListener {

  /**
   * Вызывается в начальный момент процесса перетаскивания.<br>
   *
   * @param aObjects IStridablesList&lt;IShape2dView> - список перетаскиваемых объектов
   * @param aEvent MouseEvent - информация о положениии курсора и состянии кнопок
   */
  void onStartDrag( IStridablesList<IVedComponentView> aObjects, MouseEvent aEvent );

  /**
   * Вызывается в процессе перетаскивания.<br>
   *
   * @param aCurrEvent MouseEvent - информация о положениии курсора и состянии кнопок
   */
  void onDrag( MouseEvent aCurrEvent );

  /**
   * Вызывается в момент успешного окончания процесса перетаскивания (drop).<br>
   *
   * @param aCurrEvent MouseEvent - информация о положениии курсора и состянии кнопок
   */
  void onFinishDrag( MouseEvent aCurrEvent );

  /**
   * Вызывается в момент отмены процесса перетаскивания - возврат в положение до начала перетаскивания.<br>
   *
   * @param aCurrEvent MouseEvent - информация о положениии курсора и состянии кнопок
   */
  void onCancelDrag( MouseEvent aCurrEvent );

}
