package org.toxsoft.core.tslib.coll.synch;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over an editable map {@link IMapEdit}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public class SynchronizedMap<K, E>
    implements IMapEdit<K, E>, ITsSynchronizedCollectionWrapper<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final IList<K>               synchKeys;
  protected final IList<E>               synchValues;
  protected final IMapEdit<K, E>         source;

  /**
   * Constructor.
   *
   * @param aSource {@link IMapEdit} - the wrapped map
   * @param aLock {@link ReentrantReadWriteLock} - the lock to be used for synchronization
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedMap( IMapEdit<K, E> aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
    synchKeys = new SynchronizedList<>( source.keys(), lock );
    synchValues = new SynchronizedList<>( source.values(), lock );
  }

  /**
   * Constructor.
   * <p>
   * Internally creates the new instance of {@link ReentrantReadWriteLock}.
   *
   * @param aSource {@link IMapEdit} - the wrapped map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedMap( IMapEdit<K, E> aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

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

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public E getByKey( K aKey ) {
    lock.readLock().lock();
    try {
      return source.getByKey( aKey );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E findByKey( K aKey ) {
    lock.readLock().lock();
    try {
      return source.findByKey( aKey );
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
  public boolean hasKey( K aKey ) {
    lock.readLock().lock();
    try {
      return source.hasKey( aKey );
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
  public IList<K> keys() {
    lock.readLock().lock();
    try {
      return synchKeys;
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
  public IList<E> values() {
    lock.readLock().lock();
    try {
      return synchValues;
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Iterator<E> iterator() {
    lock.readLock().lock();
    try {
      return synchValues.iterator();
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public IMapEdit<K, E> copyTo( IMapEdit<K, E> aDest ) {
    lock.readLock().lock();
    try {
      IMapEdit<K, E> dest = aDest;
      if( dest == null ) {
        int estOrder = TsCollectionsUtils.estimateOrder( size() );
        int bucksCount = TsCollectionsUtils.getMapBucketsCount( estOrder );
        int bindleCapacity = TsCollectionsUtils.getListInitialCapacity( estOrder );
        dest = new ElemMap<>( bucksCount, bindleCapacity );
      }
      dest.putAll( this );
      return dest;
    }
    finally {
      lock.readLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
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

  @Override
  public E put( K aKey, E aElem ) {
    lock.writeLock().lock();
    try {
      return source.put( aKey, aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void putAll( IMap<K, ? extends E> aSrc ) {
    lock.writeLock().lock();
    try {
      source.putAll( aSrc );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void setAll( IMap<K, ? extends E> aSrc ) {
    lock.writeLock().lock();
    try {
      source.setAll( aSrc );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E removeByKey( K aKey ) {
    lock.writeLock().lock();
    try {
      return source.removeByKey( aKey );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsSynchronizedCollectionWrapper
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
  // Object
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
