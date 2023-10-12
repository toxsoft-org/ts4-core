package org.toxsoft.core.tsgui.ved.m5;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Groups VED items in M5 collection panel by {@link EVedItemKind}.
 *
 * @author hazard157
 */
class VedItemM5TreeMakeByKind
    implements ITsTreeMaker<IVedItem> {

  public static final ITsNodeKind<EVedItemKind> NK_KIND = new TsNodeKind<>( "Kind", EVedItemKind.class, true ); //$NON-NLS-1$
  public static final ITsNodeKind<IVedItem>     NK_ITEM = new TsNodeKind<>( "Item", IVedItem.class, false );    //$NON-NLS-1$

  /**
   * Constructor.
   */
  public VedItemM5TreeMakeByKind() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ITsTreeMaker
  //

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IVedItem> aItems ) {
    // nodes of kinds
    IMapEdit<EVedItemKind, DefaultTsNode<EVedItemKind>> kindNodes = new ElemMap<>();
    for( EVedItemKind k : EVedItemKind.asList() ) {
      DefaultTsNode<EVedItemKind> node = new DefaultTsNode<>( NK_KIND, aRootNode, k );
      node.setName( k.nmName() );
      node.setIconId( k.iconId() );
      kindNodes.put( k, node );
    }
    // items in kind nodes
    for( IVedItem item : aItems ) {
      DefaultTsNode<EVedItemKind> parent = kindNodes.getByKey( item.kind() );
      DefaultTsNode<IVedItem> node = new DefaultTsNode<>( NK_ITEM, parent, item );
      parent.addNode( node );
      node.setName( item.nmName() );
    }
    return (IList)kindNodes.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_ITEM;
  }

}
