package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Compound handler incorporates other {@link ITsActionSetProvider} implementation.
 *
 * @author hazard157
 */
public class CompoundTsActionSetProvider
    extends AbstractTsActionSetProvider {

  private final IStridablesListEdit<ITsActionDef>  actionDefs   = new StridablesList<>();
  private final IListEdit<ITsActionSetProvider>      handlersList = new ElemArrayList<>();
  private final IStringMapEdit<ITsActionSetProvider> handlersMap  = new StringMap<>();

  /**
   * Constructor.
   */
  public CompoundTsActionSetProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsActionSetProvider
  //

  @Override
  protected void doHandleAction( String aActionId ) {
    ITsActionSetProvider h = handlersMap.findByKey( aActionId );
    if( h != null ) {
      h.handleAction( aActionId );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsActionSetProvider
  //

  @Override
  public IStridablesList<ITsActionDef> listHandledActionDefs() {
    return actionDefs;
  }

  @Override
  public boolean isActionKnown( String aActionId ) {
    return handlersMap.findByKey( aActionId ) != null;
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    ITsActionSetProvider h = handlersMap.findByKey( aActionId );
    if( h != null ) {
      return h.isActionEnabled( aActionId );
    }
    return true;
  }

  @Override
  public boolean isActionChecked( String aActionId ) {
    ITsActionSetProvider h = handlersMap.findByKey( aActionId );
    if( h != null ) {
      return h.isActionChecked( aActionId );
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Add handler to this compound handler.
   *
   * @param aHandler {@link ITsActionSetProvider} - the handler to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void addHandler( ITsActionSetProvider aHandler ) {
    if( !handlersList.hasElem( aHandler ) ) {
      for( ITsActionDef adef : aHandler.listHandledActionDefs() ) {
        TsItemAlreadyExistsRtException.checkTrue( handlersMap.hasKey( adef.id() ) );
        handlersMap.put( adef.id(), aHandler );
      }
      actionDefs.addAll( aHandler.listHandledActionDefs() );
      handlersList.add( aHandler );
      aHandler.actionsStateEventer().addListener( actionsStateEventer() );
    }
  }

  /**
   * Removes the handler.
   *
   * @param aHandler {@link ITsActionSetProvider} - the handler to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void removeHandler( ITsActionSetProvider aHandler ) {
    if( handlersList.remove( aHandler ) >= 0 ) {
      aHandler.actionsStateEventer().removeListener( actionsStateEventer() );
      handlersList.remove( aHandler );
      for( ITsActionDef adef : aHandler.listHandledActionDefs() ) {
        handlersMap.removeByKey( adef.id() );
        actionDefs.removeByKey( adef.id() );
      }
    }
  }

}
