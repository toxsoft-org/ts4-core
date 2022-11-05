package org.toxsoft.core.tsgui.graphics.lines;

import static org.toxsoft.core.tsgui.graphics.lines.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The outline drawing kind.
 *
 * @author goga
 */
public enum ETsOutlineKind
    implements IStridable {

  /**
   * No outline will be drawn.
   */
  NONE( NONE_ID, STR_N_OK_NONE, STR_D_OK_NONE ),

  /**
   * Simple outline specified by line and color.
   */
  SIMPLE( "simple", STR_N_OK_SIMPLE, STR_D_OK_SIMPLE ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETsOutlineKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETsOutlineKind> KEEPER = new StridableEnumKeeper<>( ETsOutlineKind.class );

  private static IStridablesListEdit<ETsOutlineKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  ETsOutlineKind( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link ETsOutlineKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsOutlineKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsOutlineKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsOutlineKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsOutlineKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsOutlineKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsOutlineKind item : values() ) {
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
   * @return {@link ETsOutlineKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsOutlineKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
