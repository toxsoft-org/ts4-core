package org.toxsoft.core.tslib.coll.impl;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility constants and methods for collections implementation.
 *
 * @author hazard157
 */
public final class TsCollectionsUtils {

  // ------------------------------------------------------------------------------------
  // Big collection optimization support
  //

  static final int MIN_ORDER = 1; // minimum scale value, tens of elements
  static final int MAX_ORDER = 6; // maximum scale value, millions of elements

  /**
   * An array of prime numbers to initialize the initial number of cells in the associative map collection hash tables.
   * <p>
   * Index of element is scale value - 1.
   */
  public static final int[] ORDER_BUCKETS_COUNT = { //
      3, 17, 157, 1009, 9973, 74923 //
  };

  /**
   * An array of numbers to initialize the initial capacity of linear list collection.
   * <p>
   * Index of element is scale value - 1.
   */
  public static final int[] ORDER_LIST_CAPACITY = { //
      16, 64, 512, 4096, 16384, 65536 //
  };

  // FIXME TRANSLATE

  /**
   * Estimates the scale of the collection.
   * <p>
   * Коллекции tslib (а именно, {@link IList} и {@link IMap}), как и любые другие коллекции, деградируют свою
   * производительсноть при увеличении количества оэлементов в них. Прктически каждая реализация коллекции имеет
   * параметры конструктора, влияющие на их производительность. Для линейных коллекции (списков) - это начальный размер
   * внутреннего массива (или массивов), и вытекающий из этого параметра размер кдиновременного увеличения хранилища при
   * добавлении элементов. Для ассциативных коллекции - количество ячеек для хранения список элементов.
   * <p>
   * Параметры по умолчанию подобраны так, чтобы при минимальном потреблении памяти обеспечть максимальноую
   * производительность для коллекции с дестками сотнями элементов. Но для коллекции с тысячии и миллионами элементов
   * производительность оказывется чудовищно низкой. В таком случае, следует конфигурировать (задавать параметры)
   * коллекции до их создания.
   * <p>
   * Этот метод предназначен для облегчения подбора параметров создаваемых коллекции.
   * <p>
   * Использование:
   * <ul>
   * <li>исходя из задачи, прикинуть типовое количество элементов в коллекции;</li>
   * <li>передав типовое количество в качестве аргумента этого метода, получить числовую оценку порядка величины;</li>
   * <li>параметр карт возвращается методом {@link #getMapBucketsCount(int)}, где аргумент - порядок величнны;</li>
   * <li>параметр списокв возвращается методом {@link #getListInitialCapacity(int)}, где аргумент - порядок
   * величнны;</li>
   * <li>полученны параметр передать как аргумент конструктора коллекции.</li>
   * </ul>
   *
   * @param aCount int - количствно элементов
   * @return int - оценка масштаба в пределах о 1 до максимально допустимого
   */
  public static int estimateOrder( int aCount ) {
    int cieling = (int)Math.pow( 10, MIN_ORDER );
    for( int i = MIN_ORDER; i <= MAX_ORDER; i++ ) {
      if( aCount < cieling ) {
        return i;
      }
      cieling *= 10;
    }
    return MAX_ORDER;
  }

  /**
   * Возвращает оптимальное количество ячеек карты для порядка величины прогноpируемого количества элементов.
   * <p>
   * Аргмуент может иметь любое значене, для слишком меленких и больших порядков возвращается минимальное (максимальное)
   * оптимальое количество ячеек карты.
   *
   * @param aOrder int - порядок величины прогнозируемого количества элементов коллекции
   * @return int - оптимальое количество ячеек карты
   */
  public static int getMapBucketsCount( int aOrder ) {
    if( aOrder <= 0 ) {
      return ORDER_BUCKETS_COUNT[0];
    }
    if( aOrder >= MAX_ORDER ) {
      return ORDER_BUCKETS_COUNT[MAX_ORDER - 1];
    }
    return ORDER_BUCKETS_COUNT[aOrder - 1];
  }

  /**
   * Возвращает оптимальную начальную емкость списка для порядка величины прогноpируемого количества элементов.
   * <p>
   * Аргмуент может иметь любое значене, для слишком меленких и больших порядков возвращается минимальная (максимальная)
   * оптимальая начальная емкость списка.
   *
   * @param aOrder int - порядок величины прогнозируемого количества элементов коллекции
   * @return int - оптимальная начальная емкость списка
   */
  public static int getListInitialCapacity( int aOrder ) {
    if( aOrder <= 0 ) {
      return ORDER_LIST_CAPACITY[0];
    }
    if( aOrder >= MAX_ORDER ) {
      return ORDER_LIST_CAPACITY[MAX_ORDER - 1];
    }
    return ORDER_LIST_CAPACITY[aOrder - 1];
  }

  /**
   * Minimal count of hash-table cells in the maps.
   */
  public static final int MINIMUM_BUCKETS_COUNT = 5;

  /**
   * Maximal count of hash-table cells in the maps.
   */
  public static final int MAXIMUM_BUCKETS_COUNT = 1048583; // this is a prime number

  /**
   * Default count of hash-table cells in the maps.
   */
  public static final int DEFAULT_BUCKETS_COUNT = 17;

  /**
   * Miminal number of elements in bundles of list implementation.
   */
  public static final int MIN_BUNDLE_CAPACITY = 4;

  /**
   * Maximal number of elements in bundles of list implementation.
   */
  public static final int MAX_BUNDLE_CAPACITY = 268435456; // 2 ^ 28

  /**
   * Default number of elements in bundles of list implementation.
   */
  public static final int DEFAULT_BUNDLE_CAPACITY = 32;

  /**
   * Default length of internal array (capacity) of list array implementations.
   */
  public static final int DEFAULT_ARRAY_LIST_CAPACITY = 10;

  /**
   * Minimal length of internal array (capacity) of list array implementations.
   */
  public static final int MIN_ARRAY_LIST_CAPACITY = 4;

  /**
   * Returns string representation for {@link Object#toString()} of collection.
   *
   * @param aColl {@link ITsCollection} - collection with known number of elements
   * @return String - text representation of collection
   */
  @SuppressWarnings( "boxing" )
  public static String countableCollectionToString( ITsCollection<?> aColl ) {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for( Object o : aColl ) {
      sb.append( o.toString() );
      if( i < aColl.size() - 1 ) {
        sb.append( ", " ); //$NON-NLS-1$
      }
      ++i;
      if( i > 5 ) {
        break;
      }
    }
    if( aColl.size() > 5 ) {
      sb.append( "..." ); //$NON-NLS-1$
    }
    return String.format( "%s[%d] { %s }", aColl.getClass().getSimpleName(), aColl.size(), sb.toString() ); //$NON-NLS-1$
  }

  /**
   * Compares two lists.
   * <p>
   * Lists are equal when they have equal elements in the same order.
   * <p>
   * Designed to simplify {@link Object#equals(Object)} for different list implementations.
   *
   * @param aL1 {@link IList} - first list
   * @param aL2 {@link IList} - second list
   * @return boolean - <code>true</code> if lists are equal
   */
  @SuppressWarnings( "rawtypes" )
  public static boolean isListsEqual( IList aL1, IList aL2 ) {
    if( aL1.isEmpty() && aL2.isEmpty() ) {
      return true;
    }
    if( aL1.size() != aL2.size() ) {
      return false;
    }
    Iterator it1 = aL1.iterator();
    Iterator it2 = aL2.iterator();
    while( it1.hasNext() ) {
      if( !it1.next().equals( it2.next() ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Compares two maps.
   * <p>
   * Maps are equal when they have equal keys and elements in the same order of the keys.
   * <p>
   * Designed to simplify {@link Object#equals(Object)} for different map implementations.
   *
   * @param aM1 {@link IMap} - first map
   * @param aM2 {@link IMap} - second map
   * @return boolean - <code>true</code> if maps are equal
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public static boolean isMapsEqual( IMap aM1, IMap aM2 ) {
    if( aM1.isEmpty() && aM2.isEmpty() ) {
      return true;
    }
    if( aM1.size() != aM2.size() ) {
      return false;
    }
    if( !isListsEqual( aM1.keys(), aM2.keys() ) ) {
      return false;
    }
    for( Object k : aM1.keys() ) {
      if( aM1.getByKey( k ).equals( aM2.getByKey( k ) ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Compares two lists.
   * <p>
   * Designed to simplify {@link Object#equals(Object)} for different list implementations.
   *
   * @param aL1 {@link IIntList} - first list
   * @param aL2 {@link IIntList} - second list
   * @return boolean - <code>true</code> if lists are equal
   */
  public static boolean isIntListsEqual( IIntList aL1, IIntList aL2 ) {
    if( aL1.isEmpty() && aL2.isEmpty() ) {
      return true;
    }
    if( aL1.size() != aL2.size() ) {
      return false;
    }
    for( int i = 0, n = aL1.size(); i < n; i++ ) {
      if( aL1.getValue( i ) != aL2.getValue( i ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Compares two lists.
   * <p>
   * Designed to simplify {@link Object#equals(Object)} for different list implementations.
   *
   * @param aL1 {@link ILongList} - first list
   * @param aL2 {@link ILongList} - second list
   * @return boolean - <code>true</code> if lists are equal
   */
  public static boolean isLongListsEqual( ILongList aL1, ILongList aL2 ) {
    if( aL1.isEmpty() && aL2.isEmpty() ) {
      return true;
    }
    if( aL1.size() != aL2.size() ) {
      return false;
    }
    for( int i = 0, n = aL1.size(); i < n; i++ ) {
      if( aL1.getValue( i ) != aL2.getValue( i ) ) {
        return false;
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // collections logical operations
  //

  /**
   * Determines if two lists intersect.
   * <p>
   * Two lists intersect if they have at least one common element. Elements are compared using
   * {@link ObjectStreamException#equals(Object)} method. If any list is empty then method returns <code>false</code>.
   *
   * @param aL1 {@link IList} - one list
   * @param aL2 {@link IList} - another list
   * @return boolean <b>true</b> - lists intersect, they have at list one common element;<br>
   *         <b>false</b> - lists are totally different.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public static boolean intersects( IList<?> aL1, IList<?> aL2 ) {
    TsNullArgumentRtException.checkNulls( aL1, aL2 );
    if( aL1.size() == 0 || aL2.size() == 0 ) {
      return false;
    }
    IList l1 = aL1, l2 = aL2;
    // make sure that l2 is shorter, and l1 is a longer list
    if( aL1.size() < aL2.size() ) {
      l2 = aL1;
      l1 = aL2;
    }
    // check if l2 has any of the l1 element
    for( int i = 0, n = l2.size(); i < n; i++ ) {
      if( l1.hasElem( l2.get( i ) ) ) { // hasElem() is faster on the sorted list
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if two string lists intersect.
   * <p>
   * Two lists intersect if they have at least one common element. If any list is empty then method returns
   * <code>false</code>.
   *
   * @param aL1 IStringList - one list
   * @param aL2 IStringList - another list
   * @return boolean <b>true</b> - lists intersect, they have at list one common element;<br>
   *         <b>false</b> - lists are totally different.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean intersects( IStringList aL1, IStringList aL2 ) {
    TsNullArgumentRtException.checkNulls( aL1, aL2 );
    if( aL1.size() == 0 || aL2.size() == 0 ) {
      return false;
    }
    IStringList l1 = aL1, l2 = aL2;
    // make sure that l2 is shorter, and l1 is a longer list
    if( aL1.size() < aL2.size() ) {
      l2 = aL1;
      l1 = aL2;
    }
    // sort a long list for a quick search
    l1 = new SortedStringLinkedBundleList( l1 );
    for( int i = 0, n = l2.size(); i < n; i++ ) {
      if( l1.hasElem( l2.get( i ) ) ) { // hasElem() is faster on the sorted list
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if one string lists contains other one.
   * <p>
   * Returns <code>true</code> if lists are equal. If list <code>aL2</code> is empty then returns <code>false</code>.
   *
   * @param aL1 IStringList - one list
   * @param aL2 IStringList - another list
   * @return boolean <b>true</b> - list <code>aL1</code> contains all items of <code>aL2</code>;<br>
   *         <b>false</b> - there is at leas one element from <code>aL2</code> not in list <code>aL1</code>.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean contains( IStringList aL1, IStringList aL2 ) {
    TsNullArgumentRtException.checkNulls( aL1, aL2 );
    if( aL1.size() < aL2.size() ) {
      return false;
    }
    if( aL2.size() == 0 ) {
      return true;
    }
    IStringList l1 = new SortedStringLinkedBundleList( aL1 );
    for( int i = 0, n = aL2.size(); i < n; i++ ) {
      if( !l1.hasElem( aL2.get( i ) ) ) {
        return false;
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // collection math operations
  //

  /**
   * Creates the union of the lists without duplicate elements.
   * <p>
   * Even if any argument has duplicate elements, duplicates will be removed from the resulting list.
   * <p>
   * Always returns new instance of the editable list.
   *
   * @param <E> - element type of the collection
   * @param aL1 {@link IList}&lt;E&gt; - one list
   * @param aL2 {@link IList}&lt;E&gt; - other list
   * @return {@link IListEdit}&lt;E&gt; - an editable list with all different elements from both lists
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <E> IListEdit<E> union( IList<E> aL1, IList<E> aL2 ) {
    TsNullArgumentRtException.checkNulls( aL1, aL2 );
    int order = estimateOrder( aL1.size() + aL2.size() );
    IListEdit<E> ll = new ElemLinkedBundleList<>( getListInitialCapacity( order ), true );
    for( E e : aL1 ) {
      if( ll.hasElem( e ) ) {
        ll.add( e );
      }
    }
    for( E e : aL2 ) {
      if( ll.hasElem( e ) ) {
        ll.add( e );
      }
    }
    return ll;
  }

  /**
   * Creates the union of the string lists without duplicate elements.
   * <p>
   * Even if any argument has duplicate elements, duplicates will be removed from the resulting list.
   * <p>
   * Always returns new instance of the editable list.
   *
   * @param aL1 {@link IStringList} - one list
   * @param aL2 {@link IStringList} - other list
   * @return {@link IStringListEdit} - an editable list with all different elements from both lists
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IStringListEdit union( IStringList aL1, IStringList aL2 ) {
    TsNullArgumentRtException.checkNulls( aL1, aL2 );
    int order = estimateOrder( aL1.size() + aL2.size() );
    IStringListEdit ll = new StringLinkedBundleList( getListInitialCapacity( order ), true );
    for( String e : aL1 ) {
      if( ll.hasElem( e ) ) {
        ll.add( e );
      }
    }
    for( String e : aL2 ) {
      if( ll.hasElem( e ) ) {
        ll.add( e );
      }
    }
    return ll;
  }

  /**
   * Subtracts the values of aList2 from aList1,
   *
   * @param <T> - type of elements in lists
   * @param aList1 {@link IListEdit} - list to be edited
   * @param aList2 {@link IList} - elements to be subtracted
   * @return {@link IListEdit} - always aList1
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> IListEdit<T> subtract( IListEdit<T> aList1, IList<T> aList2 ) {
    TsNullArgumentRtException.checkNulls( aList1, aList2 );
    for( int i = 0, n = aList2.size(); i < n; i++ ) {
      T value = aList2.get( i );
      // remove all occurences of element from the list
      while( aList1.remove( value ) >= 0 ) {
        // nop
      }
    }
    return aList1;
  }

  /**
   * Subtracts the values of aList2 from aList1,
   *
   * @param aList1 {@link IStringListEdit} - list to be edited
   * @param aList2 {@link IStringList} - elements to be subtracted
   * @return {@link IStringListEdit} - always aList1
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IStringListEdit subtract( IStringListEdit aList1, IStringList aList2 ) {
    TsNullArgumentRtException.checkNulls( aList1, aList2 );
    for( int i = 0, n = aList2.size(); i < n; i++ ) {
      String value = aList2.get( i );
      // remove all occurences of element from the list
      while( aList1.remove( value ) >= 0 ) {
        // nop
      }
    }
    return aList1;
  }

  // ------------------------------------------------------------------------------------
  // Misc
  //

  /**
   * Finds element in list te be selected when selected element will be removed.
   * <p>
   * Useful for GUI. When selected item is removed from GUI lists, next item must be selected. If last item is removed
   * than previous item must became selected one. If the only item is removed, or if there is no selection (that is
   * <code>aSelToRemove</code> = <code>null</code>), than <code>null</code> will be returned. <code>null</code> means no
   * selecion in GUI.
   * <p>
   * If <code>aSelToRemove</code> is not in list then <code>null</code> will be returned.
   *
   * @param <E> - the type of elements in list
   * @param aList {@link IList} - the list
   * @param aSelToRemove &lt;E&gt; - selected element to be removed, may be <code>null</code>
   * @return &lt;E&gt; - element to be selected after remove or <code>null</code>
   * @throws TsNullArgumentRtException <code>aList</code> = <code>null</code>
   */
  public static <E> E findSelAfterRemove( IList<E> aList, E aSelToRemove ) {
    TsNullArgumentRtException.checkNull( aList );
    if( aSelToRemove == null || aList.isEmpty() ) {
      return null;
    }
    int index = aList.indexOf( aSelToRemove );
    if( index < 0 || aList.size() == 1 ) {
      return null;
    }
    // here: list has at least 2 items
    if( index == aList.size() - 1 ) {
      return aList.get( index - 1 );
    }
    return aList.get( index + 1 );
  }

  /**
   * Finds element index in list te be selected when selected element will be removed.
   * <p>
   * This is the same emthod as {@link #findSelAfterRemove(IList, Object)}, where indexes are used instead of references
   * to the elemnts. Index -1 is equivalent of the <code>null</code> reference.
   * <p>
   * Invalid indexes is considered as -1.
   * <p>
   * Please note that method returned value is considered to be the index <b>after</b> element removal.
   *
   * @param aList {@link IList} - the list
   * @param aSelIndex int - index of the selected element to be removed or -1
   * @return int - index of the element to be selected after remove or -1
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static int findSelIndexAfterRemove( IList<?> aList, int aSelIndex ) {
    TsNullArgumentRtException.checkNull( aList );
    if( aSelIndex < 0 || aSelIndex >= aList.size() || aList.size() <= 1 ) {
      return -1;
    }
    // here: list has at least 2 items
    if( aSelIndex == aList.size() - 1 ) {
      return aSelIndex - 1;
    }
    return aSelIndex;

  }

  // ------------------------------------------------------------------------------------
  // hash code calculation support
  //

  /**
   * Calculates list's hash-code.
   * <p>
   * Designed to simplify {@link Object#hashCode()} for different list implementations.
   *
   * @param aL {@link IList} - list
   * @return int - hash code
   */
  @SuppressWarnings( "rawtypes" )
  public static int calcListHashCode( IList aL ) {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( Object o : aL ) {
      result = TsLibUtils.PRIME * result + o.hashCode();
    }
    return result;
  }

  /**
   * Calculates map's hash-code.
   * <p>
   * Designed to simplify {@link Object#hashCode()} for different map implementations.
   *
   * @param aMap {@link IMap} - map
   * @return int - hash code
   */
  @SuppressWarnings( "rawtypes" )
  public static int calcMapHashCode( IMap aMap ) {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( Object k : aMap.keys() ) {
      result = TsLibUtils.PRIME * result + k.hashCode();
    }
    for( Object v : aMap.values() ) {
      result = TsLibUtils.PRIME * result + v.hashCode();
    }
    return result;
  }

  /**
   * Detrmines if specified numer is prime.
   *
   * @param aNum int - specified number in range from 4 to {@link Integer#MAX_VALUE}
   * @return boolean - <code>true</code> if argument is prime number
   */
  public static boolean isPrime( int aNum ) {
    int i = (int)Math.ceil( Math.sqrt( aNum ) );
    while( i > 1 ) {
      if( aNum % i == 0 ) { // aNum > 4 гарантирует, что нет деления сам на себя
        return false;
      }
      --i;
    }
    return true;
  }

  /**
   * Finds mimimal prime number greater or equal to the argument.
   * <p>
   * If aLimit <= {@link #MINIMUM_BUCKETS_COUNT} then returns {@link #MINIMUM_BUCKETS_COUNT}. If aLimit >=
   * {@link #MINIMUM_BUCKETS_COUNT}then returns {@link #MAXIMUM_BUCKETS_COUNT}.
   *
   * @param aLimit int - lower limit of prime number to be found
   * @return int - mimimal prime number greater or equal to the argument
   */
  public static int calculateNextPrimeNumber( int aLimit ) {
    if( aLimit <= MINIMUM_BUCKETS_COUNT ) {
      return MINIMUM_BUCKETS_COUNT;
    }
    if( aLimit >= MAXIMUM_BUCKETS_COUNT ) {
      return MAXIMUM_BUCKETS_COUNT;
    }
    int num = aLimit;
    while( true ) {
      ++num;
      if( isPrime( num ) ) {
        return num;
      }
    }
  }

  /**
   * No descendants allowed.
   */
  private TsCollectionsUtils() {
    // nop
  }

}
