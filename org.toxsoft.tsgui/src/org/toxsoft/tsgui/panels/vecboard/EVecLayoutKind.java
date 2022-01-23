package org.toxsoft.tsgui.panels.vecboard;

import static org.toxsoft.tsgui.panels.vecboard.ITsResources.*;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Виды раскладок контролей {@link IVecLayout}.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
public enum EVecLayoutKind
    implements IStridable {

  /**
   * Раскладка контролей по четырем сторонам и центру.
   * <p>
   * Раскладку {@link IVecLayout} этого вида нужно приводить к {@link IVecBorderLayout}.
   */
  BORDER( "Border", STR_N_LK_BORDER, STR_D_LK_BORDER ),

  /**
   * Раскладка контролей одним столбцом, вертикальной лесенкой.
   * <p>
   * Раскладку {@link IVecLayout} этого вида нужно приводить к {@link IVecLadderLayout}.
   */
  LADDER( "Ladder", STR_N_LK_LADDER, STR_D_LK_LADDER ),

  /**
   * Раскладка контролей в виде вкладок.
   * <p>
   * Раскладку {@link IVecLayout} этого вида нужно приводить к {@link IVecTabLayout}.
   */
  TABS( "Tabs", STR_N_LK_TABS, STR_D_LK_TABS ),

  /**
   * Раскладка контролей в ряд с разделением перемещаемой планкой.
   * <p>
   * Раскладку {@link IVecLayout} этого вида нужно приводить к {@link IVecSashLayout}.
   */
  SASH( "Sash", STR_N_LK_SASH, STR_D_LK_SASH ),

  /**
   * Раскладка контролей в ряд.
   * <p>
   * Раскладку {@link IVecLayout} этого вида нужно приводить к {@link IVecRowLayout}.
   */
  ROW( "Row", STR_N_LK_ROW, STR_D_LK_ROW ),

  ;

  private final String id;
  private final String name;
  private final String description;

  EVecLayoutKind( String aId, String aName, String aDescr ) {
    id = aId;
    name = aName;
    description = aDescr;
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

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return ELayoutKind - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EVecLayoutKind findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EVecLayoutKind item : values() ) {
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
   * @return ELayoutKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EVecLayoutKind findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return ELayoutKind - найденная константа, или null если нет константы с таким идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EVecLayoutKind findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EVecLayoutKind item : values() ) {
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
   * @return ELayoutKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EVecLayoutKind findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

}
