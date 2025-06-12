package org.toxsoft.core.tslib.utils;

import static org.toxsoft.core.tslib.utils.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * How to use general query parameter.
 * <p>
 * Determines how to use some query parameter. Query is a any method returning collection of items where content of the
 * result depends on argument values. This <code>enum</code> may be used both as parameter accompanying argument or as a
 * boolean parameter query.
 * <p>
 * For example, method <br>
 * <code>IStringList listUsers(String aName, EQueryParamUsage aNameUsage, EQueryParamUsage aActiveUser );</code><br>
 * selects users by name and active state. Depending on <code><i>aNameUsage</i></code> the result will include users
 * with any name, name not matching or matching argument <code><i>aName</i></code>. Value of the argument
 * <code>a<i>ActiveUser</i></code> determines idf result will include anboth active/inactive, only inactive or only
 * active users.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EQueryParamUsage
    implements IStridable {

  DONT_CARE( "dont_care", STR_EQPU_DONT_CARE, STR_EQPU_DONT_CARE_D, false, false ), //$NON-NLS-1$

  EXCLUDE( "exlude", STR_EQPU_EXCLUDE, STR_EQPU_EXCLUDE_D, true, false ), //$NON-NLS-1$

  INCLUDE( "include", STR_EQPU_INCLUDE, STR_EQPU_INCLUDE_D, true, true ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EQueryParamUsage"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EQueryParamUsage> KEEPER = new StridableEnumKeeper<>( EQueryParamUsage.class );

  private static IStridablesListEdit<EQueryParamUsage> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean consider;
  private final boolean include;

  EQueryParamUsage( String aId, String aName, String aDescription, boolean aConsider, boolean aInclude ) {
    id = aId;
    name = aName;
    description = aDescription;
    consider = aConsider;
    include = aInclude;
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
   * Determines if this parameter should be considered when executing query.
   *
   * @return boolean - <code>true</code> - proceed with this parameter, <code>false</code> - ignore parameter
   */
  public boolean isConsidered() {
    return consider;
  }

  /**
   * Determines if elements matching query parameter will be included in the results.
   *
   * @return boolean - <code>true</code> include matching elements, <code>false</code> include non-=matching elements
   */
  public boolean isIncluded() {
    return include;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EQueryParamUsage} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EQueryParamUsage> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EQueryParamUsage} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EQueryParamUsage getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EQueryParamUsage} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EQueryParamUsage findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EQueryParamUsage item : values() ) {
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
   * @return {@link EQueryParamUsage} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EQueryParamUsage getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
