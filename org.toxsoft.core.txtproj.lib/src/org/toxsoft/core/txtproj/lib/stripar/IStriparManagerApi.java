package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * STRIPAR management API, common for any implementation.
 *
 * @author hazard157
 * @param <E> - stridable parameterized entity type
 */
public interface IStriparManagerApi<E extends IStridable & IParameterized>
    extends ITsClearableEx, IGenericChangeEventCapable {

  /**
   * Returns the items.
   *
   * @return {@link IStridablesList}&lt;E&gt; - the list of items
   */
  IStridablesList<E> items();

  /**
   * Creates the new instance of the STRIPAR entity.
   *
   * @param aId String - the entity ID (an IDpath)
   * @param aParams {@link IOptionSet} - entity parameters initial values
   * @return &lt;E&gt; - created item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link IStriparManagerValidator#canCreateItem(String, IOptionSet)}
   */
  E createItem( String aId, IOptionSet aParams );

  /**
   * Edits the ID and/or parameters of the item.
   * <p>
   * Argument <code>aParams</code> may contain only options to be changed/added, or event be an empty set.
   * <p>
   * Returned instance may be an update existing instance or the new instance.
   * <p>
   * If the ID is not changed generates {@link ECrudOp#EDIT} event. When ID is changed, generates two events
   * {@link ECrudOp#REMOVE} with <code>aOldId</code> and {@link ECrudOp#CREATE} with <code>aId</code>.
   *
   * @param aOldId String - the ID of the existing item to change
   * @param aId String - item new ID (an IDpath), may be the same as old ID
   * @param aParams IOptionSet - changed parameters values
   * @return &lt;E&gt; - an edited items (may be newly created instance)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation failed
   */
  E editItem( String aOldId, String aId, IOptionSet aParams );

  /**
   * Changes only parameters of the item without changing the ID.
   * <p>
   * Argument <code>aParams</code> may contain only options to be changed/added, or event be an empty set.
   * <p>
   * Returned instance may be an update existing instance or the new instance.
   *
   * @param aId String - existing item ID (an IDpath)
   * @param aParams IOptionSet - changed parameters values
   * @return &lt;E&gt; - an edited items (may be newly created instance)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed
   *           {@link IStriparManagerValidator#canEditItem(String, String, IOptionSet)}
   */
  E editItem( String aId, IOptionSet aParams );

  /**
   * Removes the item.
   * <p>
   * If such item does not exists then method does nothing.
   *
   * @param aId String - the ID of the removed entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException не прошла {@link IStriparManagerValidator#canRemoveItem(String)}
   */
  void removeItem( String aId );

  /**
   * Returns definitions of the TSRIPAR {@link IParameterized#params()} options.
   * <p>
   * Note: returned list contain at least two options with the IDs {@link IAvMetaConstants#TSID_NAME} and
   * {@link IAvMetaConstants#TSID_DESCRIPTION}.
   * <p>
   * All options from this list are checked by built-in validator of the {@link #svs()}.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   */
  IStridablesList<IDataDef> listParamDefs();

  /**
   * Returns the service eventer.
   *
   * @return {@link IStriparManagerListener} - the service eventer
   */
  ITsEventer<IStriparManagerListener> eventer();

  /**
   * Returns the service validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link IStriparManagerValidator}&gt; - the service validator
   */
  ITsValidationSupport<IStriparManagerValidator> svs();

}
