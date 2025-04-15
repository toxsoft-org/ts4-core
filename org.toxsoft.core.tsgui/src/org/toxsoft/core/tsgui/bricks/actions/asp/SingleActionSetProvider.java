package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.MethodPerActionTsActionSetProvider.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * One action handler {@link ITsActionSetProvider} implementation with methods specified in constructor.
 *
 * @author hazard157
 */
public class SingleActionSetProvider
    extends AbstractSingleActionSetProvider {

  private final Runnable      runner;
  private final IBooleanState execChecker;
  private final IBooleanState checkedChecked;

  /**
   * Constructor.
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - action executor
   * @param aExecChecker {@link IBooleanState} - method to check if execution is enabled
   * @param aCheckedChecker {@link IBooleanState} - method to check if action is checked
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument is a separator
   */
  public SingleActionSetProvider( ITsActionDef aActionDef, Runnable aRunner, IBooleanState aExecChecker,
      IBooleanState aCheckedChecker ) {
    super( aActionDef );
    TsNullArgumentRtException.checkNulls( aRunner, aExecChecker, aCheckedChecker );
    runner = aRunner;
    execChecker = aExecChecker;
    checkedChecked = aCheckedChecker;
  }

  /**
   * Constructor of always unchecked action.
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - action executor
   * @param aExecChecker {@link IBooleanState} - method to check if execution is enabled
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument is a separator
   */
  public SingleActionSetProvider( ITsActionDef aActionDef, Runnable aRunner, IBooleanState aExecChecker ) {
    this( aActionDef, aRunner, aExecChecker, IBooleanState.ALWAYS_FALSE );
  }

  /**
   * Constructor of always enabled and unchecked action.
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @param aRunner {@link Runnable} - action executor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument is a separator
   */
  public SingleActionSetProvider( ITsActionDef aActionDef, Runnable aRunner ) {
    this( aActionDef, aRunner, IBooleanState.ALWAYS_TRUE );
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
    return execChecker.isState();
  }

  @Override
  protected boolean doIsActionChecked() {
    return checkedChecked.isState();
  }

}
