package org.toxsoft.core.tslib.av.metainfo;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Atomic data type information.
 * <p>
 * This is identifiable (via {@link IStridable}) data type {@link IDataType}.
 * <p>
 * Data type information is the simple set of the constraint values held in {@link #params()}.
 * <p>
 * The only allowed implementation of this interface is {@link DataDef}.
 * <p>
 * <b>Important notice:</b> there is a significant difference between {@link #getValue(IOptionSet)} method of this
 * interface and <code>getXxx()</code> of the {@link IOptionSet}. Behaviour differs only when {@link IOptionSet}
 * contains {@link IAtomicValue#NULL} value with the key {@link #id()}. {@link IDataDef} considers <code>null</code> and
 * {@link IAtomicValue#NULL NULL} as the same case - there is no value in the options set, while {@link IOptionSet}
 * considers {@link IAtomicValue#NULL NULL} as a valid value. So {@link #getValue(IOptionSet)} returns
 * {@link #defaultValue()} instead of {@link IAtomicValue#NULL NULL} while geters returns {@link IAtomicValue#NULL NULL}
 * itself.
 *
 * @author hazard157
 */
public interface IDataDef
    extends IDataType, IStridableParameterized {

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

  @Override
  default String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

  /**
   * Returns the flag that option with identifier {@link #id()} must present in some collection.
   * <p>
   * Common usage of the flag is to reject {@link IOptionSet} if it does not contain mandatory option. For example,
   * {@link #getValue(IOptionSet)} or mandatory data may throw an {@link TsItemNotFoundRtException}.
   * <p>
   * Returns the value of the optional parameter with identifier {@link IAvMetaConstants#TSID_IS_MANDATORY} or
   * <code>false</code> is no such option is found in {@link #params()}.
   *
   * @return boolean - <code>true</code> if option must be present in some collection
   */
  default boolean isMandatory() {
    return params().getBool( TSID_IS_MANDATORY, false );
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
   * By default atomic values are compared with {@link AvUtils#DEFAULT_AV_COMPARATOR}. However for some cases (eg for
   * {@link EAtomicType#VALOBJ}) it is meaningful to compare contained values.
   *
   * @return {@link Comparator}&lt;{@link IAtomicValue}&gt; - the comparator, never is <code>null</code>
   */
  Comparator<IAtomicValue> comparator();

  /**
   * Returns the value of the option with identifier {@link #id()}.
   * <p>
   * Method behavior when option set does not contains the value with identifier {@link #id()} depends on the mandatory
   * flag {@link #isMandatory()} state:
   * <ul>
   * <li>for mandatory options throws an exception {@link TsItemNotFoundRtException};</li>
   * <li>for non-mandatory options returns {@link #defaultValue()}.</li>
   * </ul>
   * {@link #defaultValue()} is also returned when set contains {@link IAtomicValue#NULL} value.
   *
   * @param aOps {@link IOptionSet} - option set
   * @return {@link IAtomicValue} - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException for mandatory option there is no entry in option set under key {@link #id()}
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
