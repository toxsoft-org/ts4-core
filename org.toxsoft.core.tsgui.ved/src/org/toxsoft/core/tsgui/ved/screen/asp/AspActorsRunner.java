package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.ved.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED screen activating and runnning support actions. Handles the actions:
 * <ul>
 * <li>{@link ITsguiVedConstants#ACTID_ENABLE_ACTORS};</li>
 * </ul>
 *
 * @author hazard157
 */
public class AspActorsRunner
    extends MethodPerActionTsActionSetProvider {

  private final IVedScreen vedScreen;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - managed VED screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AspActorsRunner( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    vedScreen = aVedScreen;
    defineAction( ACDEF_ENABLE_ACTORS_CHECK, this::doRunActors, IBooleanState.ALWAY_TRUE, this::isRunActorsChecked );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void doRunActors() {
    boolean enable = !vedScreen.isActorsEnabled();
    if( enable ) {
      // TODO when actors enabled, turn on editing, screen redraw, UNDO, SAVE, etc.
      vedScreen.setActorsEnabled( true );
      doAfterActorsRun();
    }
    else {
      doBeforeActorsStopActorsRun();
      // TODO when actors disabled, turn off editing, screen redraw, UNDO, SAVE, etc.
      vedScreen.setActorsEnabled( false );
    }
  }

  boolean isRunActorsChecked() {
    return vedScreen.isActorsEnabled();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  // TODO comment
  protected void doAfterActorsRun() {
    // nop
  }

  // TODO comment
  protected void doBeforeActorsStopActorsRun() {
    // nop
  }

}
