package org.toxsoft.core.tsgui.bricks.actions.asp;

import static org.toxsoft.core.tsgui.bricks.actions.asp.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Compound handler incorporates other {@link ITsActionSetProvider} implementation.
 *
 * @author hazard157
 */
public class CompoundTsActionSetProvider
    extends AbstractTsActionSetProvider {

  /**
   * All actions including separators.
   */
  private final IListEdit<ITsActionDef> allActionDefs = new ElemArrayList<>();

  /**
   * Handled actions (subset of {@link #allActionDefs} without separators.
   */
  private final IStridablesListEdit<ITsActionDef> actionDefs = new StridablesList<>();

  /**
   * List of all providers added by {@link #addHandler(ITsActionSetProvider)}.
   */
  private final IListEdit<ITsActionSetProvider> allProvidersList = new ElemArrayList<>();

  /**
   * The map to find provider by the <b>action ID</b>.
   */
  private final IStringMapEdit<ITsActionSetProvider> providersByActIdMap = new StringMap<>();

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
    ITsActionSetProvider h = providersByActIdMap.findByKey( aActionId );
    if( h != null ) {
      h.handleAction( aActionId );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsActionSetProvider
  //

  @Override
  public IList<ITsActionDef> listAllActionDefs() {
    return allActionDefs;
  }

  @Override
  public IStridablesList<ITsActionDef> listHandledActionDefs() {
    return actionDefs;
  }

  @Override
  public boolean isActionKnown( String aActionId ) {
    return providersByActIdMap.findByKey( aActionId ) != null;
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    ITsActionSetProvider h = providersByActIdMap.findByKey( aActionId );
    if( h != null ) {
      return h.isActionEnabled( aActionId );
    }
    return true;
  }

  @Override
  public boolean isActionChecked( String aActionId ) {
    ITsActionSetProvider h = providersByActIdMap.findByKey( aActionId );
    if( h != null ) {
      return h.isActionChecked( aActionId );
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Add handler to this compound provider.
   * <p>
   * Action definitions from added provider with the existing ID are ignored, generating the warning message in the log.
   *
   * @param aHandler {@link ITsActionSetProvider} - the handler to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void addHandler( ITsActionSetProvider aHandler ) {
    allProvidersList.add( aHandler );
    for( ITsActionDef adef : aHandler.listAllActionDefs() ) {
      // add separator to all action defs list
      if( adef.isSeparator() ) {
        allActionDefs.add( adef );
        continue;
      }
      // ignore action and generate warning message if action ID already exists
      if( providersByActIdMap.hasKey( adef.id() ) ) {
        LoggerUtils.errorLogger().warning( LOG_FMT_WARN_ACTION_ID_ALREADY_EXISTS, adef.id() );
        continue;
      }
      allActionDefs.add( adef );
      actionDefs.add( adef );
      providersByActIdMap.put( adef.id(), aHandler );
    }
  }

}
