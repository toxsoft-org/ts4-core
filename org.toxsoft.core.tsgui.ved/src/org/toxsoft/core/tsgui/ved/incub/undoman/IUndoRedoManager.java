package org.toxsoft.core.tsgui.ved.incub.undoman;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Undo/redo operation manager.
 *
 * @author vs
 */
public interface IUndoRedoManager {

  /**
   * Determines if last operation can be UNDOed.
   *
   * @return true - the flags that undo operation can be done
   */
  boolean canUndo();

  /**
   * Determines if last undo operation can be restored.
   *
   * @return true - the flags that REDO operation can be done
   */
  boolean canRedo();

  /**
   * Undoes last operation
   *
   * @throws TsIllegalStateRtException method {@link #canUndo()} = <code>true</code>
   */
  void undo();

  /**
   * Redoes last undo operation.
   *
   * @throws TsIllegalStateRtException method {@link #canRedo()} = <code>true</code>
   */
  void redo();

  /**
   * Add UNDO/REDO operation to the internal stack.
   * <p>
   * New item is added at the {@link #currentPosition()}, all items after {@link #currentPosition()} will be removed and
   * {@link #currentPosition()} increases by 1.
   *
   * @param aItem {@link IUndoRedoItem} - the UNDO/REDO operation item
   */
  void addUndoredoItem( IUndoRedoItem aItem );

  /**
   * Returns the list of UNDO/REDO items in order as they were added.
   *
   * @return {@link IList}&lt;{@link IUndoRedoItem}&gt; - the items list
   */
  IList<IUndoRedoItem> items();

  /**
   * Returns items scheduled for UNDO operations.
   * <p>
   * This is the list of items from index {@link #currentPosition()}-1 down to 0. So order of items is reversed compared
   * to {@link #items()} because it is the order of {@link #undo()} execution. Returned list may be directly used to
   * display drop-down list under the UNDO button in the application GUI.
   * <p>
   * Method {@link #canUndo()} returns <code>true</code> only of this list is not empty.
   *
   * @return {@link IList}&lt;{@link IUndoRedoItem}&gt; - the UNDO items list in the order of {@link #undo()} calls
   */
  IList<IUndoRedoItem> listUndoItems();

  /**
   * Returns items scheduled for REDO operations.
   * <p>
   * This is the list of items from index {@link #currentPosition()} up to the size of {@link #items()}. Returned list
   * may be directly used to display drop-down list under the UNDO button in the application GUI.
   * <p>
   * Method {@link #canRedo()} returns <code>true</code> only of this list is not empty.
   *
   * @return {@link IList}&lt;{@link IUndoRedoItem}&gt; - the REDO items list in the order of {@link #redo()} calls
   */
  IList<IUndoRedoItem> listRedoItems();

  /**
   * Returns current position in the {@link #items()}.
   * <p>
   * Current position is the index, where the {@link #addUndoredoItem(IUndoRedoItem)} will add an item. After each
   * {@link #undo()} current position decreases, after {@link #undo()} - increases. For an empty list of items method
   * returns 0.
   *
   * @return int - index of the current position in items list
   */
  int currentPosition();

  /**
   * Resets (clears) UNDO/REDO operations stack.
   */
  void reset();

  /**
   * Returns items stack and/or positions change eventer.
   *
   * @return {@link IGenericChangeEventer} - the eventer
   */
  IGenericChangeEventer genericChangeEventer();

}
