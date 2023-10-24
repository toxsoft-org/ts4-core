package org.toxsoft.core.tsgui.graphics.cursors;

import static org.toxsoft.core.tsgui.graphics.cursors.ITsResources.*;

import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Константы предопределенных курсоров мыши.
 * <p>
 * Включает в себя типы курсоров, определенные константами SWT.CURSOR_XXX.
 *
 * @author vs
 */
@SuppressWarnings( "nls" )
public enum ECursorType
    implements IStridable {

  /**
   * Системный курсор: стрелка.
   */
  ARROW( "Arrow", STR_N_CUR_ARROW, SWT.CURSOR_ARROW ),

  /**
   * Системный курсор: ожидание.
   */
  WAIT( "Wait", STR_N_CUR_WAIT, SWT.CURSOR_WAIT ),

  /**
   * Системный курсор: перекрестие.
   */
  CROSS( "Cross", STR_N_CUR_CROSS, SWT.CURSOR_CROSS ),

  /**
   * Системный курсор: запуск.
   */
  APPSTARTING( "AppStarting", STR_N_CUR_APPSTARTING, SWT.CURSOR_APPSTARTING ),

  /**
   * Системный курсор: подсказка.
   */
  HELP( "Help", STR_N_CUR_HELP, SWT.CURSOR_HELP ),

  /**
   * Системный курсор: луч (текстовый).
   */
  I_BEAM( "IBeam", STR_N_CUR_IBEAM, SWT.CURSOR_IBEAM ),

  /**
   * Системный курсор: рука.
   */
  HAND( "Hand", STR_N_CUR_HAND, SWT.CURSOR_HAND ),

  /**
   * Системный курсор: стрелка вверх.
   */
  UP_ARROW( "UpArrow", STR_N_CUR_UPARROW, SWT.CURSOR_UPARROW ),

  /**
   * Системный курсор: изменить размер во всех направлениях.
   */
  SIZSTR_N_ALL( "SizeAll", STR_N_CUR_SIZEALL, SWT.CURSOR_SIZEALL ),

  /**
   * Системный курсор: изменить размер север-восток-юг-запад.
   */
  // RCP_RAP: константы SWT.CURSOR_SIZENESW нет в RAP
  // SIZSTR_N_NESW( "SizeNorthEastSouthWest", STR_N_CUR_SIZENESW, SWT.CURSOR_SIZENESW ),

  /**
   * Системный курсор: изменить размер север-юг.
   */
  SIZSTR_N_NORTH_SOUTH( "", STR_N_CUR_SIZENS, SWT.CURSOR_SIZENS ),

  /**
   * Системный курсор: изменить размер север-запад-юг-восток.
   */
  // RCP_RAP: константы SWT.CURSOR_SIZENWSE нет в RAP
  // SIZENWSE( "SizeNorthWestSouthEast", STR_N_CUR_SIZENWSE, SWT.CURSOR_SIZENWSE ),

  /**
   * Системный курсор: изменить размер запад_восток.
   */
  SIZSTR_N_WEST_EAST( "SizeWestEast", STR_N_CUR_SIZEWE, SWT.CURSOR_SIZEWE ),

  /**
   * Системный курсор: изменить размер север.
   */
  SIZSTR_N_NORTH( "SizeNorth", STR_N_CUR_SIZEN, SWT.CURSOR_SIZEN ),

  /**
   * Системный курсор: изменить размер юг.
   */
  SIZSTR_N_SOUTH( "SizeSouth", STR_N_CUR_SIZES, SWT.CURSOR_SIZES ),

  /**
   * Системный курсор: изменить размер восток.
   */
  SIZSTR_N_EAST( "SizeEast", STR_N_CUR_SIZEE, SWT.CURSOR_SIZEE ),

  /**
   * Системный курсор: изменить размер запад.
   */
  SIZSTR_N_WEST( "SizeWest", STR_N_CUR_SIZEW, SWT.CURSOR_SIZEW ),

  /**
   * Системный курсор: изменить размер север-восток.
   */
  SIZSTR_N_NORTH_EAST( "SizeNorthEast", STR_N_CUR_SIZENE, SWT.CURSOR_SIZENE ),

  /**
   * Системный курсор: изменить размер юг-восток.
   */
  SIZSTR_N_SOUTH_EAST( "SizeSouthEast", STR_N_CUR_SIZESE, SWT.CURSOR_SIZESE ),

  /**
   * Системный курсор: изменить размер юг-запад.
   */
  SIZSTR_N_SOUTH_WEST( "SizeSouthWest", STR_N_CUR_SIZESW, SWT.CURSOR_SIZESW ),

  /**
   * Системный курсор: изменить размер север-запад.
   */
  SIZSTR_N_NORTH_WEST( "SizeNorthWest", STR_N_CUR_SIZENW, SWT.CURSOR_SIZENW ),

  /**
   * Системный курсор: отсутствие курсора.
   */
  NONE( "None", STR_N_CUR_NONE, SWT.CURSOR_NO );

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ECursorType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ECursorType> KEEPER = new StridableEnumKeeper<>( ECursorType.class );

  private static IStridablesListEdit<ECursorType> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    swtCursorCode;

  ECursorType( String aId, String aName, int aSwtCursorCode ) {
    id = aId;
    name = aName;
    description = aName;
    swtCursorCode = aSwtCursorCode;
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
   * Returns SWT cursor type (one of the SWT.CURSOR_XXX).
   * <p>
   * For custom cursors without corresponding SWT cursor returns -1.
   *
   * @return int - SWT cursor type (one of the SWT.CURSOR_XXX) or -1
   */
  public int swtCursorCode() {
    return swtCursorCode;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ECursorType} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ECursorType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the cursor type for rectangular area resising.
   * <p>
   * The argument describes which edge or corner will be dragged fro rectangle resizing. {@link ETsFulcrum#CENTER}
   * assumes cursor for object moving, not resizing.
   *
   * @param aFulcrum {@link ETsFulcrum} - the drag start point
   * @return {@link ECursorType} - the cursor kind
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ECursorType cursorForFulcrum( ETsFulcrum aFulcrum ) {
    TsNullArgumentRtException.checkNull( aFulcrum );
    switch( aFulcrum ) {
      case TOP_CENTER:
      case BOTTOM_CENTER:
        return ECursorType.SIZSTR_N_NORTH_SOUTH;
      case CENTER:
        return ECursorType.SIZSTR_N_ALL;
      case LEFT_CENTER:
      case RIGHT_CENTER:
        return ECursorType.SIZSTR_N_WEST_EAST;
      case LEFT_TOP:
        return ECursorType.SIZSTR_N_NORTH_WEST;
      case RIGHT_BOTTOM:
        return ECursorType.SIZSTR_N_SOUTH_EAST;
      case RIGHT_TOP:
        return ECursorType.SIZSTR_N_NORTH_EAST;
      case LEFT_BOTTOM:
        return ECursorType.SIZSTR_N_SOUTH_WEST;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ECursorType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ECursorType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ECursorType} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ECursorType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ECursorType item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ECursorType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ECursorType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
