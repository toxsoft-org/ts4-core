package org.toxsoft.tsgui.panels.toolbar;

import org.toxsoft.tsgui.bricks.actions.ITsActionDef;

/**
 * Listener for {@link TsToolBar} buttons click.
 *
 * @author hazard157
 */
public interface ITsToolBarListener {

  /**
   * Called when button is pressed on toolbar.
   *
   * @param aActionId String - the button action ID {@link ITsActionDef#id()}
   */
  void onToolButtonPressed( String aActionId );

}
