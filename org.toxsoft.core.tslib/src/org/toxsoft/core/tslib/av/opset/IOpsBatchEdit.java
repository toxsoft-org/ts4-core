package org.toxsoft.core.tslib.av.opset;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface of options batch editing.
 *
 * @author hazard157
 */
public interface IOpsBatchEdit {

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
   * @return boolean - <code>true</code> if there were any changes
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  boolean extendSet( IOptionSet aOps );

  /**
   * Extends content of the set with options from the specified set.
   * <p>
   * Existing options will remain unchanged, unexisting options will be added to this set.
   *
   * @param aOps IMap&lt;String,{@link IAtomicValue}&gt; - the options to be added
   * @return boolean - <code>true</code> if there were any changes
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   */
  boolean extendSet( IMap<String, ? extends IAtomicValue> aOps );

  /**
   * Refreshes content of the set with options from the specified set.
   * <p>
   * Existing options will be updated, unexisting options will be ignored.
   *
   * @param aOps {@link IOptionSet} - the options to refresh existing ones
   * @return boolean - <code>true</code> if there were any changes
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  boolean refreshSet( IOptionSet aOps );

  /**
   * Refreshes content of the set with options from the specified set.
   * <p>
   * Existing options will be updated, unexisting options will be ignored.
   *
   * @param aOps IMap&lt;String,{@link IAtomicValue}&gt; - the options to refresh existing ones
   * @return boolean - <code>true</code> if there were any changes
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   */
  boolean refreshSet( IMap<String, ? extends IAtomicValue> aOps );

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
  void setAll( IMap<String, ? extends IAtomicValue> aOps );

}
