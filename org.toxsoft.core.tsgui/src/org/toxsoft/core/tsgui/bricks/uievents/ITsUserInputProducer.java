package org.toxsoft.core.tsgui.bricks.uievents;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface of classes generating {@link ITsUserInputListener} events.
 *
 * @author hazard157
 */
public interface ITsUserInputProducer {

  /**
   * Adds the user input event listener.
   * <p>
   * Already added listeners are ignored.
   *
   * @param aListener {@link ITsUserInputListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addTsUserInputListener( ITsUserInputListener aListener );

  /**
   * Removes the user input event listener.
   * <p>
   * If listener was not added then method does nothing.
   *
   * @param aListener {@link ITsUserInputListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeTsUserInputListener( ITsUserInputListener aListener );

}
