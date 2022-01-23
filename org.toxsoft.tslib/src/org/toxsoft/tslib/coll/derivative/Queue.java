package org.toxsoft.tslib.coll.derivative;

import static org.toxsoft.tslib.coll.derivative.ITsResources.*;

import java.io.Serializable;
import java.util.Iterator;

import org.toxsoft.tslib.coll.IListBasicEdit;
import org.toxsoft.tslib.coll.impl.ElemLinkedList;
import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация очереди, как оболочки над редактируемым списком.
 *
 * @author hazard157
 * @param <E> - тип хранимых элементов
 */
public class Queue<E>
    implements IQueue<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Исходный список, содержащий элементы очереди.
   */
  private final IListBasicEdit<E> source;

  /**
   * Максимальный размер очереди, -1 означает отсутствие ограничения на размер.
   */
  private final int maxSize;

  /**
   * Основной конструктор - создает очередь как оболочку над редактируемым cписком.
   * <p>
   * Для того, чтобы создать очередь без ограничения размера, следует указать -1 в качестве значения максимального
   * размера aMaxSize.
   *
   * @param aSourceList IListBasicEdit&lt;E&gt; - исходный список
   * @param aMaxSize int - максимальное количество элементов в очереди, или -1
   * @throws TsNullArgumentRtException аргумент = null
   */
  public Queue( IListBasicEdit<E> aSourceList, int aMaxSize ) {
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
  public Queue() {
    this( new ElemLinkedList<>(), -1 );
  }

  /**
   * Creates restricted queue based on linked list.
   *
   * @param aMaxSize int - maximum number of elements in queue
   */
  public Queue( int aMaxSize ) {
    this( new ElemLinkedList<>(), aMaxSize );
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public boolean isEmpty() {
    return source.isEmpty();
  }

  @Override
  public int size() {
    return source.size();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public E get( int aIndex ) {
    return source.get( aIndex );
  }

  @Override
  public boolean hasElem( E aElem ) {
    return source.hasElem( aElem );
  }

  @Override
  public int indexOf( E aElem ) {
    return source.indexOf( aElem );
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    source.clear();
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
  // IQueue
  //

  @Override
  public boolean putTail( E aElem ) {
    if( isSizeRestricted() && size() >= maxSize ) {
      throw new TsIllegalStateRtException( MSG_ERR_QUEUE_FULL, Integer.valueOf( source.size() ) );
    }
    source.add( aElem );
    return true;
  }

  @Override
  public boolean offerTail( E aElem ) {
    if( isSizeRestricted() && size() >= maxSize ) {
      return false;
    }
    source.add( aElem );
    return true;
  }

  @Override
  public E getHead() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_QUEUE_EMPTY );
    return source.removeByIndex( 0 );
  }

  @Override
  public E getHeadOrNull() {
    if( source.isEmpty() ) {
      return null;
    }
    return source.removeByIndex( 0 );
  }

  @Override
  public E peekHead() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_QUEUE_EMPTY );
    return source.get( 0 );
  }

  @Override
  public E peekHeadOrNull() {
    if( source.isEmpty() ) {
      return null;
    }
    return source.get( 0 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IQueue) ) {
      return false;
    }
    return source.equals( obj );
  }

  @Override
  public int hashCode() {
    return source.hashCode();
  }

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

}
