package org.toxsoft.core.tsgui.bricks.qtree.mgr;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Provides actions associated to the nodes of handled kinds.
 *
 * @author hazard157
 */
public interface IQTreeNodeActionsProvider {

  /**
   * Determines if nodes of given kind ID is handled by this provider.
   *
   * @return {@link ITsFilter}&lt;String&gt; - filter to accept node kind ID
   */
  ITsFilter<String> nodeKindIdFilter();

  /**
   * Returns the actions defined for the handled nodes.
   *
   * @return {@link IStridablesList}&lt;{@link ITsActionDef}&gt; - list of actions defined for node
   */
  IStridablesList<ITsActionDef> listActionDefs();

  // TODO IStridablesGroupirator<ITsActionDef> menuGroupirator();
  // TODO IStridablesGroupirator<ITsActionDef> toolbarGroupirator();

  /**
   * Returns ID of action to perform when the handled node is double clicked.
   * <p>
   * If non-empty string, method must return ID of one of the action from list {@link #listActionDefs()}.
   *
   * @return String - ID of action or an empty string for default or no action
   */
  String doubleClickActionId();

  /**
   * Determines actions state for the given node at the time of method call.
   * <p>
   * Any action not listend in <code>aDisabledActionIds</code> are considered as enabled. Any action not listend in
   * <code>aCheckedActionIds</code> are considered as unchecked.
   *
   * @param aNode {@link IQNode} - the node
   * @param aDisabledActionIds {@link IStringListEdit} - list to fill with IDs of disabled actions
   * @param aCheckedActionIds {@link IStringListEdit} - list to fill with IDs of checked actions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException node kind ID is not accepted by {@link #nodeKindIdFilter()}
   */
  void getActionsState( IQNode aNode, IStringListEdit aDisabledActionIds, IStringListEdit aCheckedActionIds );

  /**
   * Executes the action.
   *
   * @param aActionId String - action ID
   * @param aNode {@link IQNode} - the node on which action must be performed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such action is provided in {@link #listActionDefs()}
   * @throws TsIllegalArgumentRtException node kind ID is not accepted by {@link #nodeKindIdFilter()}
   */
  void executeAction( String aActionId, IQNode aNode );

}
