package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.MethodPerActionTsActionSetProvider.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * One action handler {@link ITsActionSetProvider} implementation.
 * <p>
 * All information are suuplied in constructor.
 *
 * @author hazard157
 */
public class AspSingle
    extends AbstractSingleActionSetProvider {

  private final Runnable      runner;
  private final IBooleanState enaState;
  private final IBooleanState checkState;

  /**
   * Defines an action set provider with exactly one action.
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - the action executor
   * @param aEnaState {@link IBooleanState} - determines if action is enables
   * @param aCheckState {@link IBooleanState} - determines if action is checked
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AspSingle( ITsActionDef aActionDef, Runnable aRunner, IBooleanState aEnaState,
      IBooleanState aCheckState ) {
    super( aActionDef );
    TsNullArgumentRtException.checkNulls( aRunner, aEnaState, aCheckState );
    runner = aRunner;
    enaState = aEnaState;
    checkState = aCheckState;
  }

  /**
   * Defines an action set provider with exactly one action.
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - the action executor
   * @param aEnaState {@link IBooleanState} - determines if action is enables
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AspSingle( ITsActionDef aActionDef, Runnable aRunner, IBooleanState aEnaState ) {
    this( aActionDef, aRunner, aEnaState, IBooleanState.ALWAYS_FALSE );
  }

  /**
   * Defines an action set provider with exactly one action.
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - the action executor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AspSingle( ITsActionDef aActionDef, Runnable aRunner ) {
    this( aActionDef, aRunner, IBooleanState.ALWAYS_TRUE, IBooleanState.ALWAYS_FALSE );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSingleActionSetProvider
  //

  @Override
  public void run() {
    runner.run();
  }

  @Override
  protected boolean doIsActionEnabled() {
    return enaState.isState();
  }

  @Override
  protected boolean doIsActionChecked() {
    return checkState.isState();
  }

}
