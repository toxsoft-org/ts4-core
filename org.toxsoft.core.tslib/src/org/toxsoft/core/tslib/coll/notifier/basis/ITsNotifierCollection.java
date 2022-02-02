package org.toxsoft.core.tslib.coll.notifier.basis;

import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface for all collection with ability to be a data model.
 * <p>
 * In addition with common collections fuctionality, this interface adds following capabilities:
 * <ul>
 * <li>notifications <b>after</b> changes in elements via {@link ITsCollectionChangeListener}, including ability to
 * temporally suspend event generations;</li>
 * <li>vetoable checking <b>before</b> collection modification via {@link ITsMapValidator} or {@link ITsListValidator},
 * including to temporally disable validation.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface ITsNotifierCollection
    extends ITsCollectionChangeEventProducer {

  /**
   * Fires {@link ECrudOp#EDIT} event about specified element in collection.
   * <p>
   * This method invokes {@link ITsCollectionChangeListener#onCollectionChanged(Object, ECrudOp, Object)
   * onCollectionChanged(this, ECrudOp.<b>CHANGE</b>, element)}.
   *
   * @param aIndex int - index of the element
   * @throws TsIllegalArgumentRtException the index is out of range
   */
  void fireItemByIndexChangeEvent( int aIndex );

  /**
   * Fires {@link ECrudOp#EDIT} event about specified element in collection.
   * <p>
   * This method invokes {@link ITsCollectionChangeListener#onCollectionChanged(Object, ECrudOp, Object)
   * onCollectionChanged(this, ECrudOp.<b>CHANGE</b>, element)}.
   *
   * @param aItem Object - reference to element
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws ClassCastException optiona exception, thrown when argument is not of expected type
   * @throws TsItemNotFoundRtException collection does not contains specified element
   */
  void fireItemByRefChangeEvent( Object aItem );

  /**
   * Fires {@link ECrudOp#LIST} event.
   * <p>
   * This method invokes {@link ITsCollectionChangeListener#onCollectionChanged(Object, ECrudOp, Object)
   * onCollectionChanged(this, ECrudOp.<b>BATCH</b>, <code><b>null</b></code> )}.
   */
  void fireBatchChangeEvent();

  /**
   * Determines if checks before collection change is turned on.
   *
   * @return boolean - <code>true</code> if checks (validation) is enabled
   */
  boolean isValidationEnabled();

  /**
   * Turns on/off checks before collection change.
   *
   * @param aEnabled boolean - desided validation state
   */
  void setValidationEnabled( boolean aEnabled );

}
