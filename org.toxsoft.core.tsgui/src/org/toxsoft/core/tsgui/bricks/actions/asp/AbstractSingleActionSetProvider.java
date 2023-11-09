package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
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
   *
   * @param aActionDef {@link ITsActionDef} - the action definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractSingleActionSetProvider( ITsActionDef aActionDef ) {
    TsNullArgumentRtException.checkNull( aActionDef );
    actionDef = aActionDef;
    actionDefList = new SingleStridableList<>( actionDef );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsActionSetProvider
  //

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
  final public boolean isActionEnabled( String aActionId ) {
    if( actionDefList.hasKey( aActionId ) ) {
      return doIsActionEnabled();
    }
    return true;
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
