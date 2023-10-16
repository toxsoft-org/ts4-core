package org.toxsoft.core.tslib.av;

import static org.toxsoft.core.tslib.av.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Atomic data type.
 * <p>
 * Atomic data type is modeling real (red) world data, eg. real data exisеing outside program code. The introduction of
 * this enumeration with interface {@link IAtomicValue} serves as "bridge" between the real world and the computer
 * (Java-software) model.
 * <p>
 * Atomic types are modeling following real world concepts:
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
 * <li>{@link #id()} - unique type identifier (IDname);</li>
 * <li>{@link #nmName()} - short name of the type;</li>
 * <li>{@link #description()} - brief description of the type.</li>
 * </ul>
 * <p>
 * It is important how the {@link #NONE} type is used:<br>
 * <ul>
 * <li>for <b>variables (L-values)</b> {@link #NONE} - means that <b>any</b> type may be assigned to the variable;</li>
 * <li>for <b>values (R-values)</b> {@link #NONE} means that there is no value, so it is {@link IAtomicValue#NULL}
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
  NONE( DDID_NONE, STR_AT_NONE, STR_AT_NONE_D, IAtomicValue.NULL ),

  /**
   * Boolean type.
   */
  BOOLEAN( DDID_BOOLEAN, STR_AT_BOOL, STR_AT_BOOL_D, AV_FALSE ),

  /**
   * Integer type with no value limits.
   */
  INTEGER( DDID_INTEGER, STR_AT_INTEGER, STR_AT_INTEGER_D, AV_0 ),

  /**
   * Real type with no value limits.
   */
  FLOATING( DDID_FLOATING, STR_AT_FLOATING, STR_AT_FLOATING_D, AV_F_0 ),

  /**
   * Text string with no length limit.
   */
  STRING( DDID_STRING, STR_AT_STRING, STR_AT_STRING_D, AV_STR_EMPTY ),

  /**
   * Timestamp.
   */
  TIMESTAMP( DDID_TIMESTAMP, STR_AT_TIMESTAMP, STR_AT_TIMESTAMP_D, AV_TIME_0 ),

  /**
   * Embedded value object.
   */
  VALOBJ( DDID_VALOBJ, STR_AT_VALOBJ, STR_AT_VALOBJ_D, AV_VALOBJ_NULL );

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
   * @return boolean - this constant is {@link #NONE}
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

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EAtomicType} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EAtomicType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EAtomicType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EAtomicType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EAtomicType} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EAtomicType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EAtomicType item : values() ) {
      if( item.nmName.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EAtomicType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EAtomicType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
