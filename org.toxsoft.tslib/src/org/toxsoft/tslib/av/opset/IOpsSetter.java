package org.toxsoft.tslib.av.opset;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.errors.AvTypeCastRtException;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Mixin interface to set different kind of values the set.
 *
 * @author hazard157
 */
public interface IOpsSetter
    extends IOpsGetter {

  /**
   * Sets the option value.
   * <p>
   * Type of the existring value will be changed.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue {@link IAtomicValue} - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   */
  void setValue( String aId, IAtomicValue aValue );

  /**
   * Sets the option value.
   * <p>
   * Type of the existring value will be changed.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue {@link IAtomicValue} - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setValue( IDataDef aOpId, IAtomicValue aValue );

  /**
   * Sets the option value of type {@link EAtomicType#BOOLEAN}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue boolean - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setBool( String aId, boolean aValue );

  /**
   * Sets the option value of type {@link EAtomicType#BOOLEAN}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue boolean - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setBool( IDataDef aOpId, boolean aValue );

  /**
   * Sets the option value of type {@link EAtomicType#INTEGER}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue int - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setInt( String aId, int aValue );

  /**
   * Sets the option value of type {@link EAtomicType#INTEGER}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue int - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setInt( IDataDef aOpId, int aValue );

  /**
   * Sets the option value of type {@link EAtomicType#INTEGER}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue long - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setLong( String aId, long aValue );

  /**
   * Sets the option value of type {@link EAtomicType#INTEGER}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue long - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setLong( IDataDef aOpId, long aValue );

  /**
   * Sets the option value of type {@link EAtomicType#FLOATING}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue float - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setFloat( String aId, float aValue );

  /**
   * Sets the option value of type {@link EAtomicType#FLOATING}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue float - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setFloat( IDataDef aOpId, float aValue );

  /**
   * Sets the option value of type {@link EAtomicType#FLOATING}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue double - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setDouble( String aId, double aValue );

  /**
   * Sets the option value of type {@link EAtomicType#FLOATING}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue double - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setDouble( IDataDef aOpId, double aValue );

  /**
   * Sets the option value of type {@link EAtomicType#TIMESTAMP}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue long - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setTime( String aId, long aValue );

  /**
   * Sets the option value of type {@link EAtomicType#TIMESTAMP}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue long - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setTime( IDataDef aOpId, long aValue );

  /**
   * Sets the option value of type {@link EAtomicType#STRING}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue {@link String} - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setStr( String aId, String aValue );

  /**
   * Sets the option value of type {@link EAtomicType#STRING}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue {@link Object} - the value of the option
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setStr( IDataDef aOpId, String aValue );

  /**
   * Sets the option value of type {@link EAtomicType#VALOBJ}.
   *
   * @param aId {@link String} - the identifier (IDpath) of the option
   * @param aValue {@link Object} - the value of the option, may be <code>null</code>
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws TsIllegalArgumentRtException the identifier is not valid IDpath
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setValobj( String aId, Object aValue );

  /**
   * Sets the option value of type {@link EAtomicType#VALOBJ}.
   *
   * @param aOpId {@link IDataDef} - the identifier of the option
   * @param aValue {@link Object} - the value of the option, may be <code>null</code>
   * @throws TsNullArgumentRtException aOpId = <code>null</code>
   * @throws AvTypeCastRtException existing option has incompatible type
   */
  void setValobj( IDataDef aOpId, Object aValue );

  /**
   * Sets the option if it is not defined or has {@link IAtomicValue#NULL} value.
   *
   * @param aId String - option ID
   * @param aValue {@link IAtomicValue} - option value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is nit an IDpath
   */
  void setValueIfNull( String aId, IAtomicValue aValue );

}
