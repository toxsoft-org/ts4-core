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
 * Реализация стека, как оболочки над редактируемым списком.
 * <p>
 * Самый первый (самый глубокий) элемент имеет индекс 0, а вершина стека - индекс {@link #size()}-1. Элементы по индексу
 * можно получить методом {@link #get(int)}.
 *
 * @author goga
 * @version $id$
 * @param <E> - тип хранимых элементов
 */
class Stack<E>
    implements IStack<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Исходный список, содержащий элементы стека.
   */
  private final IListBasicEdit<E> source;

  /**
   * Максимальный размер стека, -1 означает отсутствие ограничения на размер.
   */
  private final int maxSize;

  /**
   * Основной конструктор - создает стек как оболочку над редактируемым cписком.
   * <p>
   * Для того, чтобы создать стек без ограничения размера, следует указать -1 в качестве значения максимального размера
   * aMaxSize.
   *
   * @param aSourceList IListBasicEdit&lt;E&gt; - исходный список
   * @param aMaxSize int - максимальное количество элементов в очереди, или -1
   * @throws TsNullArgumentRtException аргумент = null
   */
  public Stack( IListBasicEdit<E> aSourceList, int aMaxSize ) {
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
  public Stack() {
    this( new ElemLinkedList<>(), -1 );
  }

  /**
   * Creates restricted stack based on linked list.
   *
   * @param aMaxSize int - maximum number of elements in queue
   */
  public Stack( int aMaxSize ) {
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
  // ITsClearable
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
  // IStack
  //

  @Override
  public void push( E aElem ) {
    if( isSizeRestricted() && size() >= maxSize ) {
      throw new TsIllegalStateRtException( MSG_ERR_STACK_FULL, Integer.valueOf( source.size() ) );
    }
    source.add( aElem );
  }

  @Override
  public E peek() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_STACK_EMPTY );
    return source.get( source.size() - 1 );
  }

  @Override
  public E peekOrNull() {
    if( isEmpty() ) {
      return null;
    }
    return source.get( source.size() - 1 );
  }

  @Override
  public E pop() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_STACK_EMPTY );
    return source.removeByIndex( source.size() - 1 );
  }

  @Override
  public E popOrNull() {
    if( isEmpty() ) {
      return null;
    }
    return source.removeByIndex( source.size() - 1 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IStack) ) {
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
