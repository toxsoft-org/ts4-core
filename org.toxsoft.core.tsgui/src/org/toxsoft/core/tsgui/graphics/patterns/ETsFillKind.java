package org.toxsoft.core.tsgui.graphics.patterns;

import static org.toxsoft.core.tsgui.graphics.patterns.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Types of filling drawn shapes.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public enum ETsFillKind
    implements IStridable {

  NONE( "none", STR_TFK_NONE, STR_TFK_NONE_D ), //$NON-NLS-1$

  SOLID( "solid", STR_TFK_SOLID, STR_TFK_SOLID_D ), //$NON-NLS-1$

  IMAGE( "image", STR_TFK_IMAGE, STR_TFK_IMAGE_D ), //$NON-NLS-1$

  GRADIENT( "gradient", STR_TFK_GRADIENT, STR_TFK_GRADIENT_D ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ETsFillKind"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ETsFillKind> KEEPER = new StridableEnumKeeper<>( ETsFillKind.class );

  private static IStridablesListEdit<ETsFillKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  ETsFillKind( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link ETsFillKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETsFillKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETsFillKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETsFillKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETsFillKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETsFillKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsFillKind item : values() ) {
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
   * @return {@link ETsFillKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETsFillKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
