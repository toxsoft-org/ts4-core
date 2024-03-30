package org.toxsoft.core.tsgui.ved.editor;

import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

public interface IViselsPositionProcessor
    extends IStridable {

  /**
   * Редактирует, изменяет (если необходимо) список идентификаторов перемещаемых элементов редактора {@link IVedItem}.
   *
   * @param aViselIds {@link IStringListEdit} - текущий список идентификаторов перемещаемых визуальных элементов
   * @param aParams {@link IOptionSetEdit} - редактируемый набор параметров
   */
  void editIdsForMove( IStringListEdit aViselIds, IOptionSetEdit aParams );

  // FIXME реализовать
  // void editIdsForAlignment
}
