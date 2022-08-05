package org.toxsoft.core.tsgui.bricks.uievents;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface of classes generating {@link ITsMouseInputListener} events.
 *
 * @author hazard157
 */
public interface ITsMouseInputProducer {

  /**
   * Adds the mouse input event listener.
   * <p>
   * Already added listeners are ignored.
   *
   * @param aListener {@link ITsMouseInputListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addTsMouseInputListener( ITsMouseInputListener aListener );

  /**
   * Removes the mouse input event listener.
   * <p>
   * If listener was not added then method does nothing.
   *
   * @param aListener {@link ITsMouseInputListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeTsMouseInputListener( ITsMouseInputListener aListener );

}
