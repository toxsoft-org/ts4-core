package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * Listens changes in {@link IVedItemsManager}.
 *
 * @author hazard157
 * @param <T> - the type of the managed VED items
 */
public interface IVedItemsManagerListener<T extends VedAbstractItem> {

  /**
   * Called when items list or item fields changes.
   *
   * @param aSource {@link IVedItemsManager} - the event source
   * @param aOp {@link ECrudOp} - the operation kind
   * @param aId String - affected item ID or <code>null</code> for {@link ECrudOp#LIST}
   */
  void onVedItemsListChange( IVedItemsManager<T> aSource, ECrudOp aOp, String aId );

}
