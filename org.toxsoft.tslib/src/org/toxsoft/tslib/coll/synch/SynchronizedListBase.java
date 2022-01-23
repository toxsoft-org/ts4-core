package org.toxsoft.tslib.coll.synch;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.tslib.coll.IListBasicEdit;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.utils.errors.TsErrorUtils;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Служебный базовый класс потоко-безопасной оболочки над редактируемым списком.
 *
 * @author goga
 * @param <E> - тип элементов списка
 * @param <L> - (служебный) тип редактируемого списка-источника
 */
class SynchronizedListBase<E, L extends IListBasicEdit<E>>
    extends SynchronizedList<E, L>
    implements IListBasicEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом.
   *
   * @param aSource L - список - источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected SynchronizedListBase( L aSource ) {
    super( aSource );
  }

  /**
   * Создает оболочку над aSource с потоко-безопасным доступом с указанием блокировки.
   *
   * @param aSource L - список - источник
   * @param aLock {@link ReentrantReadWriteLock} - блокировка списка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  protected SynchronizedListBase( L aSource, ReentrantReadWriteLock aLock ) {
    super( aSource, aLock );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IListBasicEdit
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
