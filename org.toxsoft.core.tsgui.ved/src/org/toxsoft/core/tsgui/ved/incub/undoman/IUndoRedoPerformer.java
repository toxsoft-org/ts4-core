package org.toxsoft.core.tsgui.ved.incub.undoman;

/**
 * Performs undo or redo operation.
 *
 * @author hazard157
 */
public interface IUndoRedoPerformer {

  /**
   * Implementation must perform the operation.
   */
  void doOperation();

}
