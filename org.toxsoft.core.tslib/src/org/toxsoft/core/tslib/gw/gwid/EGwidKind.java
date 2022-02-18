package org.toxsoft.core.tslib.gw.gwid;

import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.core.tslib.gw.gwid.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link Gwid} content kind.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EGwidKind
    implements IStridable {

  GW_CLASS( GW_KEYWORD_CLASS, STR_N_GK_CLASS, STR_D_GK_CLASS, false ),

  GW_ATTR( GW_KEYWORD_ATTR, STR_N_GK_ATTR, STR_D_GK_ATTR, true ),

  GW_RIVET( GW_KEYWORD_RIVET, STR_N_GK_RIVET, STR_D_GK_RIVET, true ),

  GW_CLOB( GW_KEYWORD_CLOB, STR_N_GK_CLOB, STR_D_GK_CLOB, true ),

  GW_LINK( GW_KEYWORD_LINK, STR_N_GK_LINK, STR_D_GK_LINK, true ),

  GW_EVENT( GW_KEYWORD_EVENT, STR_N_GK_EVENT, STR_D_GK_EVENT, true ),

  GW_RTDATA( GW_KEYWORD_RTDATA, STR_N_GK_RTDATA, STR_D_GK_RTDATA, true ),

  GW_CMD( GW_KEYWORD_CMD, STR_N_GK_CMD, STR_D_GK_CMD, true ),

  ;

  private static IStridablesList<EGwidKind> list = null;

  private final String  id;
  private final String  nmName;
  private final String  description;
  private final boolean hasProp;

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDpath)
   * @param aName - short name
   * @param aDescription String - description
   * @param aHasProp boolean - indicates that {@link IGwid#isProp()} is <code>true</code>
   */
  EGwidKind( String aId, String aName, String aDescription, boolean aHasProp ) {
    id = aId;
    nmName = aName;
    description = aDescription;
    hasProp = aHasProp;
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

  // ----------------------------------------------------------------------------------
  // Additional API
  //

  /**
   * Determines if this kind has property defined.
   *
   * @return boolean - indicates that {@link IGwid#isProp()} is <code>true</code>
   */
  public boolean hasProp() {
    return hasProp;
  }

  /**
   * Returns all constants as list.
   *
   * @return {@link IStridablesList}&lt;{@link EGwidKind}&gt; - list of all constants
   */
  public static IStridablesList<EGwidKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  // ----------------------------------------------------------------------------------
  // Find and get
  //

  /**
   * Finds the constant by the identifier.
   *
   * @param aId String - identifier of the constant
   * @return {@link EGwidKind} - found constant or <code>null</code> there is no constant with specified identifier
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EGwidKind findById( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EGwidKind item : asList() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the identifier.
   *
   * @param aId String - identifier of the constant
   * @return {@link EGwidKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException there is no constant with specified identifier
   */
  public static EGwidKind getById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findById( aId ) );
  }

}
