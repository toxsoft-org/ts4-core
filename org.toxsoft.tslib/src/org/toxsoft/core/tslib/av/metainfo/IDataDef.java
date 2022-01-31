package org.toxsoft.core.tslib.av.metainfo;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.Comparator;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.errors.AvTypeCastRtException;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.utils.IStdParameterized;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.IStridableParameterized;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Atomic data type information.
 * <p>
 * This is identifyable (via {@link IStridable}) data type {@link IDataType}.
 * <p>
 * Data type information is the simple set of the constraint values held in {@link #params()}.
 *
 * @author hazard157
 */
public interface IDataDef
    extends IDataType, IStridableParameterized, IStdParameterized {

  /**
   * Returns the atomic type of the data.
   *
   * @return {@link EAtomicType} - atomic type or the {@link EAtomicType#NONE} for any type
   */
  @Override
  EAtomicType atomicType();

  @Override
  default String nmName() {
    return DDEF_NAME.getValue( params() ).asString();
  }

  @Override
  default String description() {
    return DDEF_DESCRIPTION.getValue( params() ).asString();
  }

  /**
   * Returns the default value of the data or {@link IAtomicValue#NULL}.
   * <p>
   * Returns the value of the optional parameter with identifier {@link IAvMetaConstants#TSID_DEFAULT_VALUE}.
   *
   * @return {@link IAtomicValue} - default value of the data, or {@link IAtomicValue#NULL} if no parameter specified
   */
  default IAtomicValue defaultValue() {
    return params().getValue( TSID_DEFAULT_VALUE, IAtomicValue.NULL );
  }

  /**
   * Returns the flag that absence of the value is allowed.
   * <p>
   * Returns the value of the optional parameter with identifier {@link IAvMetaConstants#TSID_IS_NULL_ALLOWED}.
   *
   * @return boolean - allowed value absence flag or <code>true</code> if no parameter specified
   */
  default boolean isNullAllowed() {
    return params().getBool( TSID_IS_NULL_ALLOWED, true );
  }

  /**
   * Returns the flag that option with identifier {@link #id()} must present in some collection.
   * <p>
   * Common usage of the flag is to reject {@link IOptionSet} if it does not contain mandatory option.
   * <p>
   * Returns the value of the optional parameter with identifier {@link IAvMetaConstants#TSID_IS_MANDATORY}.
   *
   * @return boolean - <code>true</code> if option must be present in some collection
   */
  default boolean isMandatory() {
    return params().getBool( TSID_IS_MANDATORY, false );
  }

  /**
   * Returns the keeper identifier (meaningful only for {@link EAtomicType#VALOBJ} atomic types).
   * <p>
   * Returns the value of the optional parameter with identifier {@link IAvMetaConstants#TSID_KEEPER_ID}.
   *
   * @return String - keeper identifier or <code>null</code> if no parameter specified
   */
  default String keeperId() {
    return params().getStr( TSID_KEEPER_ID, null );
  }

  /**
   * Returns the format string to be used with {@link AvUtils#printAv(String, IAtomicValue)}.
   * <p>
   * Returns the value of the optional parameter with identifier {@link IAvMetaConstants#TSID_FORMAT_STRING}.
   *
   * @return String - format string or <code>null</code> if no parameter specified
   */
  default String formatString() {
    return params().getStr( TSID_FORMAT_STRING, null );
  }

  /**
   * Determines if data is supposed to have resticted set of allowed values.
   * <p>
   * Enumerated values have {@link IAvMetaConstants#TSID_ENUMERATION} parameter defined. This method checks and returns
   * <code>true</code> when {@link IAvMetaConstants#TSID_ENUMERATION} parameter is present in {@link #params()}.
   *
   * @return boolean - enumeration flag
   */
  default boolean isEnumeration() {
    return params().hasValue( TSID_ENUMERATION );
  }

  /**
   * Returns the allowed values of the data.
   * <p>
   * Returns the {@link EAtomicType#VALOBJ} value of the optional parameter with identifier
   * {@link IAvMetaConstants#TSID_ENUMERATION}.
   * <p>
   * For non-enumeration types returns an empty list.
   *
   * @return {@link IList}&lt;{@link IAtomicValue}&gt; - the list of the enum values or an empty list
   */
  default IList<IAtomicValue> enumerateValues() {
    if( !isEnumeration() ) {
      return IList.EMPTY;
    }
    return params().getValobj( TSID_ENUMERATION );
  }

  /**
   * Returns the value validator.
   * <p>
   * If parameter {@link IAvMetaConstants#TSID_VALIDATOR_CLASS} is specified, method once creates an instant of the
   * specified class as the validator. The parameter value is interpreted as class name. Class must have at least one
   * public constructor either with single argument of type {@link IDataDef} (searched first) or with no arguments.
   *
   * @return {@link ITsValidator}&lt;{@link IAtomicValue}&gt; - validator or {@link ITsValidator#PASS}
   */
  ITsValidator<IAtomicValue> validator();

  /**
   * Returns the value comparator.
   * <p>
   * BY default atomic values are compared with {@link AvUtils#DEFAULT_AV_COMPARATOR}. However for some cases, mostly
   * for {@link EAtomicType#VALOBJ} types it is meaningfull to compare contained values.
   *
   * @return {@link Comparator}&lr;{@link IAtomicValue}&gt; - the comparator never is <code>null</code>
   */
  Comparator<IAtomicValue> comparator();

  /**
   * Returns the value of the option with identifier {@link #id()}.
   * <p>
   * If set does contains the value with identifier {@link #id()}, the {@link #defaultValue()} is returned.
   *
   * @param aOps {@link IOptionSet} - option set
   * @return {@link IAtomicValue} - the value of the option
   */
  IAtomicValue getValue( IOptionSet aOps );

  /**
   * Sets the option value to the set with identifier {@link #id()}.
   * <p>
   * The <code>null</code> is stored as {@link IAtomicValue#NULL}
   *
   * @param aOps {@link IOptionSetEdit} - option set
   * @param aValue {@link IAtomicValue} - the value to be set, may be <code>null</code>
   * @throws TsNullArgumentRtException aOps == <code>null</code>
   * @throws TsItemNotFoundRtException there is no entry in option set for mandatory option
   * @throws AvTypeCastRtException the type of the value is not compatible with the {@link #atomicType()}
   */
  void setValue( IOptionSetEdit aOps, IAtomicValue aValue );

}
