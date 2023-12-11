package org.toxsoft.core.tsgui.graphics.vpcalc;

import static org.toxsoft.core.tsgui.graphics.vpcalc.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * When to use the {@link CalculationStrategySettings#fulcrum()} parameter.
 *
 * @author hazard157
 */
public enum EVpFulcrumUsageStartegy
    implements IStridable {

  /**
   * The content always will be placed relative to the viewport as specified by the fulcrum.
   */
  ALWAYS( "always", STR_FUS_ALWAYS, STR_FUS_ALWAYS_D ), //$NON-NLS-1$

  /**
   * If content is smaller then viewport the fulcrum will be applied, for bigger content fulcrum is just a hint.
   */
  INSIDE( "inside", STR_FUS_INSIDE, STR_FUS_INSIDE_D ), //$NON-NLS-1$

  /**
   * Fulcrum will be only the hint if no other placement rules are applied.
   */
  HINT( "never", STR_FUS_HINT, STR_FUS_HINT_D ), //$NON-NLS-1$

  ;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EVpFulcrumUsageStartegy"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EVpFulcrumUsageStartegy> KEEPER =
      new StridableEnumKeeper<>( EVpFulcrumUsageStartegy.class );

  private static IStridablesListEdit<EVpFulcrumUsageStartegy> list = null;

  private final String id;
  private final String name;
  private final String description;

  EVpFulcrumUsageStartegy( String aId, String aName, String aDescription ) {
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
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EVpFulcrumUsageStartegy} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EVpFulcrumUsageStartegy> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EVpFulcrumUsageStartegy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EVpFulcrumUsageStartegy getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVpFulcrumUsageStartegy} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EVpFulcrumUsageStartegy findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVpFulcrumUsageStartegy item : values() ) {
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
   * @return {@link EVpFulcrumUsageStartegy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EVpFulcrumUsageStartegy getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
