package org.toxsoft.core.tsgui.ved.extra.tools.impl;

import static org.toxsoft.core.tsgui.ved.core.impl.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Kind of {@link IScreenObject} entities.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public enum EScreenObjectKind
    implements IStridable {

  COMPONENT( "component", STR_N_SOK_COMPONENT, STR_D_SOK_COMPONENT ), //$NON-NLS-1$

  VERTEX( "vertex", STR_N_SOK_VERTEX, STR_D_SOK_VERTEX ), //$NON-NLS-1$

  ;

  private static IStridablesListEdit<EScreenObjectKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  EScreenObjectKind( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link EScreenObjectKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EScreenObjectKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EScreenObjectKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EScreenObjectKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EScreenObjectKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EScreenObjectKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EScreenObjectKind item : values() ) {
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
   * @return {@link EScreenObjectKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EScreenObjectKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
