package org.toxsoft.core.tsgui.ved.incub.undoman.tsgui;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.utils.icons.*;

/**
 * The item of the UNDO/REDO operations stack.
 * <p>
 * {@link #params()} may contain {@link IAvMetaConstants#TSID_NAME}, {@link IAvMetaConstants#TSID_DESCRIPTION} and
 * {@link IAvMetaConstants#TSID_ICON_ID} options for item's visualization,
 *
 * @author hazard157
 */
public sealed interface IUndoRedoItem
    extends IParameterized, IIconIdable permits AbstractUndoRedoItem {

  /**
   * Returns the manager responsible for UNDO/REDO. operation.
   *
   * @param <T> - the expected type of the UNDO manager
   * @return &lt;T&gt; - the owner UNDO manager
   */
  <T extends IUndoManager> T manager();

}
