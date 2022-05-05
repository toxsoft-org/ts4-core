package org.toxsoft.core.tslib.av.metainfo;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Atimic data type - defines atomic type and type parameters.
 *
 * @author hazard157
 */
public interface IDataType
    extends IParameterized {

  /**
   * Returns the atomic type of the data.
   *
   * @return {@link EAtomicType} - atomic type
   */
  EAtomicType atomicType();

  /**
   * Returns type parameters.
   *
   * @return ITypeConstraints - atomic data type parameters
   */
  @Override
  IOptionSet params();

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

}
