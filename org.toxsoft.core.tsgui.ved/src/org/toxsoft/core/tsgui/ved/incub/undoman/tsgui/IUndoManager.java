package org.toxsoft.core.tsgui.ved.incub.undoman.tsgui;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * UNDO/REDO operation manager.
 *
 * @author vs
 */
public sealed interface IUndoManager
    permits UndoManager {

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
   * @param aItem {@link AbstractUndoRedoItem} - the UNDO/REDO operation item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException UNDO/REDO operation is in progress, {@link #isPerformingUndoRedo()} =
   *           <code>true</code>
   */
  void addUndoredoItem( AbstractUndoRedoItem aItem );

  boolean isEnabled();

  void setEnabled( boolean aEnable );

  /**
   * Determines if right now UNDO or REDO operation is performing.
   * <p>
   * This flag is <code>true</code> when {@link #undo()} or {@link #redo()} method is executing. The manager needs to
   * distinguish between common editing and editing by UNDO/REDO operations. The first one needs to create and add
   * {@link AbstractUndoRedoItem} instances while second one must be ignored.
   *
   * @return boolean - <code>true</code> when UNDO/REDO operation is in progress
   */
  boolean isPerformingUndoRedo();

  // /**
  // * Sets the value of the {@link #isPerformingUndoRedo()} flag.
  // *
  // * @param aUndoRedo - <code>true</code> when UNDO/REDO operation is in progress
  // */
  // void setPerformingUndoRedo( boolean aUndoRedo );

  /**
   * Returns the list of UNDO/REDO items in order as they were added.
   *
   * @return {@link IList}&lt;{@link AbstractUndoRedoItem}&gt; - the items list
   */
  IList<AbstractUndoRedoItem> items();

  /**
   * Returns items scheduled for UNDO operations.
   * <p>
   * This is the list of items from index {@link #currentPosition()}-1 down to 0. So order of items is reversed compared
   * to {@link #items()} because it is the order of {@link #undo()} execution. Returned list may be directly used to
   * display drop-down list under the UNDO button in the application GUI.
   * <p>
   * Method {@link #canUndo()} returns <code>true</code> only of this list is not empty.
   *
   * @return {@link IList}&lt;{@link AbstractUndoRedoItem}&gt; - the UNDO items list in the order of {@link #undo()}
   *         calls
   */
  IList<AbstractUndoRedoItem> listUndoItems();

  /**
   * Returns items scheduled for REDO operations.
   * <p>
   * This is the list of items from index {@link #currentPosition()} up to the size of {@link #items()}. Returned list
   * may be directly used to display drop-down list under the UNDO button in the application GUI.
   * <p>
   * Method {@link #canRedo()} returns <code>true</code> only of this list is not empty.
   *
   * @return {@link IList}&lt;{@link AbstractUndoRedoItem}&gt; - the REDO items list in the order of {@link #redo()}
   *         calls
   */
  IList<AbstractUndoRedoItem> listRedoItems();

  /**
   * Returns current position in the {@link #items()}.
   * <p>
   * Current position is the index, where the {@link #addUndoredoItem(AbstractUndoRedoItem)} will add an item. After
   * each {@link #undo()} current position decreases, after {@link #undo()} - increases. For an empty list of items
   * method returns 0.
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
