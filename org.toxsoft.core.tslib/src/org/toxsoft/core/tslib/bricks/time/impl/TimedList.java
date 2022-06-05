package org.toxsoft.core.tslib.bricks.time.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link ITimedListEdit}.
 *
 * @author hazard157
 * @param <T> - конкретный тип сущности с меткой времени
 */
public class TimedList<T extends ITimestampable>
    extends SortedElemLinkedBundleListEx<T>
    implements ITimedListEdit<T> {

  private static final long serialVersionUID = 157157L;

  static class TimestampablesComparator
      implements Comparator<ITimestampable>, Serializable {

    private static final long serialVersionUID = 157157L;

    @Override
    public int compare( ITimestampable aObj1, ITimestampable aObj2 ) {
      if( aObj1 == aObj2 ) {
        return 0;
      }
      if( aObj1 == null ) {
        return 1;
      }
      if( aObj2 == null ) {
        return -1;
      }
      return Long.compare( aObj1.timestamp(), aObj2.timestamp() );
    }

  }

  private static final Comparator<ITimestampable> COMPARATOR = new TimestampablesComparator();

  // --------------------------------------------------------------------------
  // Конструкторы
  //

  /**
   * Constructor with all invariants.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  @SuppressWarnings( "unchecked" )
  public TimedList( int aBundleCapacity, boolean aAllowDuplicates ) {
    super( (Comparator<T>)COMPARATOR, aBundleCapacity, aAllowDuplicates );
  }

  /**
   * Constructor.
   * <p>
   * Created list may contain duplicates.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  public TimedList( int aBundleCapacity ) {
    this( aBundleCapacity, true );
  }

  /**
   * Constructor.
   * <p>
   * Created list may contain duplicates.
   */
  public TimedList() {
    this( DEFAULT_BUNDLE_CAPACITY, true );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsCollection}&lt;T&gt; - исходная коллекция
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TimedList( ITsCollection<T> aSource ) {
    this( getListInitialCapacity( estimateOrder( TsNullArgumentRtException.checkNull( aSource ).size() ) ), true );
    setAll( aSource );
  }

  /**
   * Constructor with initialization by array.
   *
   * @param aElems &lt;E&gt;[] - specified array
   * @throws TsNullArgumentRtException argument or any it's element = <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  public TimedList( T... aElems ) {
    this();
    setAll( aElems );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Находит индекс первого элемента с заданным временем или первый перед ним.
   *
   * @param aTimestamp long - запрошенное время
   * @return int - индекс элемента или -1
   *         <ul>
   *         <li>если список пустой, возвращает -1;</li>
   *         <li>если все элементы находятся перед запрошенным временем, возвращает индекс последнего {@link #size()}-1;
   *         </li>
   *         <li>если все элементы находятся после запрошенного времени, возвращает -1;</li>
   *         <li>если есть элемент(ы) с заданным временем, возвращает индекс последнего из таких элементов;</li>
   *         <li>найден элемент слева от запрошенного времени, возвращает его индекс.</li>
   *         </ul>
   */
  int findFirstOrBefore( long aTimestamp ) {
    int count = size();
    if( count == 0 ) {
      return -1;
    }
    if( last().timestamp() < aTimestamp ) {
      return count - 1;
    }
    if( first().timestamp() > aTimestamp ) {
      return -1;
    }
    // TODO OPTIMIZE переделать алогритм на двоичный поиск
    for( int i = 0; i < count; i++ ) {
      long t = get( i ).timestamp();
      if( t == aTimestamp ) {
        return i;
      }
      if( t > aTimestamp ) {
        return i - 1;
      }
    }
    throw new TsInternalErrorRtException(); // не должны оказаться здесь
  }

  /**
   * Находит индекс первого элемента с заданным временем или первый после него.
   *
   * @param aTimestamp long - запрошенное время
   * @return int - индекс элемента или -1
   *         <ul>
   *         <li>если список пустой, возвращает -1;</li>
   *         <li>если все элементы находятся перед запрошенным временeм, возвращает -1;</li>
   *         <li>если все элементы находятся после запрошенного времени, возвращает 0;</li>
   *         <li>если есть элемент(ы) с заданным временем, возвращает индекс первого из таких элементов;</li>
   *         <li>найден элемент справа от запрошенного времени, возвращает его индекс.</li>
   *         </ul>
   */
  int findFirstOrAfter( long aTimestamp ) {
    int count = size();
    if( count == 0 ) {
      return -1;
    }
    if( last().timestamp() < aTimestamp ) {
      return -1;
    }
    if( first().timestamp() > aTimestamp ) {
      return 0;
    }
    // TODO OPTIMIZE переделать алогритм на двоичный поиск
    for( int i = 0; i < count; i++ ) {
      long t = get( i ).timestamp();
      if( t >= aTimestamp ) {
        return i;
      }
    }
    throw new TsInternalErrorRtException(); // не должны оказаться здесь
  }

  /**
   * Находит индекс последнего элемента с заданным временем или последний перед ним.
   *
   * @param aTimestamp long - запрошенное время
   * @return int - индекс элемента или -1
   *         <ul>
   *         <li>если список пустой, возвращает -1;</li>
   *         <li>если все элементы находятся перед запрошенным временем, возвращает индекс последнего {@link #size()}-1;
   *         </li>
   *         <li>если все элементы находятся после запрошенного времени, возвращает -1;</li>
   *         <li>если есть элемент(ы) с заданным временем, возвращает индекс последнего из таких элементов;</li>
   *         <li>найден элемент слева от запрошенного времени, возвращает его индекс.</li>
   *         </ul>
   */
  int findLastOrBefore( long aTimestamp ) {
    int count = size();
    if( count == 0 ) {
      return -1;
    }
    if( first().timestamp() > aTimestamp ) {
      return count - 1;
    }
    if( last().timestamp() < aTimestamp ) {
      return -1;
    }
    // TODO OPTIMIZE переделать алогритм на двоичный поиск
    for( int i = count - 1; i >= 0; i-- ) {
      long t = get( i ).timestamp();
      if( t <= aTimestamp ) {
        return i;
      }
    }
    throw new TsInternalErrorRtException(); // не должны оказаться здесь
  }

  /**
   * Находит индекс последнего элемента с заданным временем или первый после него.
   *
   * @param aTimestamp long - запрошенное время
   * @return int - индекс элемента или -1
   *         <ul>
   *         <li>если список пустой, возвращает -1;</li>
   *         <li>если все элементы находятся перед запрошенным временeм, возвращает -1;</li>
   *         <li>если все элементы находятся после запрошенного времени, возвращает 0;</li>
   *         <li>если есть элемент(ы) с заданным временем, возвращает индекс последнего из таких элементов;</li>
   *         <li>найден элемент справа от запрошенного времени, возвращает его индекс.</li>
   *         </ul>
   */
  int findLastOrAfter( long aTimestamp ) {
    int count = size();
    if( count == 0 ) {
      return -1;
    }
    if( last().timestamp() < aTimestamp ) {
      return -1;
    }
    if( first().timestamp() > aTimestamp ) {
      return 0;
    }
    // TODO OPTIMIZE переделать алогритм на двоичный поиск
    for( int i = count - 1; i >= 0; i-- ) {
      long t = get( i ).timestamp();
      if( t == aTimestamp ) {
        return i;
      }
      if( t < aTimestamp ) {
        return i + 1;
      }
    }
    throw new TsInternalErrorRtException(); // не должны оказаться здесь
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITimedList
  //

  @Override
  public ITimeInterval getInterval() {
    if( size() == 0 ) {
      return ITimeInterval.NULL;
    }
    return new TimeInterval( first().timestamp(), last().timestamp() );
  }

  @Override
  public ITimedListEdit<T> selectInterval( ITimeInterval aTimeInterval ) {
    TsNullArgumentRtException.checkNull( aTimeInterval );
    ITimedListEdit<T> ll = new TimedList<>();
    if( isEmpty() ) {
      return ll;
    }
    int index1 = findFirstOrAfter( aTimeInterval.startTime() );
    int index2 = findLastOrBefore( aTimeInterval.endTime() );
    for( int i = index1; i <= index2; i++ ) {
      ll.add( get( i ) );
    }
    return ll;
  }

  @Override
  public ITimedListEdit<T> selectExtendedInterval( ITimeInterval aTimeInterval ) {
    TsNullArgumentRtException.checkNull( aTimeInterval );
    ITimedListEdit<T> ll = new TimedList<>();
    if( isEmpty() ) {
      return ll;
    }
    int index1 = findFirstOrBefore( aTimeInterval.startTime() );
    int index2 = findLastOrAfter( aTimeInterval.endTime() );
    for( int i = index1; i <= index2; i++ ) {
      ll.add( get( i ) );
    }
    return ll;
  }

  @Override
  public ITimedListEdit<T> selectAfter( long aTimestamp ) {
    ITimedListEdit<T> ll = new TimedList<>();
    if( isEmpty() ) {
      return ll;
    }
    int index = findFirstOrAfter( aTimestamp );
    for( int i = index, count = size(); i < count; i++ ) {
      ll.add( get( i ) );
    }
    return ll;
  }

  @Override
  public ITimedListEdit<T> selectBefore( long aTimestamp ) {
    ITimedListEdit<T> ll = new TimedList<>();
    if( isEmpty() ) {
      return ll;
    }
    int index = findLastOrBefore( aTimestamp );
    for( int i = 0; i <= index; i++ ) {
      ll.add( get( i ) );
    }
    return ll;
  }

  @Override
  public int firstIndexOf( long aTimestamp ) {
    if( !isEmpty() ) {
      int index = findFirstOrAfter( aTimestamp );
      if( get( index ).timestamp() == aTimestamp ) {
        return index;
      }
    }
    return -1;
  }

  @Override
  public int lastIndexOf( long aTimestamp ) {
    if( !isEmpty() ) {
      int index = findLastOrBefore( aTimestamp );
      if( get( index ).timestamp() == aTimestamp ) {
        return index;
      }
    }
    return -1;
  }

  @Override
  public int firstIndexOrAfter( long aTimestamp ) {
    return findLastOrAfter( aTimestamp );
  }

  @Override
  public int lastIndexOrAfter( long aTimestamp ) {
    return findLastOrAfter( aTimestamp );
  }

  @Override
  public int lastIndexOrBefore( long aTimestamp ) {
    return findLastOrBefore( aTimestamp );
  }

  @Override
  public int firstIndexOrBefore( long aTimestamp ) {
    return findFirstOrBefore( aTimestamp );
  }

  @Override
  public int firstIndexAfter( long aTimestamp ) {
    int index = findLastOrAfter( aTimestamp );
    if( index >= 0 ) {
      if( get( index ).timestamp() == aTimestamp ) {
        ++index;
      }
    }
    return index;
  }

  @Override
  public int lastIndexBefore( long aTimestamp ) {
    int index = findLastOrBefore( aTimestamp );
    if( index >= 0 ) {
      if( get( index ).timestamp() == aTimestamp ) {
        --index;
      }
    }
    return index;
  }

}
