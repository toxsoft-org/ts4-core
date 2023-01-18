package org.toxsoft.core.tsgui.bricks.actions;

/**
 * Handles the action by ID.
 * <p>
 * Note: handler is a listener so several handlers may be called and some usually one of them handles the action.
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
