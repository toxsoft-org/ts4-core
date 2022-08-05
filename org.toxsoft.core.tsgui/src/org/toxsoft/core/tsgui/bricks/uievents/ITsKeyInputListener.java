package org.toxsoft.core.tsgui.bricks.uievents;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
 * KIeyboard input event listener.
 * <p>
 * Each method returns boolean flag. <code>true</code> means that event was handled by listener so there is no need to
 * call other listeners of the same event. All default implementations return <code>false</code>.
 *
 * @author hazard157
 */
public interface ITsKeyInputListener {

  /**
   * Called when key was pressed.
   *
   * @param aSource Object - the event source
   * @param aCode int - key code (as specified in {@link SWT})
   * @param aChar char - corresponding character symbol as in {@link KeyEvent#character}
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @return default boolean - event processing flag
   */
  default boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    return false;
  }

  /**
   * Called when key was released.
   *
   * @param aSource Object - the event source
   * @param aCode int - key code (as specified in {@link SWT})
   * @param aChar char - corresponding character symbol as in {@link KeyEvent#character}
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @return boolean - event processing flag
   */
  default boolean onKeyUp( Object aSource, int aCode, char aChar, int aState ) {
    return false;
  }

}
