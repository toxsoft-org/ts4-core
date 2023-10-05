package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;

public interface IVedItemsManagerListener<T extends VedAbstractItem> {

  void onListChange( IVedItemsManager<T> aSource, ECrudOp aOp, String aId );

}
