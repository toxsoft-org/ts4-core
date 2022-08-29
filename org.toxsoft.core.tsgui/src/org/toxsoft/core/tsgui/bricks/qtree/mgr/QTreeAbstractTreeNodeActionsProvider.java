package org.toxsoft.core.tsgui.bricks.qtree.mgr;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Abstract implementation of {@link IQTreeNodeActionsProvider}.
 *
 * @author hazard157
 */
public abstract class QTreeAbstractTreeNodeActionsProvider
    implements IQTreeNodeActionsProvider, ITsGuiContextable {

  private final IStridablesListEdit<ITsActionDef> actionDefs = new StridablesList<>();

  private final ITsGuiContext tsContext;

  private String doubleClickActionId = TsLibUtils.EMPTY_STRING;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public QTreeAbstractTreeNodeActionsProvider( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ISqTreeNodeGuiProvider
  //

  @Override
  public abstract ITsFilter<String> nodeKindIdFilter();

  @Override
  public IStridablesList<ITsActionDef> listActionDefs() {
    return actionDefs;
  }

  @Override
  public String doubleClickActionId() {
    return doubleClickActionId;
  }

  @Override
  public void getActionsState( IQNode aNode, IStringListEdit aDisabledActionIds, IStringListEdit aCheckedActionIds ) {
    TsNullArgumentRtException.checkNulls( aNode, aDisabledActionIds, aCheckedActionIds );
    TsIllegalArgumentRtException.checkFalse( nodeKindIdFilter().accept( aNode.kind().id() ) );
    doGetActionsState( aNode, aDisabledActionIds, aCheckedActionIds );
  }

  @Override
  public void executeAction( String aActionId, IQNode aNode ) {
    TsNullArgumentRtException.checkNulls( aActionId, aNode );
    TsItemNotFoundRtException.checkFalse( actionDefs.hasKey( aActionId ) );
    TsIllegalArgumentRtException.checkFalse( nodeKindIdFilter().accept( aNode.kind().id() ) );
    doExecuteAction( aActionId, aNode );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns editable actiondefs.
   *
   * @return {@link IStridablesListEdit}&lt;{@link ITsActionDef}&gt; - an editable list
   */
  public IStridablesListEdit<ITsActionDef> actionDefs() {
    return actionDefs;
  }

  /**
   * Sets {@link #doubleClickActionId}.
   *
   * @param aActionId String - an action ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException when non-empty, argument action ID is not in {@link #listActionDefs()}
   */
  public void setDoubleClickActionId( String aActionId ) {
    TsNullArgumentRtException.checkNull( aActionId );
    if( aActionId.isEmpty() ) {
      TsItemNotFoundRtException.checkFalse( actionDefs.hasKey( aActionId ) );
    }
    doubleClickActionId = aActionId;
  }

  // ------------------------------------------------------------------------------------
  // To implement & override
  //

  /**
   * Subclass must execute correct action.
   * <p>
   * Called from {@link #executeAction(String, IQNode)} after checking argument validity.
   *
   * @param aActionId String - action ID
   * @param aNode {@link IQNode} - the node on which action must be performed
   */
  protected abstract void doExecuteAction( String aActionId, IQNode aNode );

  /**
   * Subclass must determines actions state for the given node at the time of method call.
   * <p>
   * Called from {@link #getActionsState(IQNode, IStringListEdit, IStringListEdit)} after checking argument validity.
   * <p>
   * In the base class does nothing, there is no need to call superclass method when overriding.
   *
   * @param aNode {@link IQNode} - the node
   * @param aDisabledActionIds {@link IStringListEdit} - list to fill with IDs of disabled actions
   * @param aCheckedActionIds {@link IStringListEdit} - list to fill with IDs of checked actions
   */
  protected void doGetActionsState( IQNode aNode, IStringListEdit aDisabledActionIds,
      IStringListEdit aCheckedActionIds ) {
    // nop
  }

}
