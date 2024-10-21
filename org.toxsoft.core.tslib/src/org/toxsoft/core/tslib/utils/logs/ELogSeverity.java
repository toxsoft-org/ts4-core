package org.toxsoft.core.tslib.utils.logs;

import static org.toxsoft.core.tslib.utils.logs.ITsResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Predefined severity levels of the {@link ILogger} log messages.
 * <p>
 * Constants in this <code>enum</code> are ordered by the severiry increase.
 *
 * @author hazard157
 */
public enum ELogSeverity
    implements IStridable {

  /**
   * Debug - Message for developers must not be present in final code.
   */
  DEBUG( "DEBUG", STR_LOG_SEVERITY_DEBUG, STR_LOG_SEVERITY_DEBUG_D ), //$NON-NLS-1$

  /**
   * Info - information about program execution.
   */
  INFO( "INFO", STR_LOG_SEVERITY_INFO, STR_LOG_SEVERITY_INFO_D ), //$NON-NLS-1$

  /**
   * Warning - A recoverable problem occured, note that it may lead to errors.
   */
  WARNING( "WARNING", STR_LOG_SEVERITY_WARNING, STR_LOG_SEVERITY_WARNING_D ), //$NON-NLS-1$

  /**
   * Error - Error occured, program may not work partially or crash completely.
   */
  ERROR( "ERROR", STR_LOG_SEVERITY_ERROR, STR_LOG_SEVERITY_ERROR_D ); //$NON-NLS-1$

  /**
   * Registered keepr ID.
   */
  public static final String KEEPER_ID = "ELogSeverity"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ELogSeverity> KEEPER = new StridableEnumKeeper<>( ELogSeverity.class );

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
