package org.toxsoft.tslib.coll.synch;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IMapEdit;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.basis.ITsSynchronizedCollectionTag;
import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.coll.primtypes.*;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Потоко-безопасная оболочка над редактируемой картой отображения {@link IMapEdit}.
 *
 * @author goga
 * @version $id$
 * @param <E> - тип хранимых элементов, значений в карте
 */
public class SynchronizedStringMap<E>
    implements IStringMapEdit<E>, ITsSynchronizedCollectionTag<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final IStringList            synchKeys;
  protected final IList<E>               synchValues;
  protected final IStringMapEdit<E>      source;

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом.
   *
   * @param aSource IStringMapEdit&lt;E&gt; - карта - источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SynchronizedStringMap( IStringMapEdit<E> aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом с указанием блокировки.
   *
   * @param aSource IStringMapEdit&lt;E&gt; - карта - источник
   * @param aLock {@link ReentrantReadWriteLock} - блокировка карты
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SynchronizedStringMap( IStringMapEdit<E> aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
    synchKeys = new SynchronizedStringList<>( source.keys(), lock );
    synchValues = new SynchronizedList<>( source.values(), lock );
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
  // Реализация интерфейса IStringMapEdit
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
