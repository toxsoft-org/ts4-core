package org.toxsoft.core.tslib.coll.derivative;

import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over {@link IQueue} implementation.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class SynchronizedQueueWrapper<E>
    extends SynchronizedList<E, IQueue<E>>
    implements IQueue<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor with all invariants.
   *
   * @param aSource {@link IQueue}&lt;E&gt; - the source collection
   * @param aLock {@link ReentrantReadWriteLock} - thread safety lock
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedQueueWrapper( IQueue<E> aSource, ReentrantReadWriteLock aLock ) {
    super( aSource, aLock );
  }

  /**
   * Constructor.
   * <p>
   * Internally creates the new instance of {@link ReentrantReadWriteLock}.
   *
   * @param aSource {@link IQueue}&lt;E&gt; - the source collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedQueueWrapper( IQueue<E> aSource ) {
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
  // IQueue
  //

  @Override
  public boolean putTail( E aElem ) {
    lock.writeLock().lock();
    try {
      return source.putTail( aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public boolean offerTail( E aElem ) {
    lock.writeLock().lock();
    try {
      return source.offerTail( aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E getHead() {
    lock.writeLock().lock();
    try {
      return source.getHead();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E getHeadOrNull() {
    lock.writeLock().lock();
    try {
      return source.getHeadOrNull();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peekHead() {
    lock.writeLock().lock();
    try {
      return source.peekHead();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peekHeadOrNull() {
    lock.writeLock().lock();
    try {
      return source.peekHeadOrNull();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

}
