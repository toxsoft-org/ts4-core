package org.toxsoft.core.tslib.coll.synch;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over <b>un</b>editable list.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 * @param <L> - wrapped list type
 */
public class SynchronizedList<E, L extends IList<E>>
    implements IList<E>, ITsSynchronizedCollectionWrapper<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final L                      source;

  /**
   * Constructor.
   *
   * @param aSource &lt;L&gt; - the source collection
   * @param aLock {@link ReentrantReadWriteLock} - thread safety lock
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedList( L aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
  }

  /**
   * Constructor.
   * <p>
   * Internally creates the new instance of {@link ReentrantReadWriteLock}.
   *
   * @param aSource &lt;L&gt; - the source collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedList( L aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  // ------------------------------------------------------------------------------------
  // IList<E>
  //

  @Override
  public E get( int aIndex ) {
    lock.readLock().lock();
    try {
      return source.get( aIndex );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean hasElem( E aElem ) {
    lock.readLock().lock();
    try {
      return source.hasElem( aElem );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int indexOf( E aElem ) {
    lock.readLock().lock();
    try {
      return source.indexOf( aElem );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isEmpty() {
    lock.readLock().lock();
    try {
      return source.isEmpty();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int size() {
    lock.readLock().lock();
    try {
      return source.size();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    lock.readLock().lock();
    try {
      return source.toArray( aSrcArray );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Object[] toArray() {
    lock.readLock().lock();
    try {
      return source.toArray();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E first() {
    lock.readLock().lock();
    try {
      return source.first();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E last() {
    lock.readLock().lock();
    try {
      return source.last();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E middle() {
    lock.readLock().lock();
    try {
      return source.middle();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E findOnly() {
    lock.readLock().lock();
    try {
      return source.findOnly();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E getOnly() {
    lock.readLock().lock();
    try {
      return source.getOnly();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E next( E aItem ) {
    lock.readLock().lock();
    try {
      return source.next( aItem );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E prev( E aItem ) {
    lock.readLock().lock();
    try {
      return source.prev( aItem );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public IListEdit<E> fetch( IIntList aIndexes ) {
    lock.readLock().lock();
    try {
      return source.fetch( aIndexes );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public IList<E> fetch( int aFromIndex, int aToIndex ) {
    lock.readLock().lock();
    try {
      return source.fetch( aFromIndex, aToIndex );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  // --- following are atomic methods from IList, no need to override
  // public boolean isFirst( E aItem );
  // public boolean isLast( E aItem );
  // public boolean isInRange( int aIndex );
  // ---

  @Override
  public IListBasicEdit<E> copyTo( IListBasicEdit<E> aDest ) {
    IListBasicEdit<E> dest = aDest;
    lock.readLock().lock();
    if( dest == null ) {
      dest = new ElemArrayList<>( size() );
    }
    try {
      dest.addAll( this );
      return dest;
    }
    finally {
      lock.readLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    lock.readLock().lock();
    try {
      return source.iterator();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsSynchronizedCollectionTag
  //

  @Override
  public ReentrantReadWriteLock getLockObject() {
    return lock;
  }

  @Override
  public ITsCollection<E> getSourceCollection() {
    return source;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object obj ) {
    lock.readLock().lock();
    try {
      return source.equals( obj );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int hashCode() {
    lock.readLock().lock();
    try {
      return source.hashCode();
    }
    finally {
      lock.readLock().unlock();
    }
  }

}
