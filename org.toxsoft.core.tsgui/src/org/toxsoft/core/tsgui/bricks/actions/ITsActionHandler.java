package org.toxsoft.core.tsgui.bricks.actions;

/**
 * Handles the action by ID.
 *
 * @author hazard157
 */
public interface ITsActionHandler {

  /**
   * Called when the action is fired.
   *
   * @param aActionId String - the action ID {@link ITsActionDef#id()}
   */
  void handleAction( String aActionId );

}
