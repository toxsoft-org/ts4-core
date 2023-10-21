package org.toxsoft.core.tsgui.ved.incub.undoman;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IUndoRedoManager} implementation.
 *
 * @author vs
 * @author hazard157
 */
public class UndoManager
    implements IUndoRedoManager {

  private static final int      DEFAULT_STACK_SIZE = 100;
  private static final IntRange STACK_SIZE_RANGE   = new IntRange( 10, 1_000 );

  /**
   * UNDO/REDO items list used as size restricted stack.
   */
  IListEdit<IUndoRedoItem> items = new ElemArrayList<>();

  /**
   * The current position index in rtange 0 .. items.size().
   */
  int pointer = 0;

  private final int maxCount;

  private final GenericChangeEventer eventer;

  /**
   * Constructor.
   * <p>
   * Invalid argument will fit
   *
   * @param aMaxCount int - maximum number of items in UNDO/REDO stack
   */
  public UndoManager( int aMaxCount ) {
    eventer = new GenericChangeEventer( this );
    maxCount = STACK_SIZE_RANGE.inRange( aMaxCount );
  }

  /**
   * Constructor.
   * <p>
   * Max number of items in stack will be set to {@link #DEFAULT_STACK_SIZE}.
   */
  public UndoManager() {
    this( DEFAULT_STACK_SIZE );
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
    if( items.size() >= 0 && pointer < items.size() ) {
      return true;
    }
    return false;
  }

  @Override
  public void undo() {
    if( canUndo() ) {
      IUndoRedoItem item = items.get( pointer - 1 );
      doBeforeUndo();
      item.undo();
      pointer--;
      doAfterUndo();
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void redo() {
    if( canRedo() ) {
      IUndoRedoItem item = items.get( pointer );
      doBeforeRedo();
      pointer++;
      item.redo();
      doAfterRedo();
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void addUndoredoItem( IUndoRedoItem aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    for( int i = items.size() - 1; i >= pointer; i-- ) {
      items.removeRangeByIndex( pointer, items.size() - 1 );
    }
    items.add( aItem );
    if( items.size() > maxCount ) {
      items.removeByIndex( 0 );
    }
    pointer = items.size();
    eventer.fireChangeEvent();
  }

  @Override
  public IList<IUndoRedoItem> items() {
    return items;
  }

  @Override
  public IList<IUndoRedoItem> listUndoItems() {
    if( pointer > 0 ) {
      IListEdit<IUndoRedoItem> ll = new ElemArrayList<>( pointer );
      for( int i = pointer - 1; i >= 0; i-- ) {
        ll.add( items.get( i ) );
      }
      return ll;
    }
    return IList.EMPTY;
  }

  @Override
  public IList<IUndoRedoItem> listRedoItems() {
    return items.fetch( pointer, items.size() );
  }

  @Override
  public int currentPosition() {
    return pointer;
  }

  @Override
  public void reset() {
    if( !items.isEmpty() ) {
      items.clear();
      pointer = 0;
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
