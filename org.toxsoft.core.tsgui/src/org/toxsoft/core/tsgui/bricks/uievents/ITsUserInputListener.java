package org.toxsoft.core.tsgui.bricks.uievents;

/**
 * User input (currently mouse and keyboard) event listener.
 * <p>
 * This is simply union of {@link ITsKeyInputListener} and {@link ITsMouseInputListener}.
 *
 * @author hazard157
 */
public interface ITsUserInputListener
    extends ITsKeyInputListener, ITsMouseInputListener {

  // nop

}
