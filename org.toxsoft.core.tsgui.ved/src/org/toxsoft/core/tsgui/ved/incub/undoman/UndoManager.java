package org.toxsoft.core.tsgui.ved.incub.undoman;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IUndoManager} implementation.
 *
 * @author vs
 */
public class UndoManager
    implements IUndoManager {

  /**
   * Deafault value of maximum number of items in undo/redo stack.
   */
  public static final int DEFAULT_MAX_COUNT = 20;

  /**
   * Undo/reado items list used as size restricted stack.
   */
  IListEdit<AbstractUndoRedoItem> items = new ElemArrayList<>();

  /**
   * Указатель на элемент последовательности элементов отмены/возврата операций
   */
  int pointer = -1;

  private final int maxCount;

  private final GenericChangeEventer eventer;

  /**
   * Constructor.
   *
   * @param aMaxCount int - maximum number of items in undo/redo stack
   * @throws TsIllegalArgumentRtException <code>aMaxCount</code> < 1
   */
  public UndoManager( int aMaxCount ) {
    TsIllegalArgumentRtException.checkTrue( aMaxCount < 1 );
    eventer = new GenericChangeEventer( this );
    maxCount = aMaxCount;
  }

  /**
   * Constructor.
   * <p>
   * Max number of items in stack will be set to {@link #DEFAULT_MAX_COUNT}.
   */
  public UndoManager() {
    this( DEFAULT_MAX_COUNT );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void stepBack() {
    TsIllegalStateRtException.checkTrue( pointer < 0 );
    if( pointer >= 0 ) {
      pointer--;
    }
  }

  private void stepForward() {
    TsIllegalStateRtException.checkTrue( pointer > items.size() );
    pointer++;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IUndoManager
  //

  @Override
  public boolean canUndo() {
    return pointer > 0;
  }

  @Override
  public boolean canRedo() {
    if( items.size() >= 0 && pointer < items.size() - 1 ) {
      return true;
    }
    return false;
  }

  @Override
  public void undo() {
    if( canUndo() ) {
      doBeforeUndo();
      IUndoRedoPerformer p = items.get( pointer ).undoPerformer();
      p.doOperation();
      stepBack();
      doAfterUndo();
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void redo() {
    if( canRedo() ) {
      doBeforeRedo();
      stepForward();
      IUndoRedoPerformer p = items.get( pointer ).redoPerformer();
      p.doOperation();
      doAfterRedo();
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void addUndoredoItem( IUndoRedoItem aItem ) {
    pointer++;
    for( int i = items.size() - 1; i >= pointer; i-- ) {
      items.removeByIndex( i );
    }
    items.add( AbstractUndoRedoItem.class.cast( aItem ) );
    if( items.size() > maxCount ) {
      items.removeByIndex( 0 );
      // FIXME dispose removed items
    }
    pointer = items.size() - 1;
    eventer.fireChangeEvent();
  }

  @Override
  public void reset() {
    if( !items.isEmpty() ) {
      items.clear();
      // FIXME dispose removed items
      pointer = -1;
      eventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Is called before UNDO operation is performed.
   * <p>
   * Does nothing in base class so there is no need to call superclass method when overriding.
   */
  protected void doBeforeUndo() {
    // nop
  }

  /**
   * Is called after UNDO operation is performed.
   * <p>
   * Does nothing in base class so there is no need to call superclass method when overriding.
   */
  protected void doAfterUndo() {
    // nop
  }

  /**
   * Is called before REDO operation is performed.
   * <p>
   * Does nothing in base class so there is no need to call superclass method when overriding.
   */
  protected void doBeforeRedo() {
    // nop
  }

  /**
   * Is called after ReDO operation is performed.
   * <p>
   * Does nothing in base class so there is no need to call superclass method when overriding.
   */
  protected void doAfterRedo() {
    // nop
  }

}
