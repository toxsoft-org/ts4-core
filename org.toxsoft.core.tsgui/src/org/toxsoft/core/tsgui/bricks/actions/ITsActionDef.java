package org.toxsoft.core.tsgui.bricks.actions;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
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
   * @return int - SWT style of this action, <code>IAction.AS_XXX</code> flag
   */
  int actionStyle();

  /**
   * Returns the SWT style of action for SWT entities like menu items, buttons, etc.
   *
   * @return int the SWT style, one of {@link SWT#PUSH}, {@link SWT#CHECK}, {@link SWT#CHECK}
   */
  int swtStyle();

  /**
   * Determines if this is a separator (in menus, toolbars, etc).
   *
   * @return boolean - <code>true</code> for separator, <code>false</code> for regular action definition
   */
  boolean isSeparator();

}
