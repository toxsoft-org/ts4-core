package org.toxsoft.core.tsgui.chart.api;

import static org.toxsoft.core.tsgui.chart.api.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Analog graph drawing types.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public enum EGraphicRenderingKind
    implements IStridable {

  LINE( "Line", STR_GRK_LINE, STR_GRK_LINE_D ), //$NON-NLS-1$

  LADDER( "Ladder", STR_GRK_LADDER, STR_GRK_LADDER_D ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EGraphicRenderingKind"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EGraphicRenderingKind> KEEPER =
      new StridableEnumKeeper<>( EGraphicRenderingKind.class );

  private final String id;
  private final String nmName;
  private final String description;

  EGraphicRenderingKind( String aId, String aName, String aDescr ) {
    id = aId;
    nmName = aName;
    description = aDescr;
  }

  // --------------------------------------------------------------------------
  // IStridable
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
    return nmName;
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

  /**
   * Определяет, существует ли константа с заданным именем.
   *
   * @param aName String - имя {@link #nmName()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким именем;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByName( String aName ) {
    return findByNameOrNull( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, возвращает null.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EGraphicRenderingKind findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EGraphicRenderingKind item : values() ) {
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
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EGraphicRenderingKind findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, возвращает null.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EGraphicRenderingKind findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EGraphicRenderingKind item : values() ) {
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
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EGraphicRenderingKind findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Находит константу с заданным именем, а если нет такой константы, возвращает null.
   *
   * @param aName String - имя {@link #nmName()} константы
   * @return EGraphicRenderingKind - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EGraphicRenderingKind findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EGraphicRenderingKind item : values() ) {
      if( item.nmName.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным именем, а если нет такой константы, выбрасывает исключение.
   *
   * @param aName String - имя {@link #nmName()} константы
   * @return EGraphicRenderingKind - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EGraphicRenderingKind findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}
