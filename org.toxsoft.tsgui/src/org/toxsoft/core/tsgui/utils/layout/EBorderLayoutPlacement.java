package org.toxsoft.core.tsgui.utils.layout;

import static org.toxsoft.core.tsgui.utils.layout.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Константы расположения дочерных контролей в раскладке {@link BorderLayout}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EBorderLayoutPlacement
    implements IStridable {

  CENTER( "center", STR_N_CENTER, STR_D_CENTER ), //$NON-NLS-1$

  NORTH( "north", STR_N_NORTH, STR_D_NORTH ), //$NON-NLS-1$

  SOUTH( "south", STR_N_SOUTH, STR_D_SOUTH ), //$NON-NLS-1$

  EAST( "east", STR_N_EAST, STR_D_EAST ), //$NON-NLS-1$

  WEST( "west", STR_N_WEST, STR_D_WEST ); //$NON-NLS-1$

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "EBorderLayoutPlacement"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EBorderLayoutPlacement> KEEPER =
      new StridableEnumKeeper<>( EBorderLayoutPlacement.class );

  private static IStridablesListEdit<EBorderLayoutPlacement> list = null;

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
  EBorderLayoutPlacement( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
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

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link EBorderLayoutPlacement} &gt; - список всех констант
   */
  public static IStridablesList<EBorderLayoutPlacement> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

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
   * @return EBorderLayoutPlacement - найденная константа, или <code>null</code> если нет константы с таимк
   *         идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EBorderLayoutPlacement findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EBorderLayoutPlacement - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EBorderLayoutPlacement getById( String aId ) {
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
   * @return EBorderLayoutPlacement - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EBorderLayoutPlacement findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EBorderLayoutPlacement item : values() ) {
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
   * @return EBorderLayoutPlacement - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EBorderLayoutPlacement getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
