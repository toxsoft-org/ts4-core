package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages VED entities (items of VISELs, actors) in the {@link IVedScreenModel}.
 * <p>
 * Notes:
 * <ul>
 * <li>VED screen framework internally uses {@link #prepareFromTemplate(IVedItemCfg)} when creating new VED item. It is
 * highly recommended to use the same approach when adding new items by external means. Thus ensuring the unified item
 * ID generation. However, user may specify it's own ID at the item creation;</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - the type of the managed VED items
 */
public interface IVedItemsManager<T extends VedAbstractItem>
    extends ITsClearable {

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
  IStridablesListReorderer<T> reorderer();

  /**
   * Prepares the item config from some kind of the template config provided.
   * <p>
   * It is assumed that template configuration is provided by external means such as the VED items palette or copy/paste
   * operation. Preparation includes the unique (for current VED screen) ID and name generation.
   * <p>
   * Note: the new ID/name pair will be generated even if template ID/name already is unique.
   * <p>
   * It is guaranteed that {@link #create(int, IVedItemCfg)} method will not throw an "duplicate ID"
   * {@link TsItemAlreadyExistsRtException} exception.
   *
   * @param aTemplateCfg {@link IVedItemCfg} - the item configuration template
   * @return {@link VedItemCfg} - an editable instance based on template
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no item factory found
   */
  VedItemCfg prepareFromTemplate( IVedItemCfg aTemplateCfg );

  /**
   * Creates the item and adds it at the end of the list {@link #list()}.
   *
   * @param aCfg {@link IVedItemCfg} - the configuration the item to create
   * @return &lt;T&gt; - created item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link IVedItemsManagerValidator#canCreate(int, IVedItemCfg)}
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
   * @throws TsValidationFailedRtException failed {@link IVedItemsManagerValidator#canCreate(int, IVedItemCfg)}
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
