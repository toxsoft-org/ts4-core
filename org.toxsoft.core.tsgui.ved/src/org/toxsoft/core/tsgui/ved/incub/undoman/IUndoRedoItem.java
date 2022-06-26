package org.toxsoft.core.tsgui.ved.incub.undoman;

import org.eclipse.ui.services.*;
import org.toxsoft.core.tslib.av.utils.*;

/**
 * The item of the undo/redo operations stack.
 *
 * @author hazard157
 */
public sealed interface IUndoRedoItem
    extends IStdParameterized, IDisposable permits AbstractUndoRedoItem {

  /**
   * Returns UNDO operation performer.
   *
   * @return {@link IUndoRedoPerformer} - UNDO operation performer
   */
  IUndoRedoPerformer undoPerformer();

  /**
   * Returns REDO operation performer.
   *
   * @return {@link IUndoRedoPerformer} - REDO operation performer
   */
  IUndoRedoPerformer redoPerformer();

}
