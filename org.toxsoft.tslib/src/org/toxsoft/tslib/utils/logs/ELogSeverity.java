package org.toxsoft.tslib.utils.logs;

import static org.toxsoft.tslib.utils.logs.ITsResources.*;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Predefined severity levels of the {@link ILogger} log messages.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public enum ELogSeverity
    implements IStridable {

  /**
   * Info - A recoverable problem occured, note that it may lead to errors.
   */
  INFO( "INFO", STR_N_LOG_SEVERITY_INFO, STR_D_LOG_SEVERITY_INFO ),

  /**
   * Warning - A recoverable problem occured, note that it may lead to errors.
   */
  WARNING( "WARNING", STR_N_LOG_SEVERITY_WARNING, STR_D_LOG_SEVERITY_WARNING ),

  /**
   * Error - Error occured, program may not work partially or crash completely.
   */
  ERROR( "ERROR", STR_N_LOG_SEVERITY_ERROR, STR_D_LOG_SEVERITY_ERROR ),

  /**
   * Debug - Message for developers must not be present in fincal code.
   */
  DEBUG( "DEBUG", STR_N_LOG_SEVERITY_DEBUG, STR_D_LOG_SEVERITY_DEBUG ),

  ;

  private static IStridablesList<ELogSeverity> list = null;

  private final String id;
  private final String nmName;
  private final String description;

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDPath)
   * @param aName String - short, human-readable name
   * @param aDescription String - description
   */
  ELogSeverity( String aId, String aName, String aDescription ) {
    id = aId;
    nmName = aName;
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
    return nmName;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  /**
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link ELogSeverity} &gt; - list of all constants in order of declaration
   */
  public static IStridablesList<ELogSeverity> list() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  // ------------------------------------------------------------------------------------
  // Search and check methods
  //

  /**
   * Determines if constant with specified identifier ({@link #id()}) exists.
   *
   * @param aId String - identifier of constant to search for
   * @return boolean - <code>true</code> if constant exists
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Determines if constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - name of constant to search for
   * @return boolean - <code>true</code> if constant exists
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Find & get
  //

  /**
   * Finds constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return ELogSeverity - found constant or <code>null</code> if no constant exists with specified identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ELogSeverity findById( String aId ) {
    return list().findByKey( aId );
  }

  /**
   * Returns constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return ELogSeverity - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ELogSeverity getById( String aId ) {
    return list().getByKey( aId );
  }

  /**
   * Finds constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - identifier of constant to search for
   * @return ELogSeverity - found constant or <code>null</code> if no constant exists with specified name
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ELogSeverity findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ELogSeverity item : list() ) {
      if( item.nmName.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns constant with specified name ({@link #nmName()}).
   *
   * @param aName String - identifier of constant to search for
   * @return ELogSeverity - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static ELogSeverity getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
