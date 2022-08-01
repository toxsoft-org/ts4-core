package org.toxsoft.core.tsgui.bricks.swtevents;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface of classes generating {@link ISwtMouseListener} events.
 *
 * @author hazard157
 */
public interface ISwtMouseEventProducer {

  /**
   * Add the listener.
   * <p>
   * If specified listener is already added then method does nothing.
   *
   * @param aListener {@link ISwtMouseListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addSwtMouseListener( ISwtMouseListener aListener );

  /**
   * Removes the listener
   * <p>
   * If specified listener is not added then method does nothing.
   *
   * @param aListener {@link ISwtMouseListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeSwtMouseListener( ISwtMouseListener aListener );

}
