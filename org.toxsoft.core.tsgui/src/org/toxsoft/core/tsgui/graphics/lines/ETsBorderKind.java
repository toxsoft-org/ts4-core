package org.toxsoft.core.tsgui.graphics.lines;

import static org.toxsoft.core.tsgui.graphics.lines.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Rectanglar border (frame) drawing kind.
 *
 * @author hazard157
 */
public enum ETsBorderKind
    implements IStridable {

  /**
   * No border will be drawn.
   */
  NONE( NONE_ID, STR_N_BK_NONE, STR_D_BK_NONE ),

  /**
   * Single line border.
   */
  SINGLE( "single", STR_N_BK_SINGLE, STR_D_BK_SINGLE ), //$NON-NLS-1$

  /**
   * Double line border.
   */
  DOUBLE( "double", STR_N_BK_DOUBLE, STR_D_BK_DOUBLE ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETsBorderKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsBorderKind> KEEPER = new StridableEnumKeeper<>( ETsBorderKind.class );

  private static IStridablesListEdit<ETsBorderKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  ETsBorderKind( String aId, String aName, String aDescription ) {
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
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ETsBorderKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsBorderKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsBorderKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsBorderKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsBorderKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsBorderKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsBorderKind item : values() ) {
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
   * @return {@link ETsBorderKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsBorderKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
