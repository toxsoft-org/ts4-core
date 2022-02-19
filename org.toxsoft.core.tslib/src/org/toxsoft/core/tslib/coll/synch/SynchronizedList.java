package org.toxsoft.core.tslib.coll.synch;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Класс потоко-безопасной оболочки над НЕредактируемым списком.
 *
 * @author goga
 * @param <E> - тип элементов списка
 * @param <L> - тип НЕредактируемого списка-источника
 */
public class SynchronizedList<E, L extends IList<E>>
    implements IList<E>, ITsSynchronizedCollectionWrapper<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final L                      source;

  /**
   * Constructor with all invariants.
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
  public IListBasicEdit<E> copyTo( IListBasicEdit<E> aDest ) {
    IListBasicEdit<E> dest = aDest;
    if( dest == null ) {
      dest = new ElemArrayList<>( size() );
    }
    lock.readLock().lock();
    try {
      dest.addAll( this );
      return dest;
    }
    finally {
      lock.readLock().unlock();
    }
  }

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
