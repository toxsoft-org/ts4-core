package org.toxsoft.core.tslib.bricks.time.misc;

import java.time.*;
import java.time.format.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The day of week, is the {@link DayOfWeek} wrapped in tslib style <code>enum</code>.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EWeekDay
    implements IStridable {

  MONDAY( "mon", Calendar.MONDAY, DayOfWeek.MONDAY ), //$NON-NLS-1$

  TUESDAY( "tue", Calendar.TUESDAY, DayOfWeek.TUESDAY ), //$NON-NLS-1$

  WEDNESDAY( "wed", Calendar.WEDNESDAY, DayOfWeek.WEDNESDAY ), //$NON-NLS-1$

  THURSDAY( "thu", Calendar.THURSDAY, DayOfWeek.THURSDAY ), //$NON-NLS-1$

  FRIDAY( "fri", Calendar.FRIDAY, DayOfWeek.FRIDAY ), //$NON-NLS-1$

  SATURDAY( "sat", Calendar.SATURDAY, DayOfWeek.SATURDAY ), //$NON-NLS-1$

  SUNDAY( "sun", Calendar.SUNDAY, DayOfWeek.SUNDAY ), //$NON-NLS-1$

  ;

  /**
   * THe registered keeper ID
   */
  public static final String KEEPER_ID = "WeekDay"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EWeekDay> KEEPER = new StridableEnumKeeper<>( EWeekDay.class );

  private static IStridablesListEdit<EWeekDay> list = null;

  private final String    id;
  private final String    name;
  private final String    description;
  private final int       javaDayOfWeek;
  private final DayOfWeek dayOfWeek;

  EWeekDay( String aId, int aCalendarDayOfWeek, DayOfWeek aDayOfWeek ) {
    id = aId;
    name = aDayOfWeek.getDisplayName( TextStyle.SHORT, Locale.getDefault() );
    description = aDayOfWeek.getDisplayName( TextStyle.FULL_STANDALONE, Locale.getDefault() );
    javaDayOfWeek = aCalendarDayOfWeek;
    dayOfWeek = aDayOfWeek;
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
   * return corresponding {@link DayOfWeek}.
   *
   * @return {@link DayOfWeek} - corresponding {@link DayOfWeek}
   */
  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  /**
   * Gets constant by {@link DayOfWeek}.
   *
   * @param aDayOfWeek {@link DayOfWeek} - the Java day of week
   * @return {@link EWeekDay} - found constant, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EWeekDay getByDayOfWeek( DayOfWeek aDayOfWeek ) {
    TsNullArgumentRtException.checkNull( aDayOfWeek );
    for( EWeekDay d : asList() ) {
      if( d.dayOfWeek == aDayOfWeek ) {
        return d;
      }
    }
    // we never be here, however compiler don't know about this
    throw new TsInternalErrorRtException();
  }

  /**
   * Returns the corresponding <i>int</i> constant from {@link Calendar}.
   * <p>
   * Returns on of the values {@link Calendar#MONDAY}, {@link Calendar#THURSDAY}, {@link Calendar#WEDNESDAY},
   * {@link Calendar#THURSDAY}, {@link Calendar#FRIDAY}, {@link Calendar#SATURDAY}, {@link Calendar#SUNDAY}.
   *
   * @return int - one of the <code>int</code> constants {@link Calendar}<code>.XXX</code>
   */
  public int getCalendarDayOfWeek() {
    return javaDayOfWeek;
  }

  /**
   * Finds the <code>enum</code> constant from {@link Calendar}<code>.XXX</code>.
   * <p>
   * Argument should be one of the {@link Calendar#MONDAY}, {@link Calendar#THURSDAY}, {@link Calendar#WEDNESDAY},
   * {@link Calendar#THURSDAY}, {@link Calendar#FRIDAY}, {@link Calendar#SATURDAY}, {@link Calendar#SUNDAY}, otherwise
   * method returns <code>null</code>.
   *
   * @param aCalendarDayOfWeek - one of the <code>int</code> constants {@link Calendar}<code>.XXX</code>
   * @return {@link EWeekDay} - <code>enum</code> constant or <code>null</code>
   */
  public static EWeekDay findByCalendarDayOfWeek( int aCalendarDayOfWeek ) {
    for( EWeekDay d : asList() ) {
      if( d.getCalendarDayOfWeek() == aCalendarDayOfWeek ) {
        return d;
      }
    }
    return null;
  }

  /**
   * Gets the <code>enum</code> constant from {@link Calendar}<code>.XXX</code>.
   * <p>
   * Argument should be one of the {@link Calendar#MONDAY}, {@link Calendar#THURSDAY}, {@link Calendar#WEDNESDAY},
   * {@link Calendar#THURSDAY}, {@link Calendar#FRIDAY}, {@link Calendar#SATURDAY}, {@link Calendar#SUNDAY}, otherwise
   * method throws an exception
   *
   * @param aCalendarDayOfWeek - one of the <code>int</code> constants {@link Calendar}<code>.XXX</code>
   * @return {@link EWeekDay} - <code>enum</code> constant
   * @throws TsIllegalArgumentRtException argument has invalid value
   */
  public static EWeekDay getByCalendarDayOfWeek( int aCalendarDayOfWeek ) {
    EWeekDay d = findByCalendarDayOfWeek( aCalendarDayOfWeek );
    TsIllegalArgumentRtException.checkTrue( d == null );
    return d;
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EWeekDay} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EWeekDay> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EWeekDay} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EWeekDay getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EWeekDay} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EWeekDay findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EWeekDay item : values() ) {
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
   * @return {@link EWeekDay} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EWeekDay getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
