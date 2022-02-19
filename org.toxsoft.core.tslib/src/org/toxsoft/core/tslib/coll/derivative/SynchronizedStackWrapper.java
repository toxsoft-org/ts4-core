package org.toxsoft.core.tslib.coll.derivative;

import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over {@link IStack} implementation.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class SynchronizedStackWrapper<E>
    extends SynchronizedList<E, IStack<E>>
    implements IStack<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor with all invariants.
   *
   * @param aSource {@link IStack}&lt;E&gt; - the source collection
   * @param aLock {@link ReentrantReadWriteLock} - thread safety lock
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedStackWrapper( IStack<E> aSource, ReentrantReadWriteLock aLock ) {
    super( aSource, aLock );
  }

  /**
   * Constructor.
   * <p>
   * Internally creates the new instance of {@link ReentrantReadWriteLock}.
   *
   * @param aSource {@link IStack}&lt;E&gt; - the source collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedStackWrapper( IStack<E> aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    lock.writeLock().lock();
    try {
      source.clear();
    }
    finally {
      lock.writeLock().unlock();
    }
  }
  // ------------------------------------------------------------------------------------
  // ITsSizeRestrictableCollection
  //

  @Override
  public boolean isSizeRestricted() {
    lock.readLock().lock();
    try {
      return source.isSizeRestricted();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int maxSize() {
    lock.readLock().lock();
    try {
      return source.maxSize();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // IStack
  //

  @Override
  public void push( E aElem ) {
    lock.writeLock().lock();
    try {
      source.push( aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E pop() {
    lock.writeLock().lock();
    try {
      return source.pop();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E popOrNull() {
    lock.writeLock().lock();
    try {
      return source.popOrNull();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peek() {
    lock.writeLock().lock();
    try {
      return source.peek();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peekOrNull() {
    lock.writeLock().lock();
    try {
      return source.peekOrNull();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

}
