package org.toxsoft.core.tsgui.bricks.tin.impl;

import static org.toxsoft.core.tsgui.bricks.tin.impl.ITsResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Internal implementation of the tree widget.
 *
 * @author hazard157
 */
class TinTree
    extends TsPanel {

  static final int TREE_ROW_HEIGHT = 24;

  ITreeContentProvider contentProvider = new ITreeContentProvider() {

    @Override
    public Object[] getElements( Object aInputElement ) {
      if( aInputElement == null || !(aInputElement instanceof ITinRow node) ) {
        return null;
      }
      return node.visibleChildren().toArray();
    }

    @Override
    public Object[] getChildren( Object aParentElement ) {
      ITinRow node = (ITinRow)aParentElement;
      return node.visibleChildren().toArray();
    }

    @Override
    public Object getParent( Object aElement ) {
      ITinRow node = (ITinRow)aElement;
      return node.parent();
    }

    @Override
    public boolean hasChildren( Object aElement ) {
      ITinRow node = (ITinRow)aElement;
      return !node.visibleChildren().isEmpty();
    }

  };

  private TinWidget  tinWidget;
  private TreeViewer treeViewer;
  private TinTopRow  rootNode = null;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - the SWT parent
   * @param aOwnerWidget {@link TinWidget} - the owner inspector widget
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TinTree( Composite aParent, TinWidget aOwnerWidget ) {
    super( aParent, aOwnerWidget.tsContext() );
    tinWidget = aOwnerWidget;

    setLayout( new BorderLayout() );

    treeViewer = new TreeViewer( this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );

    treeViewer.getTree().setHeaderVisible( true );
    treeViewer.getTree().setLinesVisible( true );
    treeViewer.getTree().addListener( SWT.MeasureItem, event -> event.height = TREE_ROW_HEIGHT );

    TreeViewerColumn columnName = new TreeViewerColumn( treeViewer, SWT.LEFT );
    columnName.getColumn().setText( STR_COLUMN_NAME );
    columnName.getColumn().setWidth( 200 );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ITinRow node = (ITinRow)aCell.getElement();
        /**
         * FIMXE формализовать отображение поля
         */
        aCell.setText( node.nmName() );
      }
    } );

    TreeViewerColumn columnValue = new TreeViewerColumn( treeViewer, SWT.LEFT );
    columnValue.getColumn().setText( STR_COLUMN_VALUE );
    columnValue.getColumn().setWidth( 200 );
    columnValue.setEditingSupport( new TinValueEditingSupport( treeViewer, tsContext() ) );
    columnValue.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ITinRow node = (ITinRow)aCell.getElement();
        ITsVisualsProvider<ITinValue> vv = node.fieldInfo().valueVisualizer();
        aCell.setText( vv.getName( node.getTinValue() ) );
      }
    } );

    treeViewer.setContentProvider( contentProvider );
    treeViewer.getTree().setLayoutData( BorderLayout.CENTER );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  TinTopRow papiGetRoot() {
    return rootNode;
  }

  TinTopRow papiCreateTopRow( ITinTypeInfo aEntityInfo ) {
    return new TinTopRow( tinWidget, aEntityInfo, treeViewer );
  }

  void papiSetRoot( TinTopRow aNode ) {
    rootNode = aNode;
    treeViewer.setInput( rootNode );
    treeViewer.refresh();
  }

  ITinRow getSelectedRow() {
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    return (ITinRow)ss.getFirstElement();
  }

  void setSelectedRow( ITinRow aItem ) {
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aItem != null ) {
      selection = new StructuredSelection( aItem );
    }
    treeViewer.setSelection( selection, true );
  }

  void addSelectionChangedListener( ISelectionChangedListener aLstener ) {
    treeViewer.addSelectionChangedListener( aLstener );
  }

}
