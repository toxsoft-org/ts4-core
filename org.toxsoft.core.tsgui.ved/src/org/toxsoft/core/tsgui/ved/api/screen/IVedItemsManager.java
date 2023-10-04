package org.toxsoft.core.tsgui.ved.api.screen;

import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;

/**
 * Manages VED entities (items of VISELs, actors) in the {@link IVedScreenModel}.
 *
 * @author hazard157
 * @param <T> - the type of the VED items
 */
@SuppressWarnings( "javadoc" ) // TODO comments
public interface IVedItemsManager<T extends VedAbstractItem>
    extends ITsCollectionChangeEventProducer {

  IStridablesList<T> items();

  IStridablesList<T> listAllItems();

  IListReorderer<T> reorderer();

  T create( IVedItemCfg aCfg );

  void remove( String aId );

}
