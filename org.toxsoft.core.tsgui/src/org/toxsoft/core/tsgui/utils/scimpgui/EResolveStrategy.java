package org.toxsoft.core.tsgui.utils.scimpgui;

import static org.toxsoft.core.tsgui.utils.scimpgui.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Strategy for resolving key conflicts when importing a collection.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EResolveStrategy
    implements IStridable {

  OVERWRITE_EXISTING( "overwrite_existing", //$NON-NLS-1$
      STR_OVERWRITE_EXISTING, STR_OVERWRITE_EXISTING_D ),

  ASK_EACH_ITEM_OVERWRITE( "ask_each_item_overwrite", //$NON-NLS-1$
      STR_ASK_EACH_ITEM_OVERWRITE, STR_ASK_EACH_ITEM_OVERWRITE_D ),

  BYPASS_EXISTING_ITEMS( "bypass_existing_items", //$NON-NLS-1$
      STR_BYPASS_EXISTING_ITEMS, STR_BYPASS_EXISTING_ITEMS_D ),

  CANCEL_IF_ANY_ITEM_EXISTS( "cancel_if_any_item_exists", //$NON-NLS-1$
      STR_CANCEL_IF_ANY_ITEM_EXISTS, STR_CANCEL_IF_ANY_ITEM_EXISTS_D ),

  ;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ECollImportStratey"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EResolveStrategy> KEEPER = new StridableEnumKeeper<>( EResolveStrategy.class );

  private static IStridablesListEdit<EResolveStrategy> list = null;

  private final String id;
  private final String name;
  private final String description;

  EResolveStrategy( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link EResolveStrategy} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EResolveStrategy> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EResolveStrategy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EResolveStrategy getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EResolveStrategy} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EResolveStrategy findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EResolveStrategy item : values() ) {
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
   * @return {@link EResolveStrategy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EResolveStrategy getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
