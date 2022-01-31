package org.toxsoft.core.tslib.coll.impl;

import java.util.*;
import java.util.function.Consumer;

/**
 * Implementations of {@link Iterator} over any kind of always emty collections.
 *
 * @author hazard157
 * @param <E> - the type of elements returned by this iterator
 */
public class EmptyIterator<E>
    implements Iterator<E> {

  /**
   * Singleton of epty iterator over {@link String} collections.
   */
  public static final Iterator<String> EMPTY_STR_ITERATOR = new EmptyIterator<>();

  /**
   * Constructor.
   */
  public EmptyIterator() {
    // nop
  }

  /**
   * Alwayes returns <code>false</code>.
   *
   * @return boolean - always <code>false</code>
   */
  @Override
  public boolean hasNext() {
    return false;
  }

  /**
   * Always throws {@link NoSuchElementException}.
   *
   * @return &lt;E&gt; - nothing is returned ever
   * @throws NoSuchElementException always thrown
   */
  @Override
  public E next() {
    throw new NoSuchElementException();
  }

  /**
   * Nothing can be removed from always empty collections.
   *
   * @throws UnsupportedOperationException always thrown
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void forEachRemaining( Consumer<? super E> aAction ) {
    Objects.requireNonNull( aAction );
  }

}
