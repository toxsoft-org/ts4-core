package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages VED entities (items of VISELs, actors) in the {@link IVedScreenModel}.
 *
 * @author hazard157
 * @param <T> - the type of the VED items
 */
public interface IVedItemsManager<T extends VedAbstractItem> {

  IStridablesList<T> list();

  IStridablesList<T> listAllItems();

  IListReorderer<T> reorderer();

  /**
   * Creates the item and inserts at the specified position in the list {@link #listAllItems()}.
   *
   * @param aIndex int - index of inserted item
   * @param aCfg {@link IVedItemCfg} - the configuration the item to create
   * @return &lt;T&gt; - created item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid index
   * @throws TsItemNotFoundRtException no registered factory for {@link IVedItemCfg#factoryId()}
   */
  T create( int aIndex, IVedItemCfg aCfg );

  void remove( String aId );

  ITsEventer<IVedItemsManagerListener<T>> eventer();

  default T create( IVedItemCfg aCfg ) {
    return create( listAllItems().size(), aCfg );
  }

}
