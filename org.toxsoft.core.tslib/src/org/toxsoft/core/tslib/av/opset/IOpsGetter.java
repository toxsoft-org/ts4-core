package org.toxsoft.core.tslib.av.opset;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface to extract different kind of identified values from the set.
 *
 * @author hazard157
 */
public interface IOpsGetter {

  // TODO сделать опции read-only

  /**
   * Returns <code>true</code> if option with specified identifier exists in the set.
   *
   * @param aId String - specified identifier of the option
   * @return boolean - <code>true</code> if option with specified name exists in the set
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  boolean hasValue( String aId );

  /**
   * Returns <code>true</code> if option with specified identifier exists in the set.
   *
   * @param aOpId {@link IDataDef} - specified identifier of the option
   * @return boolean - <code>true</code> if option with specified name exists in the set
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  boolean hasValue( IDataDef aOpId );

  /**
   * Determines if set does not contains meaningful value.
   *
   * @param aId String - specified identifier of the option
   * @return boolean - NULL option flag<br>
   *         <b>true</b> - either set does not contains specified option, or option value is
   *         {@link IAtomicValue#NULL};<br>
   *         <b>false</b> - set contains specsified option with value not {@link IAtomicValue#NULL}.
   */
  boolean isNull( String aId );

  /**
   * Determines if set does not contains meaningful value.
   *
   * @param aOpId {@link IDataDef} - specified identifier of the option
   * @return boolean - NULL option flag<br>
   *         <b>true</b> - either set does not contains specified option, or option value is
   *         {@link IAtomicValue#NULL};<br>
   *         <b>false</b> - set contains specified option with value not {@link IAtomicValue#NULL}.
   */
  boolean isNull( IDataDef aOpId );

  /**
   * Finds value specified by the identifier.
   *
   * @param aId String - specified identifier
   * @return {@link IAtomicValue} - found value or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  IAtomicValue findValue( String aId );

  /**
   * Finds value specified by the {@link IDataDef} identifier.
   *
   * @param aOpId String - specified {@link IDataDef} identifier
   * @return {@link IAtomicValue} - found value or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  IAtomicValue findValue( IDataDef aOpId );

  /**
   * Returns value specified by the identifier.
   *
   * @param aId String - specified identifier
   * @return {@link IAtomicValue} - value
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no value with spoecified identifier exsist in set
   */
  IAtomicValue getValue( String aId );

  /**
   * Returns value specified by the identifier or default value if none was found.
   *
   * @param aId String - specified identifier
   * @param aDefaultValue {@link IAtomicValue} - default value, may be <code>null</code>
   * @return {@link IAtomicValue} - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   */
  IAtomicValue getValue( String aId, IAtomicValue aDefaultValue );

  /**
   * Returns value specified by the identifier.
   *
   * @param aOpId {@link IDataDef} - specified stridable identifier
   * @return {@link IAtomicValue} - found value
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException no value with spoecified identifier exsist in set
   */
  IAtomicValue getValue( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#BOOLEAN}.
   *
   * @param aId String - the option identifier
   * @return boolean - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  boolean getBool( String aId );

  /**
   * Returns the option as {@link EAtomicType#BOOLEAN} or default value if none was found.
   *
   * @param aId String - the option identifier
   * @param aDefaultValue boolean - default value
   * @return boolean - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  boolean getBool( String aId, boolean aDefaultValue );

  /**
   * Returns the option as {@link EAtomicType#BOOLEAN}.
   *
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return boolean - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  boolean getBool( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#INTEGER}.
   *
   * @param aId String - the option identifier
   * @return int - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  int getInt( String aId );

  /**
   * Returns the option as {@link EAtomicType#INTEGER} or default value if none was found.
   *
   * @param aId String - the option identifier
   * @param aDefaultValue int - default value
   * @return int - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  int getInt( String aId, int aDefaultValue );

  /**
   * Returns the option as {@link EAtomicType#INTEGER}.
   *
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return int - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  int getInt( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#INTEGER}.
   *
   * @param aId String - the option identifier
   * @return long - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  long getLong( String aId );

  /**
   * Returns the option as {@link EAtomicType#INTEGER} or default value if none was found.
   *
   * @param aId String - the option identifier
   * @param aDefaultValue long - default value
   * @return long - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  long getLong( String aId, long aDefaultValue );

  /**
   * Returns the option as {@link EAtomicType#INTEGER}.
   *
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return long - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  long getLong( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#FLOATING}.
   *
   * @param aId String - the option identifier
   * @return float - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  float getFloat( String aId );

  /**
   * Returns the option as {@link EAtomicType#FLOATING} or default value if none was found.
   *
   * @param aId String - the option identifier
   * @param aDefaultValue float - default value
   * @return float - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  float getFloat( String aId, float aDefaultValue );

  /**
   * Returns the option as {@link EAtomicType#FLOATING}.
   *
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return float - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  float getFloat( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#FLOATING}.
   *
   * @param aId String - the option identifier
   * @return double - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  double getDouble( String aId );

  /**
   * Returns the option as {@link EAtomicType#FLOATING} or default value if none was found.
   *
   * @param aId String - the option identifier
   * @param aDefaultValue double - default value
   * @return double - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  double getDouble( String aId, double aDefaultValue );

  /**
   * Returns the option as {@link EAtomicType#FLOATING}.
   *
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return double - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  double getDouble( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#TIMESTAMP}.
   *
   * @param aId String - the option identifier
   * @return long - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  long getTime( String aId );

  /**
   * Returns the option as {@link EAtomicType#TIMESTAMP} or default value if none was found.
   *
   * @param aId String - the option identifier
   * @param aDefaultValue long - default value
   * @return long - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  long getTime( String aId, long aDefaultValue );

  /**
   * Returns the option as {@link EAtomicType#TIMESTAMP}.
   *
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return long - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  long getTime( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#STRING}.
   *
   * @param aId String - the option identifier
   * @return {@link String} - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  String getStr( String aId );

  /**
   * Returns the option as {@link EAtomicType#STRING} or default value if none was found.
   *
   * @param aId String - the option identifier
   * @param aDefaultValue {@link String} - default value, may be <code>null</code>
   * @return {@link String} - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  String getStr( String aId, String aDefaultValue );

  /**
   * Returns the option as {@link EAtomicType#STRING}.
   *
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return {@link String} - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  String getStr( IDataDef aOpId );

  /**
   * Returns the option as {@link EAtomicType#VALOBJ}.
   *
   * @param <T> - class of the value-object
   * @param aId String - the option identifier
   * @return &lt;T&gt - found value
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  <T> T getValobj( String aId );

  /**
   * Returns the option as value-object {@link EAtomicType#VALOBJ} or default value if none was found.
   *
   * @param <T> - class of the value-object
   * @param aId String - the option identifier
   * @param aDefaultValue {@link IAtomicValue} - default value, may be <code>null</code>
   * @return &lt;T&gt - found value or <code>aDefaultValue</code>
   * @throws TsNullArgumentRtException aId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  <T> T getValobj( String aId, T aDefaultValue );

  /**
   * Returns the option as value-object {@link EAtomicType#VALOBJ}.
   *
   * @param <T> - class of the value-object
   * @param aOpId {@link IDataDef} - stridable identifier of the option
   * @return &lt;T&gt - found value
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsItemNotFoundRtException there is no option with specified identifier
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  <T> T getValobj( IDataDef aOpId );

}
