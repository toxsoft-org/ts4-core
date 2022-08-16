package org.toxsoft.core.tsgui.m5.gui.viewers.impl;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.ITsTreeMaker;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Default implementation of {@link ITsTreeMaker} creates the flat list of root nodes.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5DefaultTreeMaker<T>
    implements ITsTreeMaker<T> {

  /**
   * Autogenerated node kind ID.
   */
  public static final String KIND_ID = "default_node_kind"; //$NON-NLS-1$

  private final ITsNodeKind<T> nodeKind;

  private final Class<T> itemClass;

  /**
   * Constructor.
   *
   * @param aItemClass {@link Class}&lt;T&gt; - modelled entity type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5DefaultTreeMaker( Class<T> aItemClass ) {
    itemClass = TsNullArgumentRtException.checkNull( aItemClass );
    nodeKind = new TsNodeKind<>( KIND_ID, KIND_ID, TsLibUtils.EMPTY_STRING, itemClass, false, null );
  }

  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<T> aItems ) {
    TsNullArgumentRtException.checkNulls( aRootNode, aItems );
    if( aItems.isEmpty() ) {
      return IList.EMPTY;
    }
    IListEdit<ITsNode> list = new ElemLinkedBundleList<>();
    for( T item : aItems ) {
      ITsNode node = new DefaultTsNode<>( nodeKind, aRootNode, item );
      list.add( node );
    }
    return list;
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    return aNode.kind() == nodeKind;
  }

}
