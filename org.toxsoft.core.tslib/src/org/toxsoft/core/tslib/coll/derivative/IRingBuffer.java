package org.toxsoft.core.tslib.coll.derivative;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The ring (circular) buffer.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface IRingBuffer<E>
    extends ITsCountableCollection, ITsSizeRestrictableCollection, ITsClearable {

  /**
   * Puts element in the buffer.
   * <p>
   * If buffer is full the oldest element will be overwritten.
   *
   * @param aElem &lt;E&gt; - the element to put
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void put( E aElem );

  /**
   * Removes the oldest element from the buffer an decreases {@link #size()}.
   * <p>
   * If buffer is empty throws an exception.
   *
   * @return &lt;E&gt; - oldest element from the buffer
   * @throws TsIllegalStateRtException buffer is empty
   */
  E get();

  /**
   * Removes the oldest element from the buffer an decreases {@link #size()}.
   * <p>
   * If buffer is empty returns <code>null</code>.
   *
   * @return &lt;E&gt; - oldest element from the buffer or <code>null</code> if buffer is empty
   */
  E getOrNull();

  /**
   * Determines if buffer is full so next {@link #put(Object)} will overwrite oldest element.
   *
   * @return boolean - the flag that buffer is full
   */
  boolean isFull();

  /**
   * Returns the list of items in buffer.
   * <p>
   * Oldest item fill be the first item in list.
   *
   * @return {@link IList} - list of items in buffer (odered from oldest to newest)
   */
  IList<E> getItems();

}
