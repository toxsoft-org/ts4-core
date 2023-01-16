package org.toxsoft.core.tsgui.ved.api.entity;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.api.entity.ITsResources.*;

import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.icons.*;

/**
 * Kind of VED-entities.
 *
 * @author hazard157
 */
public enum EVedEntityKind
    implements IStridable, IIconIdable {

  /**
   * {@link IVedComponent}.
   */
  COMPONENT( "component", STR_N_COMPONENT, STR_D_COMPONENT, //$NON-NLS-1$
      ICON_VED_COMPONENT, IVedComponent.class ),

  /**
   * {@link IVedActor}.
   */
  ACTOR( "actor", STR_N_ACTOR, STR_D_ACTOR, //$NON-NLS-1$
      ICON_VED_ACTOR, IVedActor.class ),

  /**
   * {@link IVedActor}.
   */
  TAILOR( "tailor", STR_N_TAILOR, STR_D_TAILOR, //$NON-NLS-1$
      ICON_VED_TAILOR, IVedTailor.class );

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EVedEntityKind"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EVedEntityKind> KEEPER = new StridableEnumKeeper<>( EVedEntityKind.class );

  private static IStridablesListEdit<EVedEntityKind> list = null;

  private final String                      id;
  private final String                      name;
  private final String                      description;
  private final Class<? extends IVedEntity> entityClass;
  private final String                      iconId;

  EVedEntityKind( String aId, String aName, String aDescription, String aIconId, Class<? extends IVedEntity> aClass ) {
    id = aId;
    name = aName;
    description = aDescription;
    iconId = aIconId;
    entityClass = aClass;
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
   * Returns the Java-class of entities base interface.
   *
   * @return {@link Class}&lt;? extends {@link IVedEntity}&gt; - entities Java-class
   */
  public Class<? extends IVedEntity> entityClass() {
    return entityClass;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EVedEntityKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EVedEntityKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EVedEntityKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EVedEntityKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVedEntityKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EVedEntityKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVedEntityKind item : values() ) {
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
   * @return {@link EVedEntityKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EVedEntityKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
