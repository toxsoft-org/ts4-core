package org.toxsoft.core.tslib.bricks.wub;

import static org.toxsoft.core.tslib.bricks.wub.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Possible state of the WUB unit.
 * <p>
 * It is important that constants are listed in the order of the unit lifecycle from the birth {@link #CREATED} to the
 * last state {@link #STOPPED}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EWubUnitState
    implements IStridable {

  CREATED( "created", STR_EWUS_CREATED, STR_EWUS_CREATED_D ), //$NON-NLS-1$

  INITED( "inited", STR_EWUS_INITED, STR_EWUS_INITED_D ), //$NON-NLS-1$

  STARTED( "started", STR_EWUS_STARTED_D, STR_EWUS_STARTED_D ), //$NON-NLS-1$

  STOP_QUERIED( "stopQueried", STR_EWUS_STOP_QUERIED_D, STR_EWUS_STOP_QUERIED_D ), //$NON-NLS-1$

  STOPPED( "stopped", STR_EWUS_STOPPED_D, STR_EWUS_STOPPED_D ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EWubUnitState"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EWubUnitState> KEEPER = new StridableEnumKeeper<>( EWubUnitState.class );

  private static IStridablesListEdit<EWubUnitState> list = null;

  private final String id;
  private final String name;
  private final String description;

  EWubUnitState( String aId, String aName, String aDescription ) {
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
   * Determines if this state is greater or equal then <code>aState</code>.
   *
   * @param aState {@link EWubUnitState} - asked state
   * @return boolean - <code>true</code> if this state is the same or after the <code>aState</code>
   */
  public boolean isSameOrAfter( EWubUnitState aState ) {
    TsNullArgumentRtException.checkNull( aState );
    return this.ordinal() >= aState.ordinal();
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EWubUnitState} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EWubUnitState> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EWubUnitState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EWubUnitState getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EWubUnitState} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EWubUnitState findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EWubUnitState item : values() ) {
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
   * @return {@link EWubUnitState} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EWubUnitState getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
