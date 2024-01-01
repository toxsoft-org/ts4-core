package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.ved.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED screen activating and running support actions. Handles the actions:
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
    defineAction( ACDEF_ENABLE_ACTORS_CHECK, this::doRunActors, IBooleanState.ALWAYS_TRUE, this::isRunActorsChecked );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  final void doRunActors() {
    boolean enable = !vedScreen.isActorsEnabled();
    if( enable ) {
      doBeforeActorsRun();
      vedScreen.setActorsEnabled( true );
      doAfterActorsRun();
    }
    else {
      doBeforeActorsStop();
      vedScreen.setActorsEnabled( false );
      doAfterActorsStop();
    }
  }

  boolean isRunActorsChecked() {
    return vedScreen.isActorsEnabled();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  // TODO comment
  protected void doBeforeActorsRun() {
    // nop
  }

  // TODO comment
  protected void doAfterActorsRun() {
    // nop
  }

  // TODO comment
  protected void doBeforeActorsStop() {
    // nop
  }

  // TODO comment
  protected void doAfterActorsStop() {
    // nop
  }

}
