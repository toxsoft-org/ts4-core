package org.toxsoft.core.tslib.coll.synch;

import java.util.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over an editable list.
 *
 * @author hazard157
 * @version $id$
 * @param <E> - the type of elements in this collection
 */
public class SynchronizedListEdit<E>
    extends SynchronizedListBase<E, IListEdit<E>>
    implements IListEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   *
   * @param aSource &lt;L&gt; - the source collection
   * @param aLock {@link ReentrantReadWriteLock} - thread safety lock
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedListEdit( IListEdit<E> aSource, ReentrantReadWriteLock aLock ) {
    super( aSource, aLock );
  }

  /**
   * Constructor.
   * <p>
   * Internally creates the new instance of {@link ReentrantReadWriteLock}.
   *
   * @param aSource &lt;L&gt; - the source collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedListEdit( IListEdit<E> aSource ) {
    super( aSource );
  }

  // ------------------------------------------------------------------------------------
  // IListEdit
  //

  @Override
  public E set( int aIndex, E aElem ) {
    lock.writeLock().lock();
    try {
      return source.set( aIndex, aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    lock.writeLock().lock();
    try {
      source.insert( aIndex, aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void insertAll( int aIndex, E... aArray ) {
    lock.writeLock().lock();
    try {
      source.insertAll( aIndex, aArray );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<E> aElemList ) {
    lock.writeLock().lock();
    try {
      source.insertAll( aIndex, aElemList );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void insertAll( int aIndex, Collection<E> aElemColl ) {
    lock.writeLock().lock();
    try {
      source.insertAll( aIndex, aElemColl );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    lock.writeLock().lock();
    try {
      source.removeRangeByIndex( aIndex, aCount );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

}
