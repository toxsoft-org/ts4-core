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
 * Потоко-безопасная реализация стека, как оболочки над редактируемым списком.
 *
 * @author goga
 * @version $id$
 * @param <E> - тип хранимых элементов
 */
class SynchronizedStack<E>
    implements IStack<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Блокировка доступа к исходному списку.
   */
  private final transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * Исходный список, содержащий элементы стека.
   */
  private final IListBasicEdit<E> source;

  /**
   * Максимальный размер стека, -1 означает отсутствие ограничения на размер.
   */
  private final int maxSize;

  /**
   * Создает стек как оболочку над редактируемым cписком.
   * <p>
   * Для того, чтобы создать стек без ограничения размера, следует указать -1 в качестве значения максимального размера
   * aMaxSize.
   *
   * @param aSourceList IListBasicEdit&lt;E&gt; - исходный список
   * @param aMaxSize int - максимальное количество элементов в стеке, или -1
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SynchronizedStack( IListBasicEdit<E> aSourceList, int aMaxSize ) {
    source = TsNullArgumentRtException.checkNull( aSourceList );
    if( aMaxSize < 0 ) {
      maxSize = -1;
    }
    else {
      maxSize = aMaxSize;
    }
  }

  /**
   * Creates unrestricted stack based on linked list.
   */
  public SynchronizedStack() {
    this( new ElemLinkedList<>(), -1 );
  }

  /**
   * Creates restricted stack based on linked list.
   *
   * @param aMaxSize int - maximum number of elements in queue
   */
  public SynchronizedStack( int aMaxSize ) {
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
  // ITsClearable
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
  // ITsSizeRestrictableCollection
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
  // IStack
  //

  @Override
  public void push( E aElem ) {
    lock.writeLock().lock();
    try {
      if( isSizeRestricted() && size() >= maxSize ) {
        throw new TsIllegalStateRtException( MSG_ERR_STACK_FULL, Integer.valueOf( source.size() ) );
      }
      source.add( aElem );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peek() {
    lock.writeLock().lock();
    try {
      TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_STACK_EMPTY );
      return source.get( 0 );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E peekOrNull() {
    lock.writeLock().lock();
    try {
      if( isEmpty() ) {
        return null;
      }
      return source.get( 0 );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E pop() {
    lock.writeLock().lock();
    try {
      TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_STACK_EMPTY );
      return source.removeByIndex( 0 );
    }
    finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public E popOrNull() {
    lock.writeLock().lock();
    try {
      if( isEmpty() ) {
        return null;
      }
      return source.removeByIndex( 0 );
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
