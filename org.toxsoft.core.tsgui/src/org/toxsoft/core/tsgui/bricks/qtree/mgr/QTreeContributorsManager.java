package org.toxsoft.core.tsgui.bricks.qtree.mgr;

import org.toxsoft.core.tslib.bricks.qnodes.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQTreeContributorsManager} implementation.
 *
 * @author hazard157
 */
public class QTreeContributorsManager
    implements IQTreeContributorsManager {

  private final IStringMapEdit<IListEdit<IQNodeChildsContributor>> nodeContibs = new StringMap<>();
  private final IListEdit<IQTreeNodeActionsProvider>               actionProvs = new ElemLinkedBundleList<>();

  /**
   * Constructor.
   */
  public QTreeContributorsManager() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IQTreeContributorsManager
  //

  @Override
  public void registerNodeChildsContibutor( String aNodeKindId, IQNodeChildsContributor aContributor ) {
    StridUtils.checkValidIdPath( aNodeKindId );
    TsNullArgumentRtException.checkNull( aContributor );
    IListEdit<IQNodeChildsContributor> ll = nodeContibs.findByKey( aNodeKindId );
    if( ll == null ) {
      ll = new ElemArrayList<>();
      nodeContibs.put( aNodeKindId, ll );
    }
    if( !ll.hasElem( aContributor ) ) {
      ll.add( aContributor );
    }
  }

  @Override
  public IList<IQNodeChildsContributor> listNodeContributors( String aNodeKindId ) {
    IListEdit<IQNodeChildsContributor> ll = nodeContibs.findByKey( aNodeKindId );
    if( ll == null ) {
      return IList.EMPTY;
    }
    return ll;
  }

  @Override
  public IStringList listNodeKindIdsWithContributors() {
    return nodeContibs.keys();
  }

  @Override
  public void registerNodeActionsProvider( IQTreeNodeActionsProvider aProvider ) {
    if( !actionProvs.hasElem( aProvider ) ) {
      actionProvs.add( aProvider );
    }
  }

  @Override
  public IList<IQTreeNodeActionsProvider> listNodeActionsProviders( String aNodeKindId ) {
    TsNullArgumentRtException.checkNull( aNodeKindId );
    IListEdit<IQTreeNodeActionsProvider> ll = new ElemArrayList<>();
    for( IQTreeNodeActionsProvider p : actionProvs ) {
      if( p.nodeKindIdFilter().accept( aNodeKindId ) ) {
        ll.add( p );
      }
    }
    return ll;
  }

}
