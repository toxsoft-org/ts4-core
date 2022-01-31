package org.toxsoft.core.tslib.bricks.time;

import static org.toxsoft.core.tslib.bricks.time.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Тип интервала запроса {@link IQueryInterval}.
 * <p>
 * Константа перечисления определяет, будет ли ответ на запрос включать в чебя данные, выходящие за пределы интервала.
 * Такое возможно, только если в запрашиваемых данных нет хотя бы одного данного с метко времени точно равной одной из
 * границ интервала.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
public enum EQueryIntervalType
    implements IStridable {

  /**
   * .
   */
  OSOE( "OSOE", STR_D_QIT_OSOE, STR_N_QIT_OSOE, false, false ),

  /**
   * Интерал запроса с откритым началом и закрытым окончанием.
   */
  OSCE( "OSCE", STR_D_QIT_OSCE, STR_N_QIT_OSCE, false, true ),

  /**
   * Интерал запроса с закритым началом и открытым окончанием.
   */
  CSOE( "CSOE", STR_D_QIT_CSOE, STR_N_QIT_CSOE, true, true ),

  /**
   * Интерал запроса с закритым началом и окончанием.
   */
  CSCE( "CSCE", STR_D_QIT_CSCE, STR_N_QIT_CSCE, true, true );

  private final String  id;
  private final String  description;
  private final String  name;
  private final boolean openStart;
  private final boolean openEnd;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - краткое название константы
   * @param aOpenStart boolean - признак закртого начала
   * @param aOpenEnd boolean - признак закртого окончания
   */
  EQueryIntervalType( String aId, String aDescr, String aName, boolean aOpenStart, boolean aOpenEnd ) {
    id = aId;
    description = aDescr;
    name = aName;
    openStart = aOpenStart;
    openEnd = aOpenEnd;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает признак открытого начала интервала запроса.
   * <p>
   * Если начало открыто, и в запрашиваемых данных нет элементов с меткой времени <b>точно</b> совпадающим с
   * {@link IQueryInterval#startTime()}, то вернется как минимум одно значение со временем меньшим, чем начало
   * интервала. При этом естественно, что значения с меткой времени меньше начала интервала должно существовать в
   * запрашиваемых данных.
   *
   * @return boolean - признак открытого начала
   */
  public boolean isStartOpen() {
    return openStart;
  }

  /**
   * Возвращает признак открытого окончания интервала запроса.
   * <p>
   * Если окончание открыто, и в запрашиваемых данных нет элементов с меткой времени <b>точно</b> совпадающим с
   * {@link IQueryInterval#endTime()}, то вернется как минимум одно значение со временем большим, чем окончание
   * интервала. При этом естественно, что значения с меткой времени больше окончания интервала должно существовать в
   * запрашиваемых данных.
   *
   * @return boolean - признак открытого окончания
   */
  public boolean isEndOpen() {
    return openEnd;
  }

  // ------------------------------------------------------------------------------------
  // Статическое API
  //

  /**
   * Возвращает константу, соответствующую признакам открытости границ интервала.
   *
   * @param aIsStartOpen boolean - признак открытого начала
   * @param aIsEndOpen boolean - признак открытого окончания
   * @return {@link EQueryIntervalType} - соответствующая константа
   */
  public static EQueryIntervalType fromFlags( boolean aIsStartOpen, boolean aIsEndOpen ) {
    if( aIsStartOpen ) {
      if( aIsEndOpen ) {
        return OSOE;
      }
      return OSCE;
    }
    if( aIsEndOpen ) {
      return CSOE;
    }
    return CSCE;
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - неет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным описанием.
   *
   * @param aDescription String - описание искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным описанием существует;<br>
   *         <b>false</b> - неет константы с таким описанием.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - неет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByName( String aName ) {
    return findByNameOrNull( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return EIntervalType - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EQueryIntervalType findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EQueryIntervalType item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по идентификатору или выбрасывает исключение.
   *
   * @param aId String - идентификатор искомой константы
   * @return EIntervalType - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EQueryIntervalType findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return EIntervalType - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EQueryIntervalType findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EQueryIntervalType item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по описанию или выбрасывает исключение.
   *
   * @param aDescription String - описание искомой константы
   * @return EIntervalType - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EQueryIntervalType findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return EIntervalType - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EQueryIntervalType findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EQueryIntervalType item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени или выбрасывает исключение.
   *
   * @param aName String - имя искомой константы
   * @return EIntervalType - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EQueryIntervalType findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}
