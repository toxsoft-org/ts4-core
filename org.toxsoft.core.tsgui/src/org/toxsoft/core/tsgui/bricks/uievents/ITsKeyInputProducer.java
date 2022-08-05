package org.toxsoft.core.tsgui.bricks.uievents;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface of classes generating {@link ITsKeyInputListener} events.
 *
 * @author hazard157
 */
public interface ITsKeyInputProducer {

  /**
   * Adds the user input event listener.
   * <p>
   * Already added listeners are ignored.
   *
   * @param aListener {@link ITsKeyInputListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addTsKeyInputListener( ITsKeyInputListener aListener );

  /**
   * Removes the user input event listener.
   * <p>
   * If listener was not added then method does nothing.
   *
   * @param aListener {@link ITsKeyInputListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeTsKeyInputListener( ITsKeyInputListener aListener );

}
