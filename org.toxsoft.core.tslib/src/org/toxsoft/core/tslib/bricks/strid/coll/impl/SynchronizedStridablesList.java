package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over an editable map {@link IStridablesListEdit}.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public class SynchronizedStridablesList<E extends IStridable>
    implements IStridablesListEdit<E>, ITsSynchronizedCollectionWrapper<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final IStringList            synchKeys;
  protected final IList<E>               synchValues;
  protected final IStridablesListEdit<E> source;

  /**
   * Constructor.
   * <p>
   * Creates new instance of the lock for synchronization.
   *
   * @param aSource {@link IStridablesListEdit} - the wrapped map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedStridablesList( IStridablesListEdit<E> aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  /**
   * Constructor.
   *
   * @param aSource {@link IStridablesListEdit} - the wrapped map
   * @param aLock {@link ReentrantReadWriteLock} - the lock to be used for synchronization
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedStridablesList( IStridablesListEdit<E> aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
    synchKeys = new SynchronizedStringList<>( source.keys(), lock );
    synchValues = new SynchronizedList<>( source.values(), lock );
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

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public E getByKey( String aKey ) {
    lock.readLock().lock();
    try {
      return source.getByKey( aKey );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public E findByKey( String aKey ) {
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
  public boolean hasKey( String aKey ) {
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
  public IStringList keys() {
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

  // ------------------------------------------------------------------------------------
  // IStridablesListEdit
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
  public E put( String aKey, E aElem ) {
    lock.writeLock().lock();
    try {
      return source.put( aKey, aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void putAll( IStringMap<? extends E> aSrc ) {
    lock.writeLock().lock();
    try {
      source.putAll( aSrc );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void setAll( IStringMap<? extends E> aSrc ) {
    lock.writeLock().lock();
    try {
      source.setAll( aSrc );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E removeByKey( String aKey ) {
    lock.writeLock().lock();
    try {
      return source.removeByKey( aKey );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public int put( E aElem ) {
    lock.writeLock().lock();
    try {
      return source.put( aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E replace( String aId, E aElem ) {
    lock.writeLock().lock();
    try {
      return source.replace( aId, aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E removeById( String aId ) {
    lock.writeLock().lock();
    try {
      return source.removeById( aId );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public IStringList ids() {
    return keys();
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
  public int add( E aElem ) {
    lock.writeLock().lock();
    try {
      return source.add( aElem );
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
