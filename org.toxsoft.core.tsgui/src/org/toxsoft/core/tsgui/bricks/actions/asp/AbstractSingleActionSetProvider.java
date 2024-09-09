package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * One action handler {@link ITsActionSetProvider} implementation.
 * <p>
 * Also implements {@link Runnable} interface.
 *
 * @author hazard157
 */
public abstract class AbstractSingleActionSetProvider
    extends AbstractTsActionSetProvider
    implements Runnable {

  private final IStridablesList<ITsActionDef> actionDefList;
  private final ITsActionDef                  actionDef;

  /**
   * Constructor.
   * <p>
   * Note: separator is not allowed as an action definition.
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the argument is a separator
   */
  public AbstractSingleActionSetProvider( ITsActionDef aActionDef ) {
    TsNullArgumentRtException.checkNull( aActionDef );
    TsIllegalArgumentRtException.checkTrue( aActionDef.isSeparator() );
    actionDef = aActionDef;
    actionDefList = new SingleStridableList<>( actionDef );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsActionSetProvider
  //

  @Override
  public IList<ITsActionDef> listAllActionDefs() {
    return actionDefList;
  }

  @Override
  final public IStridablesList<ITsActionDef> listHandledActionDefs() {
    return actionDefList;
  }

  @Override
  final public boolean isActionKnown( String aActionId ) {
    TsNullArgumentRtException.checkNull( aActionId );
    return aActionId.equals( actionDef.id() );
  }

  @Override
  protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
    return doIsActionEnabled();
  }

  @Override
  final public boolean isActionChecked( String aActionId ) {
    if( actionDefList.hasKey( aActionId ) ) {
      return doIsActionChecked();
    }
    return false;
  }

  @Override
  final protected void doHandleAction( String aActionId ) {
    run();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the single provided action.
   *
   * @return {@link ITsActionDef} - the single provided action
   */
  public ITsActionDef getActionDef() {
    return actionDef;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * The subclass must implementa action processing.
   */
  @Override
  public abstract void run();

  /**
   * Subclass may determine action enabled state.
   * <p>
   * In the base class returns <code>true</code>, there is no need to call superclass method when overriding.
   *
   * @return boolean - the action checked state
   */
  protected boolean doIsActionEnabled() {
    return true;
  }

  /**
   * Subclass may determine action checked state.
   * <p>
   * In the base class returns <code>false</code>, there is no need to call superclass method when overriding.
   *
   * @return boolean - the action checked state
   */
  protected boolean doIsActionChecked() {
    return true;
  }

}
