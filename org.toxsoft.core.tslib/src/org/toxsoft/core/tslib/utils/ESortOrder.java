package org.toxsoft.core.tslib.utils;

import static org.toxsoft.core.tslib.utils.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Sort order.
 *
 * @author hazard157
 */
public enum ESortOrder
    implements IStridable {

  /**
   * No sorting.
   */
  NONE( "None", STR_SORT_NONE, STR_SORT_NONE_D ), //$NON-NLS-1$

  /**
   * Ascending.
   */
  ASCENDING( "Ascending", STR_SORT_ASCENDING, STR_SORT_ASCENDING_D ), //$NON-NLS-1$

  /**
   * Descending.
   */
  DESCENDING( "Descending", STR_SORT_DESCENDING, STR_SORT_DESCENDING_D ), //$NON-NLS-1$

  ;

  // TODO TRANSLATE

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "ESortOrder"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ESortOrder> KEEPER = new StridableEnumKeeper<>( ESortOrder.class );

  private static IStridablesListEdit<ESortOrder> list = null;

  private final String id;
  private final String name;
  private final String description;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентификатор (ИД-путь) константы
   * @param aName String - краткое удобочитаемое название константы
   * @param aDescription String - отображаемое описание константы
   */
  ESortOrder( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  // --------------------------------------------------------------------------
  // IStridable
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

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link ESortOrder} &gt; - список всех констант
   */
  public static IStridablesList<ESortOrder> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Возвращает следующий порядок сортировки, вплоть до максимального.
   *
   * @return {@link ESortOrder} - следующий порядок сортировки
   */
  public ESortOrder nextSortOrder() {
    if( ordinal() == asList().size() - 1 ) {
      return this;
    }
    return asList().get( ordinal() + 1 );
  }

  /**
   * Возвращает следующий порядок сортировки, с переходом на первый после последнего.
   *
   * @return {@link ESortOrder} - следующий порядок сортировки
   */
  public ESortOrder nextSortOrderW() {
    if( ordinal() == asList().size() - 1 ) {
      return asList().first();
    }
    return asList().get( ordinal() + 1 );
  }

  /**
   * Возвращает предыдущий порядок сортировки, вплоть до минимаьного.
   *
   * @return {@link ESortOrder} - предыдущий порядок сортировки
   */
  public ESortOrder prevSortOrder() {
    if( ordinal() == 0 ) {
      return this;
    }
    return asList().get( ordinal() - 1 );
  }

  /**
   * Возвращает предыдущий порядок сортировки, с переходом на последний после первого.
   *
   * @return {@link ESortOrder} - предыдущий порядок сортировки
   */
  public ESortOrder prevSortOrderW() {
    if( ordinal() == 0 ) {
      return asList().get( asList().size() - 1 );
    }
    return asList().get( ordinal() - 1 );
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
   *         <b>false</b> - нет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Находит константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ESortOrder - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ESortOrder findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ESortOrder - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ESortOrder getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - нет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  /**
   * Находит константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ESortOrder - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ESortOrder findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ESortOrder item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ESortOrder - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static ESortOrder getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
