package org.toxsoft.core.tsgui.bricks.actions.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * The separator.
 *
 * @author hazard157
 */
public final class SeparatorTsActionSetProvider
    implements ITsActionSetProvider {

  /**
   * The singleton instance.
   */
  public static final ITsActionSetProvider INSTANCE = new SeparatorTsActionSetProvider();

  private final IStridablesList<ITsActionDef> LIST = new StridablesList<>( ACDEF_SEPARATOR );

  private SeparatorTsActionSetProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ITsActionSetProvider
  //

  @Override
  public IStridablesList<ITsActionDef> listHandledActionDefs() {
    return LIST;
  }

  @Override
  public boolean isActionKnown( String aActionId ) {
    return true;
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    return false;
  }

  @Override
  public boolean isActionChecked( String aActionId ) {
    return false;
  }

  @Override
  public IGenericChangeEventer actionsStateEventer() {
    return NoneGenericChangeEventer.INSTANCE;
  }

}
