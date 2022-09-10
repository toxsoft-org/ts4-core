package org.toxsoft.core.tslib.av;

import static org.toxsoft.core.tslib.av.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Atomic data type.
 * <p>
 * Atomic data type is modelling real (red) world data, eg. real data exisеing outside program code. The introduction of
 * this enumeration with interface {@link IAtomicValue} serves as "bridge" between the real world and the computer
 * (Java-software) model.
 * <p>
 * Atomic types are modelling following real world concepts:
 * <ul>
 * <li>{@link #BOOLEAN} - logical assessment (yes/no. true/false, etc.);</li>
 * <li>{@link #INTEGER} - the counted number of something;</li>
 * <li>{@link #FLOATING} - the measured values of physical quantities;</li>
 * <li>{@link #STRING} - real-world identifying information (eg., name);</li>
 * <li>{@link #TIMESTAMP} - the time in real world;</li>
 * <li>{@link #VALOBJ} - embedded value-object, some entities like time interval.</li>
 * </ul>
 * <p>
 * This class implements {@link IStridable}, with following meaning of it's fields:
 * <ul>
 * <li>{@link #id()} - uinque type identifier (IDname);</li>
 * <li>{@link #nmName()} - short name of the type;</li>
 * <li>{@link #description()} - brief description of the type.</li>
 * </ul>
 * <p>
 * It is important how the {@link #NONE} type is used:<br>
 * <ul>
 * <li>for <b>valiables (Lvalues)</b> {@link #NONE} - means that <b>any</b> type may be assigned to the variable;</li>
 * <li>for <b>values (Rvalues)</b> {@link #NONE} means that there is no value, so it is {@link IAtomicValue#NULL}
 * constant</b>.</li>
 * </ul>
 *
 * @author hazard157
 */
public enum EAtomicType
    implements IStridable {

  /**
   * Unknown, none type of the vale or any type for the variable.
   */
  NONE( DDID_NONE, STR_N_AT_NONE, STR_D_AT_NONE, IAtomicValue.NULL ),

  /**
   * Boolean type.
   */
  BOOLEAN( DDID_BOOLEAN, STR_N_AT_BOOL, STR_D_AT_BOOL, AV_FALSE ),

  /**
   * Integer type with no value limits.
   */
  INTEGER( DDID_INTEGER, STR_N_AT_INTEGER, STR_D_AT_INTEGER, AV_0 ),

  /**
   * Real type with no value limits.
   */
  FLOATING( DDID_FLOATING, STR_N_AT_FLOATING, STR_D_AT_FLOATING, AV_F_0 ),

  /**
   * Text string with no length limit.
   */
  STRING( DDID_STRING, STR_N_AT_STRING, STR_D_AT_STRING, AV_STR_EMPTY ),

  /**
   * Timestamp.
   */
  TIMESTAMP( DDID_TIMESTAMP, STR_N_AT_TIMESTAMP, STR_D_AT_TIMESTAMP, AV_TIME_0 ),

  /**
   * Embedded value object.
   */
  VALOBJ( DDID_VALOBJ, STR_N_AT_VALOBJ, STR_D_AT_VALOBJ, AV_VALOBJ_NULL );

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "EAtomicType"; //$NON-NLS-1$

  /**
   * Keeper singleton instance
   */
  public static final StridableEnumKeeper<EAtomicType> KEEPER = new StridableEnumKeeper<>( EAtomicType.class );

  private static IStridablesListEdit<EAtomicType> list = null;

  private final String       id;
  private final String       nmName;
  private final String       description;
  private final IAtomicValue defaultValue;

  EAtomicType( String aId, String aName, String aDescr, IAtomicValue aDefaultValue ) {
    id = aId;
    nmName = aName;
    description = aDescr;
    defaultValue = aDefaultValue;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return nmName;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if this is {@link #NONE} constant.
   * <p>
   * This method is intended to make code more self-documentory when chacking variables.
   *
   * @return boolean - эта константа есть {@link #NONE}
   */
  public boolean isAny() {
    return this == NONE;
  }

  /**
   * Returns default value of this atomic type.
   *
   * @return {@link IAtomicValue} - default valo of the type
   */
  public IAtomicValue defaultValue() {
    return defaultValue;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return nmName;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  /**
   * Returns all constants as ordered {@link IStridablesList}.
   *
   * @return {@link IStridablesList}&lt; {@link EAtomicType} &gt; - list of all constants in order of declaration
   */
  public static IStridablesList<EAtomicType> asStridablesList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  // ----------------------------------------------------------------------------------
  // Check for constants existance
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
   * @return EAtomicType - found constant or <code>null</code> if no constant exists with specified identifier
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static EAtomicType findById( String aId ) {
    return asStridablesList().findByKey( aId );
  }

  /**
   * Returns constant with specified identifier ({@link #id()}).
   *
   * @param aId String - identifier of constant to search for
   * @return EAtomicType - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static EAtomicType getById( String aId ) {
    return asStridablesList().getByKey( aId );
  }

  /**
   * Finds constant with specified name ({@link #nmName()}) exists.
   *
   * @param aName String - identifier of constant to search for
   * @return EAtomicType - found constant or <code>null</code> if no constant exists with specified name
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static EAtomicType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EAtomicType item : asStridablesList() ) {
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
   * @return EAtomicType - found constant
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such constant
   */
  public static EAtomicType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
