package org.toxsoft.core.tslib.coll.notifier.basis;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Mixin interface of classes with ability to generate
 * {@link ITsCollectionChangeListener#onCollectionChanged(Object, ECrudOp, Object)} event.
 *
 * @author hazard157
 */
public interface ITsCollectionChangeEventProducer
    extends ITsPausabeEventsProducer {

  /**
   * Adds the listener.
   * <p>
   * If listener is already added, method soes nothing.
   *
   * @param aListener {@link ITsCollectionChangeListener} - the listener
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void addCollectionChangeListener( ITsCollectionChangeListener aListener );

  /**
   * Removes the listener.
   * <p>
   * If listener was not added, method soes nothing.
   *
   * @param aListener {@link ITsCollectionChangeListener} - the listener
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void removeCollectionChangeListener( ITsCollectionChangeListener aListener );

}
