package org.toxsoft.core.tslib.gw.gwid;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.core.tslib.gw.gwid.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link Gwid} content kind.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EGwidKind
    implements IStridable {

  GW_CLASS( GW_KEYWORD_CLASS, STR_GK_CLASS, STR_GK_CLASS_D, false, false ),

  GW_ATTR( GW_KEYWORD_ATTR, STR_GK_ATTR, STR_GK_ATTR_D, true, false ),

  GW_RIVET( GW_KEYWORD_RIVET, STR_GK_RIVET, STR_GK_RIVET_D, true, false ),

  GW_CLOB( GW_KEYWORD_CLOB, STR_GK_CLOB, STR_GK_CLOB_D, true, false ),

  GW_LINK( GW_KEYWORD_LINK, STR_GK_LINK, STR_GK_LINK_D, true, false ),

  GW_EVENT( GW_KEYWORD_EVENT, STR_GK_EVENT, STR_GK_EVENT_D, true, false ),

  GW_EVENT_PARAM( GW_KEYWORD_EVENT_PARAM, STR_GK_EVENT_PARAM, STR_GK_EVENT_PARAM_D, true, true ),

  GW_RTDATA( GW_KEYWORD_RTDATA, STR_GK_RTDATA, STR_GK_RTDATA_D, true, false ),

  GW_CMD( GW_KEYWORD_CMD, STR_GK_CMD, STR_GK_CMD_D, true, false ),

  GW_CMD_ARG( GW_KEYWORD_CMD_ARG, STR_GK_CMD_ARG, STR_GK_CMD_ARG_D, true, true ),

  ;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "GwidKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EGwidKind> KEEPER = new StridableEnumKeeper<>( EGwidKind.class );

  private static IStridablesList<EGwidKind> list = null;

  private final String  id;
  private final String  nmName;
  private final String  description;
  private final boolean hasProp;
  private final boolean hasSubProp;

  private IAtomicValue av = null;

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDpath)
   * @param aName - short name
   * @param aDescription String - description
   * @param aHasProp boolean - indicates that {@link IGwid#isProp()} is <code>true</code>
   * @param aHasSubProp boolean - indicates that {@link IGwid#isSubProp()} is <code>true</code>
   */
  EGwidKind( String aId, String aName, String aDescription, boolean aHasProp, boolean aHasSubProp ) {
    id = aId;
    nmName = aName;
    description = aDescription;
    hasProp = aHasProp;
    hasSubProp = aHasSubProp;
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
   * Determines if this kind has sub-property defined.
   *
   * @return boolean - indicates that {@link IGwid#isSubProp()} is <code>true</code>
   */
  public boolean hasSubProp() {
    return hasSubProp;
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

  /**
   * Returns the {@link EAtomicType#VALOBJ} atomic value of this constant.
   *
   * @return {@link IAtomicValue} - atomic value of this constant
   */
  public IAtomicValue atomicValue() {
    if( av == null ) {
      av = avValobj( this );
    }
    return av;
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
