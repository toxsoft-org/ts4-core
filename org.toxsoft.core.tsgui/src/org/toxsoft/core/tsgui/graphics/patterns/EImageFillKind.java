package org.toxsoft.core.tsgui.graphics.patterns;

import static org.toxsoft.core.tsgui.graphics.patterns.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Types of background filling when filling with an image.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public enum EImageFillKind
    implements IStridable {

  CENTER( "center", STR_EIFK_CENTER, STR_EIFK_CENTER_D ), //$NON-NLS-1$

  FIT( "fit", STR_EIFK_FIT, STR_EIFK_FIT_D ), //$NON-NLS-1$

  TILE( "tile", STR_EIFK_TILE, STR_EIFK_TILE_D ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EImageFillKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EImageFillKind> KEEPER = new StridableEnumKeeper<>( EImageFillKind.class );

  private static IStridablesListEdit<EImageFillKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  EImageFillKind( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link EImageFillKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EImageFillKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EImageFillKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EImageFillKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EImageFillKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EImageFillKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EImageFillKind item : values() ) {
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
   * @return {@link EImageFillKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EImageFillKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
