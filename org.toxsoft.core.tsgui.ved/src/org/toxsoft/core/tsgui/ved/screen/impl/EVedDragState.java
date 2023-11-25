package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.impl.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * State of the VED item dragging.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EVedDragState
    implements IStridable {

  START( "dragStart", STR_DRAG_START, STR_DRAG_START_D ), //$NON-NLS-1$

  DRAGGING( "dragDragging", STR_DRAGGING, STR_DRAGGING_D ), //$NON-NLS-1$

  FINISH( "dragFinish", STR_DRAG_FINISHED, STR_DRAG_FINISHED_D ), //$NON-NLS-1$

  CANCEL( "dragCancel", STR_DRAG_CANCELED, STR_DRAG_CANCELED_D );//$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EVedDragState"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EVedDragState> KEEPER = new StridableEnumKeeper<>( EVedDragState.class );

  private static IStridablesListEdit<EVedDragState> list = null;

  private final String id;
  private final String name;
  private final String description;

  EVedDragState( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link EVedDragState} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EVedDragState> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EVedDragState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EVedDragState getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVedDragState} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EVedDragState findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVedDragState item : values() ) {
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
   * @return {@link EVedDragState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EVedDragState getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
