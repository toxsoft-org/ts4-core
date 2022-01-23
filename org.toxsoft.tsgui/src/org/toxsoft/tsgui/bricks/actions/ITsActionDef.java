package org.toxsoft.tsgui.bricks.actions;

import org.eclipse.jface.action.IAction;
import org.toxsoft.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.tslib.bricks.strid.IStridableParameterized;

/**
 * Описание действии в GUI.
 *
 * @author hazard157
 */
public interface ITsActionDef
    extends IStridableParameterized {

  /**
   * Возвращает стиль действия, одна из {@link IAction#AS_UNSPECIFIED}, {@link IAction#AS_CHECK_BOX},
   * {@link IAction#AS_DROP_DOWN_MENU}, {@link IAction#AS_PUSH_BUTTON}, {@link IAction#AS_RADIO_BUTTON}.
   *
   * @return int - тип действия
   */
  int actionStyle();

  /**
   * Возвращает идентификатор значка к для использования с {@link ITsIconManager}.
   *
   * @return String - имя значка действия или <code>null</code>, если у действия нет значка
   */
  String iconId();

}
