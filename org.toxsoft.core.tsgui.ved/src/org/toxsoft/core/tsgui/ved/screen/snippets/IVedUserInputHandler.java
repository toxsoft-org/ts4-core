package org.toxsoft.core.tsgui.ved.screen.snippets;

import org.toxsoft.core.tsgui.bricks.uievents.*;

/**
 * Wraps over {@link ITsUserInputListener} taking into account the features of the VED framework.
 *
 * @author hazard157
 */
public interface IVedUserInputHandler
    extends IVedSnippet {

  /**
   * Returns the listener to be used for user input processing.
   *
   * @return {@link ITsUserInputListener} - the user input listener
   */
  ITsUserInputListener userInputListener();

}
