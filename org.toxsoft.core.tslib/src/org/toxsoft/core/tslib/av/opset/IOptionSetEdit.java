package org.toxsoft.core.tslib.av.opset;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.coll.IMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * An editable extension of {@link IOptionSet}.
 *
 * @author hazard157
 */
public interface IOptionSetEdit
    extends IOptionSet, IOpsSetter, IStringMapEdit<IAtomicValue> {

  /**
   * Removes parameter from the set.
   *
   * @param aId String - identifier of the option to be removed
   * @return {@link IAtomicValue} - an removed value or <code>null</code> if there was no value
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  IAtomicValue remove( String aId );

  /**
   * Removes parameter from the set.
   *
   * @param aOpId {@link IDataDef} - identifier of the option to be removed
   * @return {@link IAtomicValue} - an removed value or <code>null</code> if there was no value
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  IAtomicValue remove( IDataDef aOpId );

  /**
   * Adds content of the set by the specified set.
   * <p>
   * Existing options will be overwritten, unexisting options will be added to this set.
   *
   * @param aOps {@link IOptionSet} - the options to be added
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void addAll( IOptionSet aOps );

  /**
   * Adds content of the set by the specified set.
   * <p>
   * Existing options will be overwritten, unexisting options will be added to this set.
   *
   * @param aOps IMap&lt;String,{@link IAtomicValue}&gt; - the options to be added
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   */
  void addAll( IMap<String, ? extends IAtomicValue> aOps );

  /**
   * Extends content of the set with options from the specified set.
   * <p>
   * Existing options will remain unchanged, unexisting options will be added to this set.
   *
   * @param aOps {@link IOptionSet} - the options to be added
   * @return boolean - <code>true</code> if there was any changes
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  boolean extendSet( IOptionSet aOps );

  /**
   * Extends content of the set with options from the specified set.
   * <p>
   * Existing options will remain unchanged, unexisting options will be added to this set.
   *
   * @param aOps IMap&lt;String,{@link IAtomicValue}&gt; - the options to be added
   * @return boolean - <code>true</code> if there was any changes
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   */
  boolean extendSet( IMap<String, ? extends IAtomicValue> aOps );

  /**
   * Replaces content of the set by the specified set.
   *
   * @param aOps {@link IOptionSet} - the new content of the set
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void setAll( IOptionSet aOps );

  /**
   * Replaces content of the set by the specified set.
   *
   * @param aOps IMap&lt;String,{@link IAtomicValue}&gt; - the new content of the set
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   */
  @Override
  void setAll( IMap<String, ? extends IAtomicValue> aOps );

}
