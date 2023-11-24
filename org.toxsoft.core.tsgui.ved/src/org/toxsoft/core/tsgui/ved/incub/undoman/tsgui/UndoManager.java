package org.toxsoft.core.tsgui.ved.incub.undoman.tsgui;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IUndoManager} implementation.
 *
 * @author vs
 * @author hazard157
 */
public non-sealed class UndoManager
    implements IUndoManager {

  private static final int      DEFAULT_STACK_SIZE = 100;
  private static final IntRange STACK_SIZE_RANGE   = new IntRange( 10, 1_000 );

  private final GenericChangeEventer eventer;
  private final int                  maxCount;

  /**
   * UNDO/REDO items list used as size restricted stack.
   */
  IListEdit<AbstractUndoRedoItem> items = new ElemLinkedBundleList<>();

  /**
   * The current position index in range 0 .. items.size().
   */
  int pointer = 0;

  /**
   * The value of {@link #isPerformingUndoRedo()} flag.
   */
  boolean isUndoRedoOperation = false;

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

    // DEBUG ---
    eventer.addListener( aSource -> printManagerContent() );
    // ---
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
  // implementation
  //

  @SuppressWarnings( { "nls", "boxing" } )
  void printManagerContent() {
    TsTestUtils.pl( "=== %s (items: %s, pointer=%d)", this.getClass().getSimpleName(), items.size(), pointer );
    for( int i = 0; i < pointer; i++ ) {
      IUndoRedoItem uri = items.get( i );
      TsTestUtils.pl( "   UNDO %s", uri.toString() );
    }
    for( int i = pointer; i < items.size(); i++ ) {
      IUndoRedoItem uri = items.get( i );
      TsTestUtils.pl( "   REDO %s", uri.toString() );
    }
    TsTestUtils.pl( "---" );
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
    if( !canUndo() ) {
      return;
    }
    AbstractUndoRedoItem item = items.get( pointer - 1 );
    isUndoRedoOperation = true;
    try {
      doBeforeUndo();
      item.undo();
      pointer--;
      doAfterUndo();
    }
    finally {
      isUndoRedoOperation = false;
    }
    eventer.fireChangeEvent();
  }

  @Override
  public void redo() {
    if( !canRedo() ) {
      return;
    }
    AbstractUndoRedoItem item = items.get( pointer );
    isUndoRedoOperation = true;
    try {
      doBeforeRedo();
      pointer++;
      item.redo();
      doAfterRedo();
    }
    finally {
      isUndoRedoOperation = false;
    }
    eventer.fireChangeEvent();
  }

  @Override
  public void addUndoredoItem( AbstractUndoRedoItem aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    if( isUndoRedoOperation ) {
      throw new TsIllegalStateRtException();
    }
    // adding new UNDO/REDO operation causes scheduled UNDO operations to disappear
    items.removeRangeByIndex( pointer, items.size() - pointer );
    // now add the new item
    items.add( aItem );
    // if there are too many items in the list, remove the oldest one
    if( items.size() > maxCount ) {
      items.removeByIndex( 0 );
    }
    pointer = items.size();
    eventer.fireChangeEvent();
  }

  @Override
  public boolean isPerformingUndoRedo() {
    return isUndoRedoOperation;
  }

  @Override
  public IList<AbstractUndoRedoItem> items() {
    return items;
  }

  @Override
  public IList<AbstractUndoRedoItem> listUndoItems() {
    if( pointer > 0 ) {
      IListEdit<AbstractUndoRedoItem> ll = new ElemArrayList<>( pointer );
      for( int i = pointer - 1; i >= 0; i-- ) {
        ll.add( items.get( i ) );
      }
      return ll;
    }
    return IList.EMPTY;
  }

  @Override
  public IList<AbstractUndoRedoItem> listRedoItems() {
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
