package org.toxsoft.core.tslib.coll.synch;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListBasicEdit;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsSynchronizedCollectionTag;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Класс потоко-безопасной оболочки над НЕредактируемым списком.
 *
 * @author goga
 * @version $id$
 * @param <E> - тип элементов списка
 * @param <L> - тип НЕредактируемого списка-источника
 */
public class SynchronizedList<E, L extends IList<E>>
    implements IList<E>, ITsSynchronizedCollectionTag<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  protected final ReentrantReadWriteLock lock;
  protected final L                      source;

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом.
   *
   * @param aSource L - список - источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SynchronizedList( L aSource ) {
    this( aSource, new ReentrantReadWriteLock() );
  }

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом с указанием блокировки.
   *
   * @param aSource L - список - источник
   * @param aLock {@link ReentrantReadWriteLock} - блокировка списка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SynchronizedList( L aSource, ReentrantReadWriteLock aLock ) {
    TsNullArgumentRtException.checkNulls( aSource, aLock );
    source = aSource;
    lock = aLock;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IList<E>
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
  // Реализация интерфейса ITsSynchronizedCollectionTag
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
