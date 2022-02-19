package org.toxsoft.core.tslib.coll.derivative;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The stack - FILO (First In Last Out) collection.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface IStack<E>
    extends IList<E>, ITsClearable, ITsSizeRestrictableCollection {

  /**
   * Puts the element in the stack.
   *
   * @param aElem &lt;E&gt; - the element to be pushed into the stack
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException stack is full
   */
  void push( E aElem );

  /**
   * Removes and returns an element from the stack top.
   * <p>
   * Method {@link #popOrNull()} returns <code>null</code> if the stack empty while method {@link #pop()} throws an
   * exception.
   *
   * @return &lt;E&gt; - the element that was on top of the stack
   * @throws TsIllegalStateRtException stack is empty
   */
  E pop();

  /**
   * Removes and returns an element from the stack top.
   * <p>
   * Method {@link #popOrNull()} returns <code>null</code> if the stack empty while method {@link #pop()} throws an
   * exception.
   *
   * @return &lt;E&gt; - the element that was on top of the stack or <code>null</code> if stack is empty
   */
  E popOrNull();

  /**
   * Returns an element from the stack top but the element remains at top.
   * <p>
   * Method {@link #peekOrNull()} returns <code>null</code> if the stack empty while method {@link #peek()} throws an
   * exception.
   *
   * @return &lt;E&gt; - the element at the top of the stack
   * @throws TsIllegalStateRtException stack is empty
   */
  E peek();

  /**
   * Returns an element from the stack top but the element remains at top.
   * <p>
   * Method {@link #peekOrNull()} returns <code>null</code> if the stack empty while method {@link #peek()} throws an
   * exception.
   *
   * @return &lt;E&gt; - the element at the top of the stack or <code>null</code> if stack is empty
   */
  E peekOrNull();

}
