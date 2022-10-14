package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Допустимые значения цены деления X-шкалы (шкалы времени).
 *
 * @author goga, vs
 */
@SuppressWarnings( "nls" )
public enum ETimeUnit
    implements IStridable {

  /**
   * Секунда
   */
  SEC01( "sec01", "секунда", "", 1 * 1000L, 0, 0 ),
  /**
   * Две секунды
   */
  SEC02( "sec02", "2 секунды", "", 2 * 1000L, 4, 1 ),
  SEC03( "sec03", "3 секунды", "", 3 * 1000L, 2, 0 ),
  SEC05( "sec05", "5 секунд", "", 5 * 1000L, 4, 0 ),
  SEC10( "sec10", "10 секунд", "", 10 * 1000L, 4, 1 ),
  SEC15( "sec15", "15 секунд", "", 15 * 1000L, 4, 2 ),
  SEC20( "sec20", "20 секунд", "", 20 * 1000L, 1, 1 ),
  SEC30( "sec30", "30 секунд", "", 30 * 1000L, 29, 2 ),
  MIN01( "min01", "1 минута", "", 1 * 60 * 1000L, 4, 1 ),
  MIN05( "min05", "5 минут", "", 5 * 60 * 1000L, 4, 0 ),
  MIN10( "min10", "10 минут", "", 10 * 60 * 1000L, 4, 1 ),
  MIN15( "min15", "15 минут", "", 15 * 60 * 1000L, 2, 0 ),
  MIN20( "min20", "20 минут", "", 20 * 60 * 1000L, 4, 1 ),
  MIN30( "min30", "30 минут", "", 30 * 60 * 1000L, 2, 1 ),
  HOUR01( "hour01", "1 час", "", 1 * 60 * 60 * 1000L, 4, 1 ),
  HOUR02( "hour02", "2 часа", "", 2 * 60 * 60 * 1000L, 2, 1 ),
  HOUR04( "hour04", "4 часа", "", 4 * 60 * 60 * 1000L, 1, 0 ),
  HOUR08( "hour08", "8 часов", "", 8 * 60 * 60 * 1000L, 7, 1 ),
  HOUR12( "hour12", "12 часов", "", 12 * 60 * 60 * 1000L, 11, 1 ),
  DAY( "day", "День", "", 24 * 60 * 60 * 1000L, 7, 0 ),
  WEEK( "week", "Неделя", "", 7 * 24 * 60 * 60 * 1000L, 7, 0 ),
  // FIXME определить MONTH,
  // FIXME определить QUARTER,
  YEAR( "year", "Год", "", -1, 0, 0 );

  private final String id;
  private final String name;
  private final String description;
  private final long   millisecs;
  private final int    littTicksQtty;
  private final int    midTicksQtty;

  ETimeUnit( String aId, String aName, String aDescr, long aMillisecs, int aLittTickQtty, int aMidTickQtty ) {
    id = aId;
    name = aName;
    description = aDescr;
    millisecs = aMillisecs;
    littTicksQtty = aLittTickQtty;
    midTicksQtty = aMidTickQtty;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Продолжительность интервала в миллисекундах.
   *
   * @return long - продолжительность интервала в миллисекундах
   */
  public long timeInMills() {
    return millisecs;
  }

  /**
   * Количество маленьких засечек.
   *
   * @return int - количество маленьких засечек
   */
  public int littTicksQtty() {
    return littTicksQtty;
  }

  /**
   * Количество средних засечек.
   *
   * @return int - количество средних засечек
   */
  public int midTicksQtty() {
    return midTicksQtty;
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа с заданным идентификатором.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким идентификатором;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа с заданным описанием.
   *
   * @param aDescription String - описание {@link #id()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким описанием;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Находит элемент по количеству миллисекунд.
   *
   * @param aMills long - продолжительность элемента в миллисекундах
   * @return ETimeUnit - элемент с требуемой продолжительностью или null
   */
  public static ETimeUnit findByMillsOrNull( long aMills ) {
    for( ETimeUnit tu : ETimeUnit.values() ) {
      if( tu.millisecs >= aMills ) {
        return tu;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, возвращает null.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return ETimeUnit - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETimeUnit findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( ETimeUnit item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, выбрасывает исключение.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return ETimeUnit - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ETimeUnit findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, возвращает null.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return ETimeUnit - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETimeUnit findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( ETimeUnit item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, выбрасывает исключение.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return ETimeUnit - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ETimeUnit findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

}
