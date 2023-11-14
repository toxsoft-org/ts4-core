package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages VED entities (items of VISELs, actors) in the {@link IVedScreenModel}.
 *
 * @author hazard157
 * @param <T> - the type of the managed VED items
 */
public interface IVedItemsManager<T extends VedAbstractItem>
    extends ITsClearable {

  /**
   * TODO add ID generation: String nextId( String aFactoryId );
   */

  /**
   * Returns the managed items.
   *
   * @return {@link IStridablesList}&lt;T&gt; - the ordered list of items
   */
  IStridablesList<T> list();

  /**
   * Returns the managed items order change means.
   *
   * @return {@link IListReorderer}&ltT&gt; - the {@link #list()} re-orderer
   */
  IListReorderer<T> reorderer();

  /**
   * Creates the item and adds it at the end of the list {@link #list()}.
   *
   * @param aCfg {@link IVedItemCfg} - the configuration the item to create
   * @return &lt;T&gt; - created item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException item with the same ID already exists
   * @throws TsItemNotFoundRtException no registered factory for {@link IVedItemCfg#factoryId()}
   */
  default T create( IVedItemCfg aCfg ) {
    return create( list().size(), aCfg );
  }

  /**
   * Creates the item and inserts at the specified position in the list {@link #list()}.
   *
   * @param aIndex int - index of inserted item
   * @param aCfg {@link IVedItemCfg} - the configuration the item to create
   * @return &lt;T&gt; - created item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid index
   * @throws TsItemAlreadyExistsRtException item with the same ID already exists
   * @throws TsItemNotFoundRtException no registered factory for {@link IVedItemCfg#factoryId()}
   */
  T create( int aIndex, IVedItemCfg aCfg );

  /**
   * Removes the item by ID.
   * <p>
   * IF items with the specified ID does not exist then method does nothing.
   *
   * @param aId String - the ID of the item to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void remove( String aId );

  /**
   * Returns the manager validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link IVedItemsManagerValidator}&gt; - the manager validator
   */
  ITsValidationSupport<IVedItemsManagerValidator> svs();

  /**
   * Returns the manager eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IVedItemsManagerListener}&gt; - the manager eventer
   */
  ITsEventer<IVedItemsManagerListener<T>> eventer();

}
