package org.toxsoft.core.tslib.coll.synch;

import java.util.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Internal thread-safe wrapper over an editable list.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 * @param <L> - wrapped list type
 */
class SynchronizedListBase<E, L extends IListBasicEdit<E>>
    extends SynchronizedList<E, L>
    implements IListBasicEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   *
   * @param aSource &lt;L&gt; - the source collection
   * @param aLock {@link ReentrantReadWriteLock} - thread safety lock
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected SynchronizedListBase( L aSource, ReentrantReadWriteLock aLock ) {
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
  protected SynchronizedListBase( L aSource ) {
    super( aSource );
  }

  // ------------------------------------------------------------------------------------
  // IListBasicEdit
  //

  @Override
  public int add( E aElem ) {
    lock.writeLock().lock();
    try {
      return source.add( aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void addAll( E... aArray ) {
    lock.writeLock().lock();
    try {
      source.addAll( aArray );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void addAll( ITsCollection<E> aElemList ) {
    lock.writeLock().lock();
    try {
      source.addAll( aElemList );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void addAll( Collection<E> aElemColl ) {
    lock.writeLock().lock();
    try {
      source.addAll( aElemColl );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

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

  @Override
  public int remove( E aElem ) {
    lock.writeLock().lock();
    try {
      return source.remove( aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E removeByIndex( int aIndex ) {
    lock.writeLock().lock();
    try {
      return source.removeByIndex( aIndex );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void setAll( E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    lock.writeLock().lock();
    try {
      source.clear();
      source.addAll( aArray );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void setAll( ITsCollection<E> aElemList ) {
    TsNullArgumentRtException.checkNull( aElemList );
    lock.writeLock().lock();
    try {
      source.clear();
      source.addAll( aElemList );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void setAll( Collection<E> aElemColl ) {
    TsErrorUtils.checkCollectionArg( aElemColl );
    lock.writeLock().lock();
    try {
      source.clear();
      source.addAll( aElemColl );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

}
