package org.toxsoft.core.tsgui.ved.incub.undoman;

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
public interface IUndoRedoItem
    extends IParameterized, IIconIdable {

  /**
   * Performs UNDO operation.
   */
  void undo();

  /**
   * Performs REDO operation.
   */
  void redo();

}
