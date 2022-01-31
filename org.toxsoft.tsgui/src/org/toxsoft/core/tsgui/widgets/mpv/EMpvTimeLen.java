package org.toxsoft.core.tsgui.widgets.mpv;

import static org.toxsoft.core.tsgui.widgets.mpv.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Time field negth - the accuracy of time editing using sume {@link IMultiPartValue} subclasses.
 *
 * @author goga
 */
public enum EMpvTimeLen
    implements IStridable {

  /**
   * HH:MM:SS.mmm.
   */
  HH_MM_SS_MMM( "hh_mm_ss_mmm", STR_N_HH_MM_SS_MMM, STR_D_HH_MM_SS_MMM ), //$NON-NLS-1$

  /**
   * HH:MM:SS.
   */
  HH_MM_SS( "hh_mm_ss", STR_N_HH_MM_SS, STR_D_HH_MM_SS ), //$NON-NLS-1$

  /**
   * HH:MM.
   */
  HH_MM( "hh_mm", STR_N_HH_MM, STR_D_HH_MM ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EMpvTimeLen"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EMpvTimeLen> KEEPER = new StridableEnumKeeper<>( EMpvTimeLen.class );

  private static IStridablesListEdit<EMpvTimeLen> list = null;

  private final String id;
  private final String name;
  private final String description;

  /**
   * Constructor.
   *
   * @param aId String - ID of constant (an IDpath)
   * @param aName String - short name
   * @param aDescription String - long description
   */
  EMpvTimeLen( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link EMpvTimeLen} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EMpvTimeLen> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EMpvTimeLen} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EMpvTimeLen getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EMpvTimeLen} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EMpvTimeLen findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EMpvTimeLen item : values() ) {
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
   * @return {@link EMpvTimeLen} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EMpvTimeLen getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
