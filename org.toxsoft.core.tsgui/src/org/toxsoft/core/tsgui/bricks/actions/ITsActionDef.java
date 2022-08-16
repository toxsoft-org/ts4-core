package org.toxsoft.core.tsgui.bricks.actions;

import org.eclipse.jface.action.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Defines the GUI action.
 *
 * @author hazard157
 */
public interface ITsActionDef
    extends IStridableParameterized {

  /**
   * Returns the SWT style of action.
   * <p>
   * Returns one of {@link IAction#AS_UNSPECIFIED}, {@link IAction#AS_CHECK_BOX}, {@link IAction#AS_DROP_DOWN_MENU},
   * {@link IAction#AS_PUSH_BUTTON}, {@link IAction#AS_RADIO_BUTTON}.
   *
   * @return int - SWT style of this ation
   */
  int actionStyle();

}
