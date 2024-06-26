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
 * Thread-safe wrapper over an editable map {@link IStringMapEdit}.
 *
 * @author hazard157
 * @version $id$
 * @param <E> - the type of mapped values
 */
public class SynchronizedStringMap<E>
    implements IStringMapEdit<E>, ITsSynchronizedCollectionWrapper<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final IStringList            synchKeys;
  protected final IList<E>               synchValues;
  protected final IStringMapEdit<E>      source;

  /**
   * Constructor.
   *
   * @param aSource {@link IMapEdit} - the wrapped map
   * @param aLock {@link ReentrantReadWriteLock} - the lock to be used for synchronization
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedStringMap( IStringMapEdit<E> aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
    synchKeys = new SynchronizedStringList<>( source.keys(), lock );
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
  public SynchronizedStringMap( IStringMapEdit<E> aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  // ------------------------------------------------------------------------------------
  // IStringMap
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
    return synchKeys;
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
  public E[] toArray( E[] aSrcArray ) {
    lock.readLock().lock();
    try {
      return source.toArray( aSrcArray );
    }
    finally {
      lock.readLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // IStringMapEdit
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
  public Iterator<E> iterator() {
    lock.writeLock().lock();
    try {
      return synchValues.iterator();
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Заменяет содержимое карты содержимым карты-аргумента.
   *
   * @param aSrc {@link IStringMap}&lt;? extends E&gt; - новое содержимое
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void replaceAll( IStringMap<? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    lock.writeLock().lock();
    try {
      source.clear();
      source.putAll( aSrc );
    }
    finally {
      lock.writeLock().unlock();
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
