package org.toxsoft.core.tsgui.chart.api;

import static org.toxsoft.core.tsgui.chart.api.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Permissible values of the division value of the X-scale (time scale).
 *
 * @author hazard157, vs
 */
@SuppressWarnings( "javadoc" )
public enum ETimeUnit
    implements IStridable {

  SEC01( "sec01", STR_ETU_SEC01, STR_ETU_SEC01_D, 1 * 1000L, 0, 0 ), //$NON-NLS-1$
  SEC02( "sec02", STR_ETU_SEC02, STR_ETU_SEC02_D, 2 * 1000L, 4, 1 ), //$NON-NLS-1$
  SEC03( "sec03", STR_ETU_SEC03, STR_ETU_SEC03_D, 3 * 1000L, 2, 0 ), //$NON-NLS-1$
  SEC05( "sec05", STR_ETU_SEC05, STR_ETU_SEC05_D, 5 * 1000L, 4, 0 ), //$NON-NLS-1$
  SEC10( "sec10", STR_ETU_SEC10, STR_ETU_SEC10_D, 10 * 1000L, 4, 1 ), //$NON-NLS-1$
  SEC15( "sec15", STR_ETU_SEC15, STR_ETU_SEC15_D, 15 * 1000L, 4, 2 ), //$NON-NLS-1$
  SEC20( "sec20", STR_ETU_SEC20, STR_ETU_SEC20_D, 20 * 1000L, 1, 1 ), //$NON-NLS-1$
  SEC30( "sec30", STR_ETU_SEC30, STR_ETU_SEC30_D, 30 * 1000L, 29, 2 ), //$NON-NLS-1$
  MIN01( "min01", STR_ETU_MIN01, STR_ETU_MIN01_D, 1 * 60 * 1000L, 4, 1 ), //$NON-NLS-1$
  MIN05( "min05", STR_ETU_MIN05, STR_ETU_MIN05_D, 5 * 60 * 1000L, 4, 0 ), //$NON-NLS-1$
  MIN10( "min10", STR_ETU_MIN10, STR_ETU_MIN10_D, 10 * 60 * 1000L, 4, 1 ), //$NON-NLS-1$
  MIN15( "min15", STR_ETU_MIN15, STR_ETU_MIN15_D, 15 * 60 * 1000L, 2, 0 ), //$NON-NLS-1$
  MIN20( "min20", STR_ETU_MIN20, STR_ETU_MIN20_D, 20 * 60 * 1000L, 4, 1 ), //$NON-NLS-1$
  MIN30( "min30", STR_ETU_MIN30, STR_ETU_MIN30_D, 30 * 60 * 1000L, 2, 1 ), //$NON-NLS-1$
  HOUR01( "hour01", STR_ETU_HOUR01, STR_ETU_HOUR01_D, 1 * 60 * 60 * 1000L, 4, 1 ), //$NON-NLS-1$
  HOUR02( "hour02", STR_ETU_HOUR02, STR_ETU_HOUR02_D, 2 * 60 * 60 * 1000L, 2, 1 ), //$NON-NLS-1$
  HOUR04( "hour04", STR_ETU_HOUR04, STR_ETU_HOUR04_D, 4 * 60 * 60 * 1000L, 1, 0 ), //$NON-NLS-1$
  HOUR08( "hour08", STR_ETU_HOUR08, STR_ETU_HOUR08_D, 8 * 60 * 60 * 1000L, 7, 1 ), //$NON-NLS-1$
  HOUR12( "hour12", STR_ETU_HOUR12, STR_ETU_HOUR12_D, 12 * 60 * 60 * 1000L, 11, 1 ), //$NON-NLS-1$
  DAY( "day", STR_ETU_DAY, STR_ETU_DAY_D, 24 * 60 * 60 * 1000L, 7, 0 ), //$NON-NLS-1$
  WEEK( "week", STR_ETU_WEEK, STR_ETU_WEEK_D, 7 * 24 * 60 * 60 * 1000L, 7, 0 ), //$NON-NLS-1$
  YEAR( "year", STR_ETU_YEAR, STR_ETU_YEAR_D, -1, 0, 0 ); //$NON-NLS-1$

  private final String id;
  private final String name;
  private final String description;
  private final long   millisecs;
  private final int    littTicksQtty;
  private final int    midTicksQtty;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETimeUnit"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETimeUnit> KEEPER = new StridableEnumKeeper<>( ETimeUnit.class );

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
