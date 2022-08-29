package org.toxsoft.core.tsgui.bricks.qtree.mgr;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.qnodes.helpers.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages {@link IQNode} based tree content and visualisation contributors.
 * <p>
 * TODO comment usage and what is "tree visualization" (including node-related actions)<br>
 * Not all kinds of nodes may have contributions, mose of thema have no such capabilities<br>
 * This manager does not defines how node contributors and action prividers are used
 * <p>
 * There may my several different manager in the applications for several different QTrees.
 *
 * @author hazard157
 */
public interface IQTreeContributorsManager {

  // ------------------------------------------------------------------------------------
  // node childs contributon

  /**
   * Registers node child contributor.
   * <p>
   * Duplicate registration has no effect.
   *
   * @param aNodeKindId String - the node kind ID
   * @param aContributor {@link IQNodeChildsContributor} - the contributor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException childs contribution is not allowed for this node kind
   */
  void registerNodeChildsContibutor( String aNodeKindId, IQNodeChildsContributor aContributor );

  /**
   * Returns all contributors, registered for the given node kind.
   *
   * @param aNodeKindId String - the node kind ID
   * @return {@link IList}&lt;{@link IQNodeChildsContributor}&gt; - list of node childs contributors
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IQNodeChildsContributor> listNodeContributors( String aNodeKindId );

  /**
   * Returns the IDs of node kinds with at least one contributor registered.
   *
   * @return {@link IStringList} - list of contributed node kind IDs
   */
  IStringList listNodeKindIdsWithContributors();

  // ------------------------------------------------------------------------------------
  // node actions providers

  /**
   * Registers node actions provider.
   *
   * @param aProvider {@link IQTreeNodeActionsProvider} - the GUI provider for node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  void registerNodeActionsProvider( IQTreeNodeActionsProvider aProvider );

  /**
   * Returns all actions providers, registered for the given node kind.
   *
   * @param aNodeKindId String - the node kind ID or an empty string for all nodes
   * @return {@link IList}&lt;{@link IQTreeNodeActionsProvider}&gt; - list of node GUI providers
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IQTreeNodeActionsProvider> listNodeActionsProviders( String aNodeKindId );

}
