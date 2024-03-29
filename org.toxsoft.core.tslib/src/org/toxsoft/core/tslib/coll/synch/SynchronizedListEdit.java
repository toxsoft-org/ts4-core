package org.toxsoft.core.tslib.coll.synch;

import java.util.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Потоко-безопасная оболочка над редактируемым списком {@link IListEdit}.
 *
 * @author hazard157
 * @version $id$
 * @param <E> - тип элементов списка
 */
public class SynchronizedListEdit<E>
    extends SynchronizedListBase<E, IListEdit<E>>
    implements IListEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Создает соболочку над aSource с потоко-безопасным доступом.
   *
   * @param aSource IListEdit&lt;E&gt; - список - источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SynchronizedListEdit( IListEdit<E> aSource ) {
    super( aSource );
  }

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом с указанием блокировки.
   *
   * @param aSource IListEdit&lt;E&gt; - список - источник
   * @param aLock {@link ReentrantReadWriteLock} - блокировка списка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SynchronizedListEdit( IListEdit<E> aSource, ReentrantReadWriteLock aLock ) {
    super( aSource, aLock );
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
