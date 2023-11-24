package org.toxsoft.core.tsgui.ved.incub.undoman.tsgui;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsActionSetProvider} for {@link IUndoManager}.
 * <p>
 * Handles the actions:
 * <ul>
 * <li>{@link ITsStdActionDefs#ACTID_UNDO};</li>
 * <li>{@link ITsStdActionDefs#ACTID_REDO}.</li>
 * </ul>
 *
 * @author hazard157
 */
public class AspUndoManager
    extends MethodPerActionTsActionSetProvider {

  private final IUndoManager undoManager;

  /**
   * Constructor.
   *
   * @param aManager {@link IUndoManager} - the manager providing the action set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AspUndoManager( IUndoManager aManager ) {
    TsNullArgumentRtException.checkNull( aManager );
    undoManager = aManager;
    defineAction( ACDEF_UNDO, this::doHandleUndo, this::isEnabledUndo );
    defineAction( ACDEF_REDO, this::doHandleRedo, this::isEnabledRedo );
    undoManager.genericChangeEventer().addListener( actionsStateEventer() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void doHandleUndo() {
    if( undoManager.canUndo() ) {
      undoManager.undo();
    }
  }

  boolean isEnabledUndo() {
    return undoManager.canUndo();
  }

  void doHandleRedo() {
    if( undoManager.canRedo() ) {
      undoManager.redo();
    }
  }

  boolean isEnabledRedo() {
    return undoManager.canRedo();
  }

}
