package org.toxsoft.core.tsgui.bricks.swtevents;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface of classes generating {@link ISwtKeyListener} events.
 *
 * @author hazard157
 */
public interface ISwtKeyEventProducer {

  /**
   * Add the listener.
   * <p>
   * If specified listener is already added then method does nothing.
   *
   * @param aListener {@link ISwtKeyListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addSwtKeyListener( ISwtKeyListener aListener );

  /**
   * Removes the listener
   * <p>
   * If specified listener is not added then method does nothing.
   *
   * @param aListener {@link ISwtKeyListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeSwtKeyListener( ISwtKeyListener aListener );

}
