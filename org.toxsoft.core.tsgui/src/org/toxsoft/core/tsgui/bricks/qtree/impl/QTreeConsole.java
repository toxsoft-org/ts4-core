package org.toxsoft.core.tsgui.bricks.qtree.impl;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.qtree.IQTreeConsole;
import org.toxsoft.core.tslib.bricks.qnodes.IQNode;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IQTreeConsole} implementation.
 *
 * @author hazard157
 */
public class QTreeConsole
    implements IQTreeConsole {

  private final TreeViewer treeViewer;

  /**
   * Конструктор с привязкой в JFace дереву.
   *
   * @param aTreeViewer {@link TreeViewer} - JFace дерево
   * @throws TsNullArgumentRtException любой = null
   */
  public QTreeConsole( TreeViewer aTreeViewer ) {
    TsNullArgumentRtException.checkNull( aTreeViewer );
    treeViewer = aTreeViewer;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // IQTreeConsole
  //

  @Override
  public void expandNodeTo( IQNode aNode, int aLevel ) {
    int level = aLevel;
    if( level < 0 ) {
      level = AbstractTreeViewer.ALL_LEVELS;
    }
    if( aNode != null ) {
      treeViewer.expandToLevel( aNode, level );
    }
    else {
      treeViewer.expandToLevel( level );
    }
  }

  @Override
  public void collapseNode( IQNode aNode, int aLevel ) {
    int level = aLevel;
    if( level < 0 ) {
      level = AbstractTreeViewer.ALL_LEVELS;
    }
    if( aNode != null ) {
      treeViewer.collapseToLevel( aNode, level );
    }
    else {
      treeViewer.collapseAll();
    }
  }

  @Override
  public void refresh( IQNode aNode ) {
    if( aNode != null ) {
      treeViewer.refresh( aNode, true );
    }
    else {
      treeViewer.refresh( true );
    }
  }

  @Override
  public void update( IQNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    treeViewer.update( aNode, null );
  }

  @Override
  public IQNode selectedNode() {
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    return IQNode.class.cast( ss.getFirstElement() );
  }

  @Override
  public IList<IQNode> selectedNodes() {
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    IListEdit<IQNode> ll = new ElemArrayList<>( ss.size() );
    // 2023-05-15 mvk rap support
    // for( Object o : ss ) {
    // ll.add( IQNode.class.cast( o ) );
    // }
    for( Object o : ss.toList() ) {
      ll.add( IQNode.class.cast( o ) );
    }
    return ll;
  }

  @Override
  public void setSelectedNode( IQNode aNode ) {
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aNode != null ) {
      selection = new StructuredSelection( aNode );
    }
    treeViewer.setSelection( selection, true );
  }

  @Override
  public void setSelectedNodes( IList<IQNode> aNodes ) {
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aNodes != null && !aNodes.isEmpty() ) {
      selection = new StructuredSelection( aNodes.toArray() );
    }
    treeViewer.setSelection( selection, true );

  }

}
