package org.toxsoft.tslib.coll.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;

// FIXME TRANSLATE

/**
 * Список элементов, реализованный односторонне связанным списоком.
 * <p>
 * Реализация обспечивает быстрое O(1) выполнение следующих операции:
 * <ul>
 * <li>определение размера списка - {@link #size()}, {@link #isEmpty()};</li>
 * <li>добавление в конец списка - методы add();</li>
 * <li>добавление в конец или начало списка - insert(0,x) и insert(size(),x);</li>
 * <li>очистка списка - {@link #clear()};</li>
 * <li>работа итератора.</li>
 * </ul>
 * Медленно (последовательным перебором) O(n) выполняется все операции поска. Все перечисленные ниже операции содержат
 * медленный поиск, но при этом сами операции быстрые - O(1):
 * <ul>
 * <li>доступ к элементам по индексу - {@link #get(int)};</li>
 * <li>поиск элемента - {@link #hasElem(Object)}, {@link #indexOf(Object)};</li>
 * <li>удаление элементов - методы remove();</li>
 * <li>изменение элемента - {@link #set(int, Object)};</li>
 * <li>вставка элементов - методы insert().</li>
 * </ul>
 *
 * @author hazard157
 * @param <E> - тип элементов списка
 */
public class ElemLinkedList<E>
    implements IListEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Linked list items.
   *
   * @author hazard157
   */
  private final class Item
      implements Serializable {

    private static final long serialVersionUID = 157157L;

    E    e;
    Item next;

    Item( E aElem ) {
      TsNullArgumentRtException.checkNull( aElem );
      e = aElem;
      next = null;
    }
  }

  /**
   * First item of the linked list.<br>
   * In the empty string = <code>null</code>, in list with one item = {@link #last}.
   */
  Item first = null;

  /**
   * Last item of the linked list.<br>
   * In the empty string = <code>null</code>, in list with one item = {@link #first}.
   */
  Item last = null;

  /**
   * Размер списка - количество количество элементов {@link Item} в связанном списке.
   */
  protected int size = 0;

  /**
   * Счетчик количества изменений, используемый для определения конкуррентного изменения списка.
   */
  protected int changeCount = 0;

  /**
   * Создает пустой список.
   */
  public ElemLinkedList() {
    // nop
  }

  /**
   * Создает список с начальным содержимым набора или массива aElems.
   *
   * @param aElems E[] - элементы списка (набор или массив)
   * @throws TsNullArgumentRtException aElems = null
   */
  @SuppressWarnings( "unchecked" )
  public ElemLinkedList( E... aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    for( int i = 0, n = aElems.length; i < n; i++ ) {
      add( aElems[i] );
    }
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link IList} - список-источник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ElemLinkedList( IList<E> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    addAll( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Проверят, что ссылка и любой эелемнт коллекции не null.
   * <p>
   * Используется перед добавлением коллекции для обспечения атомарости операции добавления.
   *
   * @param aColl Collection&lt;E&gt; - проевряемая коллекция
   * @throws TsNullArgumentRtException aColl = null
   * @throws TsNullArgumentRtException любой элемент = null
   */
  private final void checkCollection( Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( E e : aColl ) {
      TsNullArgumentRtException.checkNull( e );
    }
  }

  /**
   * Удалет элемент спсика aItem и увеличивает счетчик {@link #changeCount}.
   * <p>
   * Метод не производит никаких проверок!
   *
   * @param aItem Item - удаляемый элемент списка
   * @param aPrev Item - предыдущий элемент списка
   */
  private final void removeItem( Item aItem, Item aPrev ) {
    if( aItem == first ) {
      if( first == last ) {
        first = last = null;
      }
      else {
        first = first.next;
      }
    }
    else {
      aPrev.next = aItem.next;
      if( aItem == last ) {
        last = aPrev;
      }
    }
    ++changeCount;
    --size;
  }

  // ------------------------------------------------------------------------------------
  // IList<E>
  //

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean hasElem( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    for( Item item = first; item != null; item = item.next ) {
      if( item.e.equals( aElem ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOf( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    Item item = first;
    for( int i = 0; i < size; i++ ) {
      if( item.e.equals( aElem ) ) {
        return i;
      }
      item = item.next;
    }
    return -1;
  }

  @Override
  public E get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 );
    TsIllegalArgumentRtException.checkTrue( aIndex >= size );
    if( aIndex == size - 1 ) { // оптимизация доступа к последнему элементу
      return last.e;
    }
    Item item = first;
    for( int i = 0; i < aIndex; i++ ) {
      item = item.next;
    }
    return item.e;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E[] toArray( E[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    int index = 0;
    E[] array = aSrcArray;
    if( aSrcArray.length < size ) { // убадимся, что массив достаточной длины
      array = (E[])Array.newInstance( aSrcArray.getClass().getComponentType(), size );
    }
    for( Item item = first; item != null; item = item.next ) {
      aSrcArray[index++] = item.e;
    }
    // этот цикл только для случая array == aSrcArray
    for( ; index < aSrcArray.length; index++ ) {
      aSrcArray[index] = null;
    }
    return array;
  }

  @Override
  public Object[] toArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
    }
    Object[] result = new Object[size];
    Iterator<E> iter = iterator();
    int i = 0;
    while( iter.hasNext() ) {
      result[i++] = iter.next();
    }
    return result;
  }

  // --------------------------------------------------------------------------
  // Iterable<E>
  //

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {

      Item              item                = first;
      private final int expectedChnageCount = changeCount;

      private void checkForConcurrentModification() {
        if( expectedChnageCount != changeCount ) {
          throw new ConcurrentModificationException();
        }
      }

      @Override
      public boolean hasNext() {
        checkForConcurrentModification();
        return item != null;
      }

      @Override
      public E next() {
        if( item != null ) {
          E elem = item.e;
          item = item.next;
          return elem;
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

  // ------------------------------------------------------------------------------------
  // IListBasicEdit<E>
  //

  @Override
  public int add( E aElem ) {
    Item newItem = new Item( aElem );
    if( last == null ) {
      first = newItem;
    }
    else {
      last.next = newItem;
    }
    last = newItem;
    ++changeCount;
    return size++;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void addAll( E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      add( aArray[i] );
    }
  }

  @Override
  public void addAll( ITsCollection<E> aElemList ) {
    TsNullArgumentRtException.checkNull( aElemList );
    if( aElemList instanceof ITsFastIndexListTag ) {
      ITsFastIndexListTag<E> src = (ITsFastIndexListTag<E>)aElemList;
      for( int i = 0, n = src.size(); i < n; i++ ) {
        add( src.get( i ) );
      }
    }
    else {
      for( E e : aElemList ) {
        add( e );
      }
    }
  }

  @Override
  public void addAll( Collection<E> aElemColl ) {
    checkCollection( aElemColl );
    for( E e : aElemColl ) {
      add( e );
    }
  }

  @Override
  public void clear() {
    size = 0;
    first = last = null;
  }

  @Override
  public int remove( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    int index = 0;
    for( Item item = first, prev = first; item != null; prev = item, item = item.next ) {
      if( item.e.equals( aElem ) ) {
        removeItem( item, prev );
        return index;
      }
      ++index;
    }
    return -1;
  }

  @Override
  public E removeByIndex( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 );
    TsIllegalArgumentRtException.checkTrue( aIndex >= size );
    Item item = first, prev = first;
    for( int i = 0; i < aIndex; i++ ) {
      prev = item;
      item = item.next;
    }
    removeItem( item, prev );
    return item.e;
  }

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    TsIllegalArgumentRtException.checkTrue( aCount < 0 || aCount > (size - aIndex) );
    // OPTIMIZE написать оптимизированную версию
    for( int i = 0; i < aCount; i++ ) {
      removeByIndex( aIndex );
    }
  }

  @Override
  public void setAll( ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    clear();
    if( aColl instanceof ITsFastIndexListTag ) {
      ITsFastIndexListTag<E> coll = (ITsFastIndexListTag<E>)aColl;
      for( int i = 0, n = coll.size(); i < n; i++ ) {
        add( coll.get( i ) );
      }
    }
    else {
      for( E e : aColl ) {
        add( e );
      }
    }
  }

  @Override
  public void setAll( Collection<E> aColl ) {
    TsErrorUtils.checkCollectionArg( aColl );
    clear();
    for( E e : aColl ) {
      add( e );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void setAll( E... aElems ) {
    TsErrorUtils.checkArrayArg( aElems );
    clear();
    for( E e : aElems ) {
      add( e );
    }
  }

  // ------------------------------------------------------------------------------------
  // IListEdit
  //

  @Override
  public E set( int aIndex, E aElem ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    TsNullArgumentRtException.checkNull( aElem );
    Item item = first;
    for( int i = 0; i < aIndex; i++ ) {
      item = item.next;
    }
    E oldElem = item.e;
    item.e = aElem;
    return oldElem;
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 );
    TsIllegalArgumentRtException.checkTrue( aIndex > size );
    Item newItem = new Item( aElem );
    if( first == null ) { // добавление в пустой список, aIndex = 0
      first = last = newItem;
      ++size;
      ++changeCount;
      return;
    }
    if( aIndex == 0 ) { // не-пустой список, вставка в начало
      newItem.next = first;
      first = newItem;
      ++size;
      ++changeCount;
      return;
    }
    if( aIndex == size ) { // не-пустой список, вставка в конец
      last.next = newItem;
      last = newItem;
      ++size;
      ++changeCount;
      return;
    }
    Item item = first, prev = first;
    for( int i = 0; i < aIndex; i++ ) {
      prev = item;
      item = item.next;
    }
    prev.next = newItem;
    newItem.next = item;
    ++size;
    ++changeCount;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void insertAll( int aIndex, E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    // OPTIMIZE написать оптимизированную версию
    for( int i = aArray.length - 1; i >= 0; i-- ) {
      insert( aIndex, aArray[i] );
    }
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( aColl.isEmpty() ) {
      return;
    }
    // OPTIMIZE написать оптимизированную версию
    if( aColl instanceof ITsFastIndexListTag ) {
      ITsFastIndexListTag<E> coll = (ITsFastIndexListTag<E>)aColl;
      for( int i = coll.size() - 1; i >= 0; i-- ) {
        insert( aIndex, coll.get( i ) );
      }
    }
    else {
      IListEdit<E> tmpList = new ElemArrayList<>( aColl.size() );
      for( E e : aColl ) {
        tmpList.insert( 0, e );
      }
      insertAll( aIndex, tmpList );
    }
  }

  @Override
  public void insertAll( int aIndex, Collection<E> aColl ) {
    checkCollection( aColl );
    // OPTIMIZE написать оптимизированную версию
    IListEdit<E> list = new ElemArrayList<>( aColl );
    insertAll( aIndex, list );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IList) ) {
      return false;
    }
    return TsCollectionsUtils.isListsEqual( this, (IList)obj );
  }

  @Override
  public int hashCode() {
    return TsCollectionsUtils.calcListHashCode( this );
  }

}
