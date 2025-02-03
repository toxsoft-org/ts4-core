package org.toxsoft.core.tsgui.bricks.actions.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

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

  private boolean actSetEnabled = true;

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
  public IList<ITsActionDef> listAllActionDefs() {
    return LIST;
  }

  @Override
  public IStridablesList<ITsActionDef> listHandledActionDefs() {
    return IStridablesList.EMPTY;
  }

  @Override
  public ITsActionDef findActionDef( String aActionId ) {
    TsNullArgumentRtException.checkNull( aActionId );
    if( aActionId.equals( ACTID_SEPARATOR ) ) {
      return ACDEF_SEPARATOR;
    }
    return null;
  }

  @Override
  public boolean isActionKnown( String aActionId ) {
    return true;
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    TsNullArgumentRtException.checkNull( aActionId );
    if( aActionId.equals( ACTID_SEPARATOR ) ) {
      return actSetEnabled;
    }
    return false;
  }

  @Override
  public boolean isActionChecked( String aActionId ) {
    return false;
  }

  @Override
  public boolean isActionSetEnabled() {
    return actSetEnabled;
  }

  @Override
  public void setActionSetEnabled( boolean aEnabled ) {
    actSetEnabled = aEnabled;
  }

  @Override
  public IGenericChangeEventer actionsStateEventer() {
    return NoneGenericChangeEventer.INSTANCE;
  }

  @Override
  public void addPostActionListener( ITsActionHandler aListener ) {
    // nop
  }

  @Override
  public void removePostActionListener( ITsActionHandler aListener ) {
    // nop
  }

}
