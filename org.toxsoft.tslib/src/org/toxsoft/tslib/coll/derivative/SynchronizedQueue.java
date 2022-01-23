package org.toxsoft.tslib.coll.derivative;

import static org.toxsoft.tslib.coll.derivative.ITsResources.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.toxsoft.tslib.coll.IListBasicEdit;
import org.toxsoft.tslib.coll.impl.ElemLinkedList;
import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Потоко-безопасная реализация очереди, как оболочки над редактируемым списком.
 *
 * @author hazard157
 * @param <E> - тип хранимых элементов
 */
class SynchronizedQueue<E>
    implements IQueue<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Блокировка доступа к исходному списку.
   */
  private final transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * Исходный список, содержащий элементы очереди.
   */
  private final IListBasicEdit<E> source;

  /**
   * Максимальный размер очереди, -1 означает отсутствие ограничения на размер.
   */
  private final int maxSize;

  /**
   * Создает очередь как оболочку над редактируемым cписком.
   * <p>
   * Для того, чтобы создать очередь без ограничения размера, следует указать -1 в качестве значения максимального
   * размера aMaxSize.
   *
   * @param aSourceList IListBasicEdit&lt;E&gt; - исходный список
   * @param aMaxSize int - максимальное количество элементов в очереди, или -1
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SynchronizedQueue( IListBasicEdit<E> aSourceList, int aMaxSize ) {
    source = TsNullArgumentRtException.checkNull( aSourceList );
    if( aMaxSize < 0 ) {
      maxSize = -1;
    }
    else {
      maxSize = aMaxSize;
    }
  }

  /**
   * Creates unrestricted queue based on linked list.
   */
  public SynchronizedQueue() {
    this( new ElemLinkedList<>(), -1 );
  }

  /**
   * Creates restricted queue based on linked list.
   *
   * @param aMaxSize int - maximum number of elements in queue
   */
  public SynchronizedQueue( int aMaxSize ) {
    this( new ElemLinkedList<>(), aMaxSize );
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    throw new TsUnderDevelopmentRtException();
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

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

  // ------------------------------------------------------------------------------------
  // ITsCollection
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
  // Реализация интерфейса ITsClearableCollection
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

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSizeRestrictableCollection
  //

  @Override
  public boolean isSizeRestricted() {
    return maxSize != -1;
  }

  @Override
  public int maxSize() {
    return maxSize;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IQueue
  //

  @Override
  public boolean putTail( E aElem ) {
    lock.writeLock().lock();
    try {
      if( isSizeRestricted() && size() >= maxSize ) {
        throw new TsIllegalStateRtException( MSG_ERR_QUEUE_FULL, Integer.valueOf( source.size() ) );
      }
      source.add( aElem );
      return true;
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public boolean offerTail( E aElem ) {
    lock.writeLock().lock();
    try {
      if( isSizeRestricted() && size() >= maxSize ) {
        return false;
      }
      source.add( aElem );
      return true;
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E getHead() {
    lock.writeLock().lock();
    try {
      TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_QUEUE_EMPTY );
      return source.removeByIndex( 0 );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E getHeadOrNull() {
    lock.writeLock().lock();
    try {
      if( source.isEmpty() ) {
        return null;
      }
      return source.removeByIndex( 0 );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peekHead() {
    lock.writeLock().lock();
    try {
      TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_QUEUE_EMPTY );
      return source.get( 0 );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peekHeadOrNull() {
    lock.writeLock().lock();
    try {
      if( source.isEmpty() ) {
        return null;
      }
      return source.get( 0 );
    }
    finally {
      lock.writeLock().unlock();
    }
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
