package org.toxsoft.core.tsgui.ved.zver1.incub.undoman;

import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * Unde/redo operation manager.
 *
 * @author vs
 */
public interface IUndoManager
    extends IGenericChangeEventCapable {

  /**
   * Determines if last operation can be undoed.
   *
   * @return true - the flags that undo operation can be done
   */
  boolean canUndo();

  /**
   * Determines if last undo operation can be restored.
   *
   * @return true - the flags that redo operation can be done
   */
  boolean canRedo();

  /**
   * Undoes last operation
   */
  void undo();

  /**
   * Redoes last undo operation.
   */
  void redo();

  /**
   * Add undo/redo operation to the internal stack.
   * <p>
   * TODO comment: where is added an operation? top of stack or at current position?
   *
   * @param aItem {@link IUndoRedoItem} - the operation item
   */
  void addUndoredoItem( IUndoRedoItem aItem );

  /**
   * Resets undo/redo operations stack.
   */
  void reset();

}
