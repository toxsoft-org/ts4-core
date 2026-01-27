package org.toxsoft.core.tslib.bricks.time.impl;

import static org.toxsoft.core.tslib.bricks.time.impl.ITsResources.*;

import java.util.*;
import java.util.function.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods for working with time stamps and time intervals.
 * <p>
 * All methods are thread-safe.
 *
 * @author hazard157
 */
public class TimeUtils {

  /**
   * Minimum timestamp (past, beginning of time).
   */
  public static final long MIN_TIMESTAMP = Long.MIN_VALUE;

  /**
   * Maximum timestamp (future, end of time).
   */
  public static final long MAX_TIMESTAMP = Long.MAX_VALUE;

  /**
   * Максимально допустимое значение мекти времени для предоставления с четырехразрадним годом YYYY.
   */
  public static final long MAX_FORMATTABLE_TIMESTAMP = (9998L - 1970L) * 365L * 86400000L;

  /**
   * Минимально допустимое значение мекти времени для предоставления с четырехразрадним годом YYYY.
   */
  public static final long MIN_FORMATTABLE_TIMESTAMP = (-1969L) * 365L * 86400000L;

  /**
   * Textual representation of the timestamp {@link #MIN_TIMESTAMP}.
   */
  public static final String STR_MIN_TIMESTAMP = "TIMESTAMP_MIN"; //$NON-NLS-1$

  /**
   * Textual representation of the timestamp {@link #MAX_TIMESTAMP}.
   */
  public static final String STR_MAX_TIMESTAMP = "TIMESTAMP_MAX"; //$NON-NLS-1$

  /**
   * Строка форматирования метки времени для {@link String#format(String, Object...) String.format( FMT_TIMESTAMP,
   * Long.valueOf( aTimestamp ) )}.
   * <p>
   * Получается строка вида "<b><code>YYYY-MM-DD_HH:MM:SS.mmm</code></b>" (смотрите
   * {@link IStrioHardConstants#TIMESTAMP_FMT}), совместимая со строковм представлением метки времени средствами
   * {@link IStrioWriter#writeTimestamp(long)}.
   */
  public static final String FMT_TIMESTAMP = "%1$tY-%1$tm-%1$td_%1$tH:%1$tM:%1$tS.%1$tL"; //$NON-NLS-1$

  /**
   * Сравнение интервалов по возрастанию начала, а потом окончания интервала.
   */
  public static Comparator<ITimeInterval> COMPARATOR_ASC = TimeUtils::compareAsc;

  /**
   * Сравнение интервалов по убыванию начала, а потом окончания интервала.
   */
  public static Comparator<ITimeInterval> COMPARATOR_DESC = TimeUtils::compareDesc;

  /**
   * Суффиксы для неполного определения меток времени
   */
  private static String[] TIMESTAMP_SUFFIX = { //
      //
      "-01-01_00:00:00.000", //$NON-NLS-1$
      "-01_00:00:00.000", //$NON-NLS-1$
      "_00:00:00.000", //$NON-NLS-1$
      ":00:00.000", //$NON-NLS-1$
      ":00.000", //$NON-NLS-1$
      ".000", //$NON-NLS-1$
      TsLibUtils.EMPTY_STRING };

  /**
   * Допустимые длины текстового канонического представления мекти времени, соответствующие TIMESTAMP_SUFFIX.
   */
  private static int possibleLengths[];

  static {
    possibleLengths = new int[TIMESTAMP_SUFFIX.length];
    int canonicalLegth = IStrioHardConstants.TIMESTAMP_FMT.length();
    for( int i = 0; i < possibleLengths.length; i++ ) {
      possibleLengths[i] = canonicalLegth - TIMESTAMP_SUFFIX[i].length();
    }
  }

  // ------------------------------------------------------------------------------------
  // Текстовое представление меток времени
  //

  /**
   * Читает из представленной частчно канонической строки метку времени.
   * <p>
   * Канонической является формат метки времени {@link IStrioHardConstants#TIMESTAMP_FMT}. Частично канонической
   * является одна из сокращенных форм:
   * <ul>
   * <li>"YYYY";</li>
   * <li>"YYYY-MM";</li>
   * <li>"YYYY-MM-DD";</li>
   * <li>"YYYY-MM-DD_HH";</li>
   * <li>"YYYY-MM-DD_HH:MM";</li>
   * <li>"YYYY-MM-DD_HH:MM:SS".<;li>
   * </ul>
   *
   * @param aTimestamp String - часть канонического строкового представления метки времени
   * @return long - метка времени
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException неверный формат метки времени
   */
  public static long readTimestamp( String aTimestamp ) {
    TsNullArgumentRtException.checkNull( aTimestamp );
    int argLen = aTimestamp.length();
    for( int index = 0, n = TIMESTAMP_SUFFIX.length; index < n; index++ ) {
      if( possibleLengths[index] == argLen ) {
        ICharInputStream chIn = new CharInputStreamString( aTimestamp + TIMESTAMP_SUFFIX[index] );
        IStrioReader sr = new StrioReader( chIn );
        try {
          return sr.readTimestamp();
        }
        catch( Exception e ) {
          throw new TsIllegalArgumentRtException( e, MSG_ERR_WRONG_TIMESTAMP );
        }
      }
    }
    throw new TsIllegalArgumentRtException( MSG_ERR_WRONG_TIMESTAMP );
  }

  /**
   * Returns a textual representation of the timestamp.
   * <p>
   * For valid values ​​(within {@link #MIN_FORMATTABLE_TIMESTAMP}..{@value #MAX_FORMATTABLE_TIMESTAMP}) returns a
   * string of the form {@link #FMT_TIMESTAMP}.
   * <p>
   * For extreme values ​​{@link #MIN_TIMESTAMP} and {@link #MAX_TIMESTAMP}, returns the strings
   * {@link #STR_MIN_TIMESTAMP} and {@link #STR_MAX_TIMESTAMP}, respectively.
   * <p>
   * For other values, returns the text representation using the {@link Long#toString(long)} method.
   *
   * @param aTimestamp long - milliseconds since epoch
   * @return String - String representation of the timestamp
   */
  public static String timestampToString( long aTimestamp ) {
    if( aTimestamp > MIN_FORMATTABLE_TIMESTAMP && aTimestamp < MAX_FORMATTABLE_TIMESTAMP ) {
      return String.format( FMT_TIMESTAMP, Long.valueOf( aTimestamp ) );
    }
    if( aTimestamp == MIN_TIMESTAMP ) {
      return STR_MIN_TIMESTAMP;
    }
    if( aTimestamp == MAX_TIMESTAMP ) {
      return STR_MAX_TIMESTAMP;
    }
    return Long.toString( aTimestamp );
  }

  /**
   * Возвращает осмысленное текстовое представление метки времени в допустимом для форматирования диапазоне.
   * <p>
   * Допустимым для форматирования диапазоном является интервал {@link #MIN_FORMATTABLE_TIMESTAMP}..
   * {@value #MAX_FORMATTABLE_TIMESTAMP}.
   *
   * @param aTimestamp long - мектс времени
   * @return String - строка вида "<b><code>YYYY-MM-DD_HH:MM:SS.mmm</code></b>"
   * @throws TsIllegalArgumentRtException аргумент находится вне допустимого диапазона
   */
  public static String timestampToSaneString( long aTimestamp ) {
    TsIllegalArgumentRtException
        .checkTrue( aTimestamp < MIN_FORMATTABLE_TIMESTAMP || aTimestamp > MAX_FORMATTABLE_TIMESTAMP );
    return String.format( FMT_TIMESTAMP, Long.valueOf( aTimestamp ) );
  }

  // ------------------------------------------------------------------------------------
  // Work with time intervals
  //

  /**
   * Returns the textual representation of the time interval.
   *
   * @param aStart long - starting time in epoch milliseconds
   * @param aEnd long - ending time in epoch milliseconds
   * @return String - string representation of the time interval
   * @throws TsIllegalArgumentRtException aEndTime < aStartTime
   */
  public static String intervalToString( long aStart, long aEnd ) {
    checkIntervalArgs( aStart, aEnd );
    return String.format( "startTime = %s, endTime = %s", //$NON-NLS-1$
        TimeUtils.timestampToString( aStart ), //
        TimeUtils.timestampToString( aEnd ) );
  }

  /**
   * Checks if the specified time stamps can make time interval.
   *
   * @param aStart long - starting time in epoch milliseconds
   * @param aEnd long - ending time in epoch milliseconds
   * @throws TsIllegalArgumentRtException aEndTime < aStartTime
   */
  public static void checkIntervalArgs( long aStart, long aEnd ) {
    if( aEnd < aStart ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_END_TIME_BEFORE_START, Long.valueOf( aEnd ),
          Long.valueOf( aStart ) );
    }
  }

  /**
   * Определяет, находится ли момент времени внутри интервала (включая обе границы интервала).
   *
   * @param aInterval {@link ITimeInterval} - интервал времени
   * @param aTimestamp long - секунда с начала эпизода
   * @return boolean - признак нахождения в интервале
   */
  public static boolean contains( ITimeInterval aInterval, long aTimestamp ) {
    if( aInterval == null ) {
      throw new TsNullArgumentRtException();
    }
    return (aTimestamp >= aInterval.startTime()) && (aTimestamp <= aInterval.endTime());
  }

  /**
   * Определяет, находится ли второй интервал внутри первого интервала.
   * <p>
   * Для равных интервалов метод возвращает <code>true</code>.
   *
   * @param aInterval {@link ITimeInterval} - первый интервал
   * @param aStart long - начальная метка времени второго интервала
   * @param aEnd long - конечная метка времени второго интервала
   * @return boolean - признак второго интервала внутри первого
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aEnd < aStart
   */
  public static boolean contains( ITimeInterval aInterval, long aStart, long aEnd ) {
    TsNullArgumentRtException.checkNull( aInterval );
    TsIllegalArgumentRtException.checkTrue( aEnd < aStart );
    return contains( aInterval, aStart ) && contains( aInterval, aEnd );
  }

  /**
   * Определяет, находится ли второй интервал внутри первого интервала.
   * <p>
   * Для равных интервалов метод возвращает <code>true</code>.
   *
   * @param aInterval1 {@link ITimeInterval} - первый интервал времени
   * @param aInterval2 {@link ITimeInterval} - второй интервал времени
   * @return boolean - признак второго интервала внутри первого
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean contains( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNull( aInterval2 );
    return contains( aInterval1, aInterval2.startTime(), aInterval2.endTime() );
  }

  /**
   * Определяет, находится ли второй интервал внутри первого интервала но не равен первому.
   * <p>
   * Для равных интервалов метод возвращает <code><code>false</code></code>.
   *
   * @param aInterval1 {@link ITimeInterval} - первый интервал времени
   * @param aInterval2 {@link ITimeInterval} - второй интервал времени
   * @return boolean - признак второго интервала внутри первого
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean containsNeq( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNull( aInterval2 );
    if( aInterval2.equals( aInterval1 ) ) {
      return false;
    }
    return contains( aInterval1, aInterval2.startTime(), aInterval2.endTime() );
  }

  /**
   * Determines whether intervals intersect.
   *
   * @param aInterval {@link ITimeInterval} - the first interval
   * @param aStart long - the second interval start time
   * @param aEnd long - the second interval end time
   * @return boolean - <code>true</code> if intervals intersects
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aEnd < aStart
   */
  public static boolean intersects( ITimeInterval aInterval, long aStart, long aEnd ) {
    TsNullArgumentRtException.checkNull( aInterval );
    TsIllegalArgumentRtException.checkTrue( aEnd < aStart );
    return ((aEnd >= aInterval.startTime()) && (aStart <= aInterval.endTime()));
  }

  /**
   * Determines whether intervals intersect.
   *
   * @param aInterval1 {@link ITimeInterval} - the first interval
   * @param aInterval2 {@link ITimeInterval} - the second interval
   * @return boolean - <code>true</code> if intervals intersects
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean intersects( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNull( aInterval2 );
    return intersects( aInterval1, aInterval2.startTime(), aInterval2.endTime() );
  }

  /**
   * Определяет, соприкасается ли два интервала.
   * <p>
   * Соприкосновением (без пересечения!) является ситуация когда между концом одного и началом следуещего интервала 0
   * миллисекунд разницы.
   *
   * @param aInterval {@link ITimeInterval} - первый интервал
   * @param aStart long - начальная метка времени второго интервала
   * @param aEnd long - конечная метка времени второго интервала
   * @return boolean - признак соприкосновения
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aEnd < aStart
   */
  public static boolean touches( ITimeInterval aInterval, long aStart, long aEnd ) {
    TsNullArgumentRtException.checkNull( aInterval );
    TsIllegalArgumentRtException.checkTrue( aEnd < aStart );
    return (aInterval.startTime() == aEnd + 1) || (aStart == aInterval.endTime() + 1);
  }

  /**
   * Определяет, соприкасается ли два интервала.
   * <p>
   * Соприкосновением (без пересечения!) является ситуация когда между концом одного и началом следуещего интервала 0
   * миллисекунд разницы.
   *
   * @param aInterval1 {@link ITimeInterval} - первый интервал времени
   * @param aInterval2 {@link ITimeInterval} - второй интервал времени
   * @return boolean - признак соприкосновения
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static boolean touches( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNull( aInterval2 );
    return touches( aInterval1, aInterval2.startTime(), aInterval2.endTime() );
  }

  /**
   * Определяет является ли метка времени позже (правее) интервала.
   *
   * @param aInterval {@link ITimeInterval} - интервал времени
   * @param aTimestamp long - мекта времени
   * @return boolean - признак, что aTimestamp > {@link ITimeInterval#endTime()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean isAfter( ITimeInterval aInterval, long aTimestamp ) {
    TsNullArgumentRtException.checkNull( aInterval );
    return aTimestamp > aInterval.endTime();
  }

  /**
   * Определяет является ли метка времени раньше (левее) нтервала.
   *
   * @param aInterval {@link ITimeInterval} - интервал времени
   * @param aTimestamp long - мекта времени
   * @return boolean - признак, что aTimestamp < {@link ITimeInterval#startTime()}
   */
  public static boolean isBefore( ITimeInterval aInterval, long aTimestamp ) {
    TsNullArgumentRtException.checkNull( aInterval );
    return aTimestamp < aInterval.startTime();
  }

  /**
   * Загоняет метку времени в интервал.
   * <p>
   * "Загнать" в интервал означает, что если метка времени левее (раньше) интервала, то возвращается начало интервала,
   * если правее - окончание интервала. Если мекта времни внутри интервала, то метод возвращает аргумент метку времени
   * без изменения.
   *
   * @param aInterval {@link ITimeInterval} - интервал времени
   * @param aTimestamp long - мекта времени
   * @return long - мекта времени, которое находится в интервале
   */
  public static long inRange( ITimeInterval aInterval, long aTimestamp ) {
    TsNullArgumentRtException.checkNull( aInterval );
    if( aTimestamp < aInterval.startTime() ) {
      return aInterval.startTime();
    }
    if( aTimestamp > aInterval.endTime() ) {
      return aInterval.endTime();
    }
    return aTimestamp;
  }

  /**
   * Creates a union of two intervals.
   *
   * @param aInterval1 {@link ITimeInterval} - first interval
   * @param aInterval2 {@link ITimeInterval} - second interval
   * @return {@link ITimeInterval} - united interval
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITimeInterval union( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNulls( aInterval1, aInterval2 );
    long start = Math.min( aInterval1.startTime(), aInterval2.startTime() );
    long end = Math.max( aInterval1.endTime(), aInterval2.endTime() );
    return new TimeInterval( start, end );
  }

  /**
   * Creates an interval that is the intersection of intervals.
   * <p>
   * Method returns <code>null</code> if the arguments do not intersect.
   *
   * @param aInterval1 {@link ITimeInterval} - the first interval
   * @param aInterval2 {@link ITimeInterval} - the second interval
   * @return {@link ITimeInterval} - the intersection or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITimeInterval intersection( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNulls( aInterval1, aInterval2 );
    long start = Math.max( aInterval1.startTime(), aInterval2.startTime() );
    long end = Math.min( aInterval1.endTime(), aInterval2.endTime() );
    if( start > end ) {
      return null;
    }
    return new TimeInterval( start, end );
  }

  /**
   * Вычитает из первого воторй интервал и возвращает разницу.
   * <p>
   * Разнице интервалов ялветяся до дву интервалов, которые дополняют второй интервал до первого слева и справа. Это два
   * интервала возвращаются в виде пары {@link Pair}. Если с любой из сторон вторй интервал равен или бьольше первого,
   * соответствующий элемент в паре равен <code>null</code>. Если второй интервал больше первого, в паре оба элемента
   * <code>null</code>.
   * <p>
   * Если инетрвалы вообще не пересекаются, то в паре возвращается первый интервал: слева, если он раньше воторого
   * интервала, или справа, если первый интиервал позже второго.
   *
   * @param aInterval1 {@link ITimeInterval} - first interval
   * @param aInterval2 {@link ITimeInterval} - second interval
   * @return {@link Pair}&lt;{@link ITimeInterval},{@link ITimeInterval}&gt; - разница слева и справа
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static Pair<ITimeInterval, ITimeInterval> subtract( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    if( aInterval1 == null || aInterval2 == null ) {
      throw new TsNullArgumentRtException();
    }
    ITimeInterval left = null, right = null;
    if( aInterval1.startTime() < aInterval2.startTime() ) {
      left = new TimeInterval( aInterval1.startTime(), Math.min( aInterval1.endTime(), aInterval2.startTime() - 1 ) );
    }
    if( aInterval1.endTime() > aInterval2.endTime() ) {
      right = new TimeInterval( Math.max( aInterval1.startTime(), aInterval2.endTime() + 1 ), aInterval1.endTime() );
    }
    return new Pair<>( left, right );
  }

  /**
   * Сравнивает два интервала по возрастанию начала, а потом окончания интервала.
   *
   * @param aInterval1 {@link ITimeInterval} - first interval
   * @param aInterval2 {@link ITimeInterval} - second interval
   * @return <0 - aInterval1 раньше aInterval2, 0 - aInterval1 равно aInterval2, >0 aInterval1 позже aInterval2
   */
  public static int compareAsc( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNulls( aInterval1, aInterval2 );
    int c = Long.compare( aInterval1.startTime(), aInterval2.startTime() );
    if( c == 0 ) {
      c = Long.compare( aInterval1.endTime(), aInterval2.endTime() );
    }
    return c;
  }

  /**
   * Сравнивает два интервала по убыванию начала, а потом окончания интервала.
   *
   * @param aInterval1 {@link ITimeInterval} - first interval
   * @param aInterval2 {@link ITimeInterval} - second interval
   * @return <0 - aInterval1 позже aInterval2, 0 - aInterval1 равно aInterval2, >0 aInterval1 раньше aInterval2
   */
  public static int compareDesc( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNulls( aInterval1, aInterval2 );
    int c = -Long.compare( aInterval1.startTime(), aInterval2.startTime() );
    if( c == 0 ) {
      c = -Long.compare( aInterval1.endTime(), aInterval2.endTime() );
    }
    return c;
  }

  // ------------------------------------------------------------------------------------
  // Optimized methods for performance
  //
  /**
   * Объединяет списки темпоральных значений в один список
   *
   * @param aInputs {@link IList}&lt;{@link ITimedList}&gt; входные списки темпоральных значений.
   * @param <T> тип занчений
   * @return {@link ITimedList} выходной список
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T extends ITemporal<T>> TimedList<T> uniteTimeporaLists( IList<ITimedList<T>> aInputs ) {
    return uniteTimeporaLists( aInputs, TimedList::new );
  }

  /**
   * Объединяет списки темпоральных значений в один список
   *
   * @param aInputs {@link IList}&lt;{@link ITimedList}&gt; входные списки темпоральных значений.
   * @param <T> тип значений
   * @param <R> - concrete type of the returned timed list
   * @param aTimeListCreator {@link Function} ссылка на метод создания результата {@link TimedList}
   * @return {@link ITimedList} выходной список
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  public static <T extends ITemporal<T>, R extends TimedList<T>> R uniteTimeporaLists( IList<ITimedList<T>> aInputs,
      Function<Integer, R> aTimeListCreator ) {
    TsNullArgumentRtException.checkNull( aInputs );
    int size = 0;
    ElemArrayList<TemporalListWrapper<T>> wrappers = new ElemArrayList<>();
    for( ITimedList<T> list : aInputs ) {
      size += list.size();
      wrappers.add( new TemporalListWrapper<>( list ) );
    }
    TimedList<T> retValue = aTimeListCreator.apply( Integer.valueOf( size ) );
    // Объединение значений по времени
    for( T value = nextValueOrNull( wrappers ); value != null; value = nextValueOrNull( wrappers ) ) {
      retValue.add( value );
    }
    return (R)retValue;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  @SuppressWarnings( "unchecked" )
  private static <T extends ITemporal<T>> T nextValueOrNull( ElemArrayList<TemporalListWrapper<T>> aWrappers ) {
    TsNullArgumentRtException.checkNull( aWrappers );
    int foundIndex = -1;
    T foundValue = null;
    for( int index = 0, n = aWrappers.size(); index < n; index++ ) {
      TemporalListWrapper<T> wrapper = aWrappers.get( index );
      T value = (T)wrapper.value();
      if( value == null ) {
        continue;
      }
      if( foundValue != null && foundValue.timestamp() < value.timestamp() ) {
        continue;
      }
      foundValue = value;
      foundIndex = index;
    }
    if( foundValue != null ) {
      aWrappers.get( foundIndex ).next();
    }
    return foundValue;
  }

  private static class TemporalListWrapper<T extends ITemporal<T>> {

    private final Iterator<T> it;
    private T                 value;

    TemporalListWrapper( ITimedList<T> aList ) {
      TsNullArgumentRtException.checkNull( aList );
      it = aList.iterator();
      next();
    }

    ITemporal<T> value() {
      return value;
    }

    void next() {
      value = null;
      if( it.hasNext() ) {
        value = it.next();
      }
    }
  }

  /**
   * No subclasses.
   */
  private TimeUtils() {
    // nop
  }

}
