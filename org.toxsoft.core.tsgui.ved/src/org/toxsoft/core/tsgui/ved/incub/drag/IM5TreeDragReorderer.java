package org.toxsoft.core.tsgui.ved.incub.drag;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * Интерфейс класса способного осуществить изменение порядка визелей вызванного перетаскиванием элемента в дереве.
 *
 * @author vs
 */
public interface IM5TreeDragReorderer {

  public void reorder( IM5TreeViewer<IVedVisel> aViewer, IListReorderer<IVedVisel> aReorderer, ITsNode aSource,
      ITsNode aTarget, ECollectionDropPlace aPlace);
}
