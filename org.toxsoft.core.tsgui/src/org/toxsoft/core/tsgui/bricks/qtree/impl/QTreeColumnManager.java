package org.toxsoft.core.tsgui.bricks.qtree.impl;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.qtree.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQTreeColumnManager} implementation.
 *
 * @author hazard157
 */
class QTreeColumnManager
    implements IQTreeColumnManager, ITsGuiContextable {

  private final IListEdit<QTreeColumn> columns = new ElemLinkedBundleList<>();

  private final QTreeViewer owner;

  public QTreeColumnManager( QTreeViewer aOwner ) {
    owner = aOwner;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private TreeViewer treeViewer() {
    return owner.treeViewer();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return owner.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    while( !columns.isEmpty() ) {
      QTreeColumn c = columns.removeByIndex( 0 );
      c.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // IQTreeColumnManager
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<IQTreeColumn> columns() {
    return (IList)columns;
  }

  @Override
  public IQTreeColumn addColumn( String aTitle, EHorAlignment aAlignment, ITsVisualsProvider<IQNode> aVisProvider ) {
    TsNullArgumentRtException.checkNulls( aTitle, aAlignment, aVisProvider );
    int colIndex = columns.size();
    TreeViewerColumn tvCol = new TreeViewerColumn( treeViewer(), aAlignment.swtStyle(), colIndex );
    owner.applyLabelProviderChanges();
    QTreeColumn col = new QTreeColumn( tvCol.getColumn(), aVisProvider );
    columns.add( col );
    if( !treeViewer().getTree().getHeaderVisible() ) {
      treeViewer().getTree().setHeaderVisible( true );
    }
    tvCol.getColumn().setText( aTitle );
    tvCol.getColumn().setAlignment( aAlignment.swtStyle() );
    tvCol.getColumn().setWidth( 50 );
    // adding column forces to set header bar visible, restore "header visible" state
    owner.applyHeaderVisibility();
    return col;
  }

}
