package org.toxsoft.core.tslib.bricks.strid.more;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStridable} items registry.
 * <p>
 * Basicallу registry is key-value map with following additions:
 * <ul>
 * <li>obviously keys in map are item identifiers {@link IStridable#id()};</li>
 * <li>putting items to map is called <b>registration</b> and removing - <b>unregistration</b>;</li>
 * <li>registry may have always registered <b>builtin</b> items. Builtin items can not by unregistered;</li>
 * <li>type of the items are checked during registration.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - type of the items in registry
 */
public interface IStridablesRegisrty<T extends IStridable>
    extends ITsClearable {

  /**
   * Returns the registered items class.
   *
   * @return {@link Class}&lt;T&gt; - the registered items class
   */
  Class<? extends T> itemClass();

  /**
   * Returns all registered items.
   *
   * @return {@link IStridablesList}&lt;T&gt; - the list of items
   */
  IStridablesList<T> items();

  /**
   * Returns the item by key.
   *
   * @param aId String - the key
   * @return &lt;T&gt; - found item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no item is registered by specified key
   */
  T get( String aId );

  /**
   * Finds the item by key.
   *
   * @param aId String - the key
   * @return &lt;T&gt; - found item or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  T find( String aId );

  /**
   * Determines if there is item registered with specified key.
   *
   * @param aId String - the key
   * @return boolean - <code>true</code> if item is registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean has( String aId );

  /**
   * Determines if item with specified key is a builtin item.
   * <p>
   * If no item is registered with this key returns <code>false</code>.
   * <p>
   * Builtin items can not be unregistered or changed.
   *
   * @param aId String - the key
   * @return boolean - <code>true</code> if item is registered as builtin one
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isBuiltin( String aId );

  /**
   * Regiters the item.
   * <p>
   * Validators may or may not allow to register new item with the exising key.
   *
   * @param aItem &lt;T&gt; - the item to be rgistered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException {@link #svs()} validation failed
   */
  void register( T aItem );

  /**
   * Unregisters the item.
   * <p>
   * If there is no such item registered then method does nothing.
   *
   * @param aId String - the item ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException {@link #svs()} validation failed
   */
  void unregister( String aId );

  /**
   * Returns the registry modification validation support.
   *
   * @return {@link ITsValidationSupport} - the validation support
   */
  ITsValidationSupport<IStridablesRegisrtyValidator<T>> svs();

}
