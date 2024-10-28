package org.toxsoft.core.tslib.bricks.time.impl;

import static org.toxsoft.core.tslib.bricks.time.impl.ITsResources.*;

import java.util.*;

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
 * Вспомогательные методы работы с метками и интервалами времени.
 * <p>
 * Все методы являются потоко-безопасными.
 *
 * @author hazard157
 */
public class TimeUtils {

  /**
   * Минимальная метка времени (прошлое, начало времен).
   */
  public static final long MIN_TIMESTAMP = Long.MIN_VALUE;

  /**
   * Максимальная метка времени (будущее, конец времен).
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
   * Строквое представление метки времени {@link #MIN_TIMESTAMP}.
   */
  public static final String STR_MIN_TIMESTAMP = "TIMESTAMP_MIN"; //$NON-NLS-1$

  /**
   * Строквое представление метки времени {@link #MAX_TIMESTAMP}.
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
   * @throws TsNullArgumentRtException аргумент = null
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
   * Возвращает текстовое представление метки врмени.
   * <p>
   * Для допустимых значений (в пределах {@link #MIN_FORMATTABLE_TIMESTAMP}..{@value #MAX_FORMATTABLE_TIMESTAMP})
   * возвращает строку вида вида {@link #FMT_TIMESTAMP}.
   * <p>
   * Для крайных значений {@link #MIN_TIMESTAMP} и {@link #MAX_TIMESTAMP} возвращает строки {@link #STR_MIN_TIMESTAMP} и
   * {@link #STR_MAX_TIMESTAMP}, соответственно.
   * <p>
   * Для остальных значений возвращает текстовое представление методом {@link Long#toString(long)}.
   *
   * @param aTimestamp long - метка времени
   * @return String - текстовое представление метки времени
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
  // Работа с интервалами времени
  //

  /**
   * Возвращает строковое представление интервала
   *
   * @param aStart long - время начала интервала времени (в миллисекундах с начала эпохи)
   * @param aEnd long - время окончания интервала времени (в миллисекундах с начала эпохи)
   * @return String - строковое представление метки времени
   * @throws TsIllegalArgumentRtException aEndTime < aStartTime
   */
  public static String intervalToString( long aStart, long aEnd ) {
    checkIntervalArgs( aStart, aEnd );
    return String.format( "startTime = %s, endTime = %s", //$NON-NLS-1$
        TimeUtils.timestampToString( aStart ), //
        TimeUtils.timestampToString( aEnd ) );
  }

  /**
   * Проверяет, что указанные метки времени образуют интервал времени
   *
   * @param aStart long - время начала интервала времени (в миллисекундах с начала эпохи)
   * @param aEnd long - время окончания интервала времени (в миллисекундах с начала эпохи)
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
   * @throws TsNullArgumentRtException аргумент = null
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
   * @throws TsNullArgumentRtException любой аргумент = null
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
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static boolean containsNeq( ITimeInterval aInterval1, ITimeInterval aInterval2 ) {
    TsNullArgumentRtException.checkNull( aInterval2 );
    if( aInterval2.equals( aInterval1 ) ) {
      return false;
    }
    return contains( aInterval1, aInterval2.startTime(), aInterval2.endTime() );
  }

  /**
   * Определяет, пересекаются ли интервалы.
   *
   * @param aInterval {@link ITimeInterval} - первый интервал
   * @param aStart long - начальная метка времени второго интервала
   * @param aEnd long - конечная метка времени второго интервала
   * @return boolean - признак нахождения в интервале
   * @throws TsNullArgumentRtException aInterval = null
   * @throws TsIllegalArgumentRtException aEnd < aStart
   */
  public static boolean intersects( ITimeInterval aInterval, long aStart, long aEnd ) {
    TsNullArgumentRtException.checkNull( aInterval );
    TsIllegalArgumentRtException.checkTrue( aEnd < aStart );
    return ((aEnd >= aInterval.startTime()) && (aStart <= aInterval.endTime()));
  }

  /**
   * Определяет, пересекаются ли интервалы времени
   *
   * @param aInterval1 {@link ITimeInterval} - первый интервал времени
   * @param aInterval2 {@link ITimeInterval} - второй интервал времени
   * @return boolean - признак, что интервалы пересекаются
   * @throws TsNullArgumentRtException любой аргумент = null
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
   * @throws TsNullArgumentRtException aInterval = null
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
   * @throws TsNullArgumentRtException любой аргумент = null
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
   * @throws TsNullArgumentRtException aInterval = null
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
   * Создает интервал, объединяющий интервалы.
   *
   * @param aIn1 {@link ITimeInterval} - первый инетрвал
   * @param aIn2 {@link ITimeInterval} - второй инетрвал
   * @return {@link ITimeInterval} - объединение интервалов
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITimeInterval union( ITimeInterval aIn1, ITimeInterval aIn2 ) {
    TsNullArgumentRtException.checkNulls( aIn1, aIn2 );
    long start = Math.min( aIn1.startTime(), aIn2.startTime() );
    long end = Math.max( aIn1.endTime(), aIn2.endTime() );
    return new TimeInterval( start, end );
  }

  /**
   * Создает интервал, являюшейся пересечением интервалов или возвращает null если аргументы не персекаются.
   *
   * @param aIn1 {@link ITimeInterval} - первый инетрвал
   * @param aIn2 {@link ITimeInterval} - второй инетрвал
   * @return {@link ITimeInterval} - пересечение интервалов или null
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITimeInterval intersection( ITimeInterval aIn1, ITimeInterval aIn2 ) {
    TsNullArgumentRtException.checkNulls( aIn1, aIn2 );
    long start = Math.max( aIn1.startTime(), aIn2.startTime() );
    long end = Math.min( aIn1.endTime(), aIn2.endTime() );
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
   * @param aIn1 {@link ITimeInterval} - первый инетрвал
   * @param aIn2 {@link ITimeInterval} - второй инетрвал
   * @return {@link Pair}&lt;{@link ITimeInterval},{@link ITimeInterval}&gt; - разница слева и справа
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static Pair<ITimeInterval, ITimeInterval> subtract( ITimeInterval aIn1, ITimeInterval aIn2 ) {
    if( aIn1 == null || aIn2 == null ) {
      throw new TsNullArgumentRtException();
    }
    ITimeInterval left = null, right = null;
    if( aIn1.startTime() < aIn2.startTime() ) {
      left = new TimeInterval( aIn1.startTime(), Math.min( aIn1.endTime(), aIn2.startTime() - 1 ) );
    }
    if( aIn1.endTime() > aIn2.endTime() ) {
      right = new TimeInterval( Math.max( aIn1.startTime(), aIn2.endTime() + 1 ), aIn1.endTime() );
    }
    return new Pair<>( left, right );
  }

  /**
   * Сравнивает два интервала по возрастанию начала, а потом окончания интервала.
   *
   * @param aIn1 {@link ITimeInterval} - первый инетрвал
   * @param aIn2 {@link ITimeInterval} - второй инетрвал
   * @return <0 - aIn1 раньше aIn2, 0 - aIn1 равно aIn2, >0 aIn1 позже aIn2
   */
  public static int compareAsc( ITimeInterval aIn1, ITimeInterval aIn2 ) {
    TsNullArgumentRtException.checkNulls( aIn1, aIn2 );
    int c = Long.compare( aIn1.startTime(), aIn2.startTime() );
    if( c == 0 ) {
      c = Long.compare( aIn1.endTime(), aIn2.endTime() );
    }
    return c;
  }

  /**
   * Сравнивает два интервала по убыванию начала, а потом окончания интервала.
   *
   * @param aIn1 {@link ITimeInterval} - первый инетрвал
   * @param aIn2 {@link ITimeInterval} - второй инетрвал
   * @return <0 - aIn1 позже aIn2, 0 - aIn1 равно aIn2, >0 aIn1 раньше aIn2
   */
  public static int compareDesc( ITimeInterval aIn1, ITimeInterval aIn2 ) {
    TsNullArgumentRtException.checkNulls( aIn1, aIn2 );
    int c = -Long.compare( aIn1.startTime(), aIn2.startTime() );
    if( c == 0 ) {
      c = -Long.compare( aIn1.endTime(), aIn2.endTime() );
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
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static <T extends ITemporal<T>> TimedList<T> uniteTimeporaLists( IList<ITimedList<T>> aInputs ) {
    TsNullArgumentRtException.checkNull( aInputs );
    int size = 0;
    ElemArrayList<TemporalListWrapper<T>> wrappers = new ElemArrayList<>();
    for( ITimedList<T> list : aInputs ) {
      size += list.size();
      wrappers.add( new TemporalListWrapper<>( list ) );
    }
    TimedList<T> retValue = new TimedList<>( size );
    // Объединение значений по времени
    for( T value = nextValueOrNull( wrappers ); value != null; value = nextValueOrNull( wrappers ) ) {
      retValue.add( value );
    }
    return retValue;
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
   * Запрет на создание экземпляров.
   */
  private TimeUtils() {
    // nop
  }

}
