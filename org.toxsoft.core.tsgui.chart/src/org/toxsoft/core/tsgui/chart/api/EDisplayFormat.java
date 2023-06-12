package org.toxsoft.core.tsgui.chart.api;

import static org.toxsoft.core.tsgui.chart.api.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of display formats.
 *
 * @author dima
 */
public enum EDisplayFormat
    implements IStridable {

  /**
   * As integer
   */
  AS_INTEGER( "AS_INTEGER", STR_EDF_AS_INTEGER, STR_EDF_AS_INTEGER_D, "%.0f" ), //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * As float with 1 digit after point.
   */
  ONE_DIGIT( "ONE_DIGIT", STR_EDF_ONE_DIGIT, STR_EDF_ONE_DIGIT_D, "%.1f" ), //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * As float with 2 digit after point.
   */
  TWO_DIGIT( "TWO_DIGIT", STR_EDF_TWO_DIGIT, STR_EDF_TWO_DIGIT_D, "%.2f" ), //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * As float with 3 digit after point.
   */
  THREE_DIGIT( "THREE_DIGIT", STR_EDF_THREE_DIGIT, STR_EDF_THREE_DIGIT_D, "%.3f" ), //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * As float.
   */
  AS_FLOAT( "AS_FLOAT", STR_EDF_AS_FLOAT, STR_EDF_AS_FLOAT_D, "%f" ); //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EDisplayFormat"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EDisplayFormat> KEEPER = new StridableEnumKeeper<>( EDisplayFormat.class );

  private static IStridablesListEdit<EDisplayFormat> list = null;

  private final String id;
  private final String name;
  private final String description;

  private final String format;

  EDisplayFormat( String aId, String aName, String aDescription, String aFormat ) {
    id = aId;
    name = aName;
    description = aDescription;

    format = aFormat;
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

  /**
   * Возвращает формат в строковой форме.
   *
   * @return String - формат в строковой форме.
   */
  public String format() {
    return format;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EDisplayFormat} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EDisplayFormat> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EDisplayFormat} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EDisplayFormat getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EDisplayFormat} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EDisplayFormat findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EDisplayFormat item : values() ) {
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
   * @return {@link EDisplayFormat} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EDisplayFormat getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
