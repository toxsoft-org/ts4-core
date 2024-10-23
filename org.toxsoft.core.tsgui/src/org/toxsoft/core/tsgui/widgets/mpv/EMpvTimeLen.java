package org.toxsoft.core.tsgui.widgets.mpv;

import static org.toxsoft.core.tsgui.widgets.mpv.ITsResources.*;

import java.time.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Time field negth - the accuracy of time editing using sume {@link IMultiPartValue} subclasses.
 *
 * @author hazard157
 */
public enum EMpvTimeLen
    implements IStridable {

  /**
   * HH:MM:SS.mmm.
   */
  HH_MM_SS_MMM( "hh_mm_ss_mmm", STR_HH_MM_SS_MMM, STR_HH_MM_SS_MMM_D ), //$NON-NLS-1$

  /**
   * HH:MM:SS.
   */
  HH_MM_SS( "hh_mm_ss", STR_HH_MM_SS, STR_HH_MM_SS_D ), //$NON-NLS-1$

  /**
   * HH:MM.
   */
  HH_MM( "hh_mm", STR_HH_MM, STR_HH_MM_D ); //$NON-NLS-1$

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
   * Returns the formatted string representation of argument respecting this constant.
   * <p>
   * Returns string like "HH:MM[:SS[.mmm]]"
   *
   * @param aTime {@link LocalTime} - the time
   * @return String - formatted string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( "boxing" )
  public String toString( LocalTime aTime ) {
    TsNullArgumentRtException.checkNull( aTime );
    switch( this ) {
      case HH_MM: {
        return String.format( "%02d:%02d", aTime.getHour(), aTime.getMinute() ); //$NON-NLS-1$
      }
      case HH_MM_SS: {
        return String.format( "%02d:%02d:%02d", aTime.getHour(), aTime.getMinute(), aTime.getSecond() ); //$NON-NLS-1$
      }
      case HH_MM_SS_MMM: {
        int msec = aTime.getNano() / 1_000_000;
        return String.format( "%02d:%02d:%02d.%03d", aTime.getHour(), aTime.getMinute(), aTime.getSecond(), msec ); //$NON-NLS-1$
      }
      default:
        throw new TsNotAllEnumsUsedRtException( this.id );
    }
  }

  /**
   * Returns the formatted string representation of argument respecting this constant.
   * <p>
   * Returns string like "YYYY-DD-MM HH:MM[:SS[.mmm]]"
   *
   * @param aTime {@link LocalDateTime} - the time
   * @return String - formatted string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public String toString( LocalDateTime aTime ) {
    TsNullArgumentRtException.checkNull( aTime );
    return aTime.toLocalDate().toString() + ' ' + toString( aTime.toLocalTime() );
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
