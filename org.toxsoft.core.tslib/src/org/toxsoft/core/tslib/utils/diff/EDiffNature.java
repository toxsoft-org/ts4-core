package org.toxsoft.core.tslib.utils.diff;

import static org.toxsoft.core.tslib.utils.diff.ITsResources.*;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Possible differences between elements of compared containers.
 *
 * @author hazard157
 */
public enum EDiffNature
    implements IStridable {

  /**
   * Requested element is not present in both containers.
   */
  NONE( "none", STR_DN_NONE, STR_DN_NONE_D ), //$NON-NLS-1$

  /**
   * Element exists only in left container.
   */
  LEFT( "left", STR_DN_LEFT, STR_DN_LEFT_D ), //$NON-NLS-1$

  /**
   * Element exists only in right container.
   */
  RIGHT( "right", STR_DN_RIGHT, STR_DN_RIGHT_D ), //$NON-NLS-1$

  /**
   * Left and right element are equal.
   */
  EQUAL( "equal", STR_DN_EQUAL, STR_DN_EQUAL_D ), //$NON-NLS-1$

  /**
   * Left and right elements are not equal.
   */
  DIFF( "diff", STR_DN_DIFF, STR_DN_DIFF_D ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EDiffNature"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EDiffNature> KEEPER = new StridableEnumKeeper<>( EDiffNature.class );

  private static IStridablesListEdit<EDiffNature> list = null;

  private final String id;
  private final String name;
  private final String description;

  EDiffNature( String aId, String aName, String aDescription ) {
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
   * Compares two elements from left and right containers.
   *
   * @param aLeft Object - element from left container, may be <code>null</code> if none found
   * @param aRight Object - element from right container, may be <code>null</code> if none found
   * @return {@link EDiffNature} - comparison result
   */
  public static EDiffNature diff( Object aLeft, Object aRight ) {
    if( aLeft == null && aRight == null ) {
      return EDiffNature.NONE;
    }
    if( aRight == null ) {
      return EDiffNature.LEFT;
    }
    if( aLeft == null ) {
      return EDiffNature.RIGHT;
    }
    boolean isEq = Objects.equals( aLeft, aRight );
    if( isEq ) {
      return EDiffNature.EQUAL;
    }
    return EDiffNature.DIFF;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EDiffNature} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EDiffNature> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EDiffNature} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EDiffNature getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EDiffNature} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EDiffNature findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EDiffNature item : values() ) {
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
   * @return {@link EDiffNature} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EDiffNature getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
