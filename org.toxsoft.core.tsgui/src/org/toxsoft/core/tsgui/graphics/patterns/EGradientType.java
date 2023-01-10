package org.toxsoft.core.tsgui.graphics.patterns;

import static org.toxsoft.core.tsgui.graphics.patterns.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The gradient type.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public enum EGradientType
    implements IStridable {

  NONE( "identifier", STR_N_GRADIENT_NONE, STR_D_GRADIENT_NONE ), //$NON-NLS-1$

  LINEAR( "linear", STR_N_GRADIENT_LINEAR, STR_D_GRADIENT_LINEAR ), //$NON-NLS-1$

  RADIAL( "radial", STR_N_GRADIENT_RADIAL, STR_D_GRADIENT_RADIAL ), //$NON-NLS-1$

  CYLINDER( "cylinder", STR_N_GRADIENT_CYLINDER, STR_D_GRADIENT_CYLINDER ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EGradientType"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EGradientType> KEEPER = new StridableEnumKeeper<>( EGradientType.class );

  private static IStridablesListEdit<EGradientType> list = null;

  private final String id;
  private final String name;
  private final String description;

  EGradientType( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link EGradientType} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EGradientType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EGradientType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EGradientType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EGradientType} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EGradientType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EGradientType item : values() ) {
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
   * @return {@link EGradientType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EGradientType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
