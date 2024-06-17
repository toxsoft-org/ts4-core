package org.toxsoft.core.tsgui.ved.screen.items;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * The kind of the {@link IVedItem}.
 * <p>
 * Depending on kind the reference to the {@link IVedItem} may be safely can to interfaces such as {@link IVedActor} or
 * {@link IVedVisel}.
 *
 * @author hazard157
 */
public enum EVedItemKind
    implements IStridable, IIconIdable {

  /**
   * The VISEL, implements {@link IVedVisel}.
   */
  VISEL( "visel", STR_ITEMKIND_VISEL, STR_ITEMKIND_VISEL_D, ICONID_VED_VISEL ), //$NON-NLS-1$

  /**
   * The actor, implements {@link IVedActor}.
   */
  ACTOR( "actor", STR_ITEMKIND_ACTOR, STR_ITEMKIND_ACTOR_D, ICONID_VED_ACTOR ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EVedItemKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EVedItemKind> KEEPER = new StridableEnumKeeper<>( EVedItemKind.class );

  private static IStridablesListEdit<EVedItemKind> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final String iconId;

  EVedItemKind( String aId, String aName, String aDescription, String aIconId ) {
    id = aId;
    name = aName;
    description = aDescription;
    iconId = aIconId;
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

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EVedItemKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EVedItemKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EVedItemKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EVedItemKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVedItemKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EVedItemKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVedItemKind item : values() ) {
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
   * @return {@link EVedItemKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EVedItemKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
