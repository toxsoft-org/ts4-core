package org.toxsoft.core.tslib.coll.derivative;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The queue - FIFO (First In First Out) collection.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface IQueue<E>
    extends IList<E>, ITsClearable, ITsSizeRestrictableCollection {

  /**
   * Adds the element to the tail of the queue.
   * <p>
   * Method {@link #offerTail(Object)} returns <code>false</code> if queue is full while method {@link #putTail(Object)}
   * throws an exception.
   *
   * @param aElem &lt;E&gt; - the element to add
   * @return boolean - always <b>true</b>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException queue is full
   */
  boolean putTail( E aElem );

  /**
   * Adds the element to the tail of the queue.
   * <p>
   * Method {@link #offerTail(Object)} returns <code>false</code> if queue is full while method {@link #putTail(Object)}
   * throws an exception.
   *
   * @param aElem &lt;E&gt; - the element to add
   * @return boolean - <b>true</b> if element was added and <code>false</code> is queue is full
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean offerTail( E aElem );

  /**
   * Removes and returns the element from the head of the queue.
   * <p>
   * Method {@link #getHeadOrNull()} returns <code>null</code> is queue is empty while {@link #getHead()} throws an
   * exception.
   *
   * @return &lt;E&gt; - next element from the queue
   * @throws TsIllegalStateRtException the queue is empty
   */
  E getHead();

  /**
   * Removes and returns the element from the head of the queue.
   * <p>
   * Method {@link #getHeadOrNull()} returns <code>null</code> is queue is empty while {@link #getHead()} throws an
   * exception.
   *
   * @return &lt;E&gt; - next element from the queue or <code>null</code> if queue is empty
   */
  E getHeadOrNull();

  /**
   * Returns the element from the head of the queue but element remains in queue.
   * <p>
   * Method {@link #peekHeadOrNull()} returns <code>null</code> is queue is empty while {@link #peekHead()} throws an
   * exception.
   *
   * @return &lt;E&gt; - next element from the queue
   * @throws TsIllegalStateRtException the queue is empty
   */
  E peekHead();

  /**
   * Returns the element from the head of the queue but element remains in queue.
   * <p>
   * Method {@link #peekHeadOrNull()} returns <code>null</code> is queue is empty while {@link #peekHead()} throws an
   * exception.
   *
   * @return &lt;E&gt; - next element from the queue or <code>null</code> if queue is empty
   */
  E peekHeadOrNull();

}
