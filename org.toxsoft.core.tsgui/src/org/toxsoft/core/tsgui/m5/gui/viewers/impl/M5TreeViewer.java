package org.toxsoft.core.tsgui.m5.gui.viewers.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.util.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.tstree.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An {@link IM5TreeViewer} implementation.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5TreeViewer<T>
    extends M5AbstractCollectionViewer<T>
    implements IM5TreeViewer<T> {

  /**
   * Tree column, an {@link IM5Column} implementation.
   *
   * @author hazard157
   */
  class Column
      extends M5AbstractColumn<T> {

    final TreeViewerColumn tvColumn;

    Column( M5AbstractCollectionViewer<T> aOwner, String aFieldId, TreeViewerColumn aTvColumn,
        IM5Getter<T, ?> aGetter ) {
      super( aOwner, aFieldId, aGetter );
      tvColumn = aTvColumn;
      init();
    }

    TreeViewerColumn getTreeViewerColumn() {
      return tvColumn;
    }

    @Override
    public void setValedEditingSupport( ITsNodeValedProvider aValedProvider ) {
      TsNullArgumentRtException.checkNull( aValedProvider );
      tvColumn.setEditingSupport( new TsNodeEditingSupport( treeViewer, tsContext(), aValedProvider ) );
    }

    @Override
    void doSetJfaceColumnWidth( int aWidth ) {
      tvColumn.getColumn().setWidth( aWidth );
    }

    @Override
    int doGetJfaceColumnWidth() {
      return tvColumn.getColumn().getWidth();
    }

    @Override
    void doSetJfaceColumnAlignment( int aSwtStyleBit ) {
      tvColumn.getColumn().setAlignment( aSwtStyleBit );
    }

    @Override
    int doGetJfaceColumnAlignment() {
      return tvColumn.getColumn().getAlignment();
    }

    @Override
    void doSetJfaceColumnText( String aText ) {
      tvColumn.getColumn().setText( aText );
    }

    @Override
    String doGetJfaceColumnText() {
      return tvColumn.getColumn().getText();
    }

    @Override
    void doSetJfaceColumnTooltip( String aTooltip ) {
      tvColumn.getColumn().setToolTipText( aTooltip );
    }

    @Override
    String doGetJfaceColumnTooltip() {
      return tvColumn.getColumn().getToolTipText();
    }

    @Override
    void doSetJfaceColumnImage( Image aImage ) {
      tvColumn.getColumn().setImage( aImage );
    }

    @Override
    Image doGetJfaceColumnImage() {
      return tvColumn.getColumn().getImage();
    }

    @Override
    void doSetJfaceColumnResizable( boolean aResizable ) {
      tvColumn.getColumn().setResizable( aResizable );
    }

    @Override
    int getFirstColumnWidthAddition() {
      int colIndex = ownerM5Viewer.columnManager().columns().values().indexOf( this );
      if( colIndex == 0 ) {
        // add item height to first cell width for the tree expand/collapse icon
        return treeViewer.getTree().getItemHeight();
      }
      return 0;

    }

    @Override
    void doJfacePack() {
      tvColumn.getColumn().pack();
    }

  }

  /**
   * Tree column manager, an {@link IM5ColumnManager} implementation.
   *
   * @author hazard157
   */
  class ColumnManager
      extends M5AbstractColumnManager<T> {

    ColumnManager( M5AbstractCollectionViewer<T> aOwner ) {
      super( aOwner );
    }

    @Override
    IM5Column<T> findM5ColumnBySwtColumn( Widget aSwtViewerColumn ) {
      for( IM5Column<T> c : columns().values() ) {
        if( ((Column)c).getTreeViewerColumn().getColumn().equals( aSwtViewerColumn ) ) {
          return c;
        }
      }
      return null;
    }

    @Override
    protected IM5Column<T> doInsert( int aColumnIndex, String aFieldId, IM5Getter<T, ?> aGetter ) {
      checkStateValidity();
      TsIllegalArgumentRtException.checkTrue( aColumnIndex < 0 || aColumnIndex > columns().size() );
      IM5FieldDef<T, ?> fDef = owner.model().fieldDefs().findByKey( aFieldId );
      TsItemNotFoundRtException.checkNull( fDef );
      TsItemAlreadyExistsRtException.checkTrue( columns().hasKey( aFieldId ) );
      // create SWT-column TreeViewerColumn
      TreeViewerColumn tvColumn = new TreeViewerColumn( treeViewer, SWT.LEFT, aColumnIndex );
      treeViewer.setLabelProvider( labelProvider ); // refresh label provider for cells of this column
      tvColumn.getColumn().addSelectionListener( headerClickHandler );
      // create M5-column M5AbstractColumn
      M5AbstractColumn<T> col = new Column( owner, aFieldId, tvColumn, aGetter );
      // add to columns()
      if( aColumnIndex == columns().size() ) { // add to the end
        columns().put( aFieldId, col );
      }
      else { // insert at spcified index using temporary list
        IListEdit<IM5Column<T>> tmpList = new ElemLinkedBundleList<>( columns().values() );
        tmpList.insert( aColumnIndex, col );
        columns().clear();
        for( IM5Column<T> c : tmpList ) {
          columns().put( c.fieldId(), c );
        }
      }
      return col;
    }

    @Override
    protected void doRemoveControlFromControl( IM5Column<T> aRemovedColumn ) {
      ((Column)aRemovedColumn).getTreeViewerColumn().getColumn().dispose();
    }

  }

  /**
   * Tree label provider returns cell text and image using {@link IM5Column}.
   *
   * @author hazard157
   */
  class InternalTreeLabelProvider
      extends InternalAbstractLabelProvider {

    @Override
    public String getText( Object aElement ) {
      if( aElement instanceof ITsNode node ) {
        return node.name();
      }
      return null;
    }

    @Override
    public Image getImage( Object aElement ) {
      if( aElement instanceof ITsNode node ) {
        return node.getIcon( iconSize() );
      }
      return null;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public String getColumnText( Object aElement, int aColumnIndex ) {
      if( aElement instanceof ITsNode node ) {
        if( treeMaker().isItemNode( node ) ) {
          IM5Column<T> col = columnManager().columns().values().get( aColumnIndex );
          return col.getCellText( (T)node.entity() );
        }
        // for nodes that does not contains modeled entities use only first column
        if( aColumnIndex == 0 ) {
          return node.name();
        }
      }
      return null;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Image getColumnImage( Object aElement, int aColumnIndex ) {
      if( !(aElement instanceof ITsNode node) ) {
        return null;
      }
      // for nodes that contains modeled entities each cell may have icon
      if( treeMaker.isItemNode( node ) ) {
        IM5Column<T> col = columnManager().columns().values().get( aColumnIndex );
        if( col.isUseThumb() ) {
          TsImage thumb = col.getCellThumb( (T)node.entity(), thumbSize() );
          if( thumb != null ) {
            return thumb.image();
          }
          return null;
        }
        return col.getCellIcon( (T)node.entity(), iconSize() );
      }
      // node icon (not a cell icon!) will be displayed only in first column
      if( aColumnIndex == 0 ) {
        return node.getIcon( iconSize() );
      }
      return null;
    }

  }

  // TODO TRANSLATE

  /**
   * Дерево-специфичный сортировщик, реализация {@link IM5SortManager}.
   *
   * @author hazard157
   */
  class InternalTreeSortManager
      extends InternalSortManager {

    @SuppressWarnings( "unchecked" )
    @Override
    protected int doCompare( Object aO1, Object aO2 ) {
      ITsNode n1 = (ITsNode)aO1;
      ITsNode n2 = (ITsNode)aO2;
      if( treeMaker().isItemNode( n1 ) && treeMaker().isItemNode( n2 ) ) {
        return objectComparator.compare( (T)n1.entity(), (T)n2.entity() );
      }
      return 0;
    }

  }

  private static final String ROOT_KIND_ID       = "M5TreeViewerRoot";           //$NON-NLS-1$
  /**
   * Стиль создаваемой таблицы.
   */
  protected static final int  DEFAULT_TREE_STYLE =
      SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION;

  private final ITsTreeMaker<T> DEFAULT_TREE_MAKER;

  @SuppressWarnings( "rawtypes" )
  private final ITsNodeKind<M5TreeViewer> ROOT_KIND =
      new TsNodeKind<>( ROOT_KIND_ID, EMPTY_STRING, EMPTY_STRING, M5TreeViewer.class, true, null );

  private TreeViewer                  treeViewer         = null;
  private CheckboxTreeViewer          checkboxTreeViewer = null;
  private ViewerPaintHelper<Tree>     paintHelper        = null;
  @SuppressWarnings( "rawtypes" )
  private DefaultTsNode<M5TreeViewer> rootNode;
  private ITsTreeViewerConsole        console            = null;
  private ITsTreeMaker<T>             treeMaker;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5TreeViewer( ITsGuiContext aContext, IM5Model<T> aModel ) {
    this( aContext, aModel, new NotifierListEditWrapper<>( new ElemLinkedBundleList<>() ), false );
  }

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aIsChecksSupported boolean - a sign that the implementation supports the checked state of elements
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5TreeViewer( ITsGuiContext aContext, IM5Model<T> aModel, boolean aIsChecksSupported ) {
    this( aContext, aModel, new NotifierListEditWrapper<>( new ElemLinkedBundleList<>() ), aIsChecksSupported );
  }

  /**
   * Constructor for subclass.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItems {@link INotifierListEdit}&lt;T&gt; - the list used to store elements
   * @param aIsChecksSupported boolean - a sign that the implementation supports the checked state of elements
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected M5TreeViewer( ITsGuiContext aContext, IM5Model<T> aModel, INotifierListEdit<T> aItems,
      boolean aIsChecksSupported ) {
    super( aContext, aModel, aItems, aIsChecksSupported );
    DEFAULT_TREE_MAKER = new M5DefaultTreeMaker<>( aModel.entityClass() );
    treeMaker = DEFAULT_TREE_MAKER;
    rootNode = new DefaultTsNode<>( ROOT_KIND, this, aContext );
    columnManager = new ColumnManager( this );
    contentProvider = new TsTreeContentProvider();
    labelProvider = new InternalTreeLabelProvider();
    sortManager = new InternalTreeSortManager();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void internalInitViewers( Composite aParent ) {
    if( !checks.isChecksSupported() ) {
      treeViewer = new TreeViewer( aParent, DEFAULT_TREE_STYLE );
      checkboxTreeViewer = null;
      return;
    }
    Tree tree = new Tree( aParent, SWT.CHECK | DEFAULT_TREE_STYLE );
    checkboxTreeViewer = new CheckboxTreeViewer( tree );
    treeViewer = checkboxTreeViewer;
    checkboxTreeViewer.addCheckStateListener( new SwtCheckStateListener() );
    checkboxTreeViewer.setCheckStateProvider( new ICheckStateProvider() {

      @Override
      public boolean isGrayed( Object aElement ) {
        if( aElement instanceof ITsNode node ) {
          IList<T> ll = internalListItemsOfNode( node );
          for( T item : ll ) {
            if( !checks.internalGetCheckState( item ) ) {
              return true;
            }
          }
          return false;
        }
        throw new TsInternalErrorRtException();
      }

      @Override
      public boolean isChecked( Object aElement ) {
        if( aElement instanceof ITsNode node ) {
          boolean check = false;
          for( ITsNode n : node.listAllTsNodesBelow( true ) ) {
            if( checks.internalGetCheckState( n.entity() ) ) {
              check = true;
              break;
            }
          }
          return check;
        }
        return false;
      }
    } );
  }

  /**
   * Возвращает элемент (сущность) из узла, если таковой там есть.
   *
   * @param aNode {@link ITsNode} - узел дерева, может быть null
   * @return &lt;T&gt; - моделированная сущность из узла или null
   */
  @SuppressWarnings( "unchecked" )
  T itemFromNode( ITsNode aNode ) {
    if( aNode != null ) {
      if( treeMaker.isItemNode( aNode ) ) {
        return (T)aNode.entity();
      }
    }
    return null;
  }

  /**
   * Находит узел по элементу (сущности).
   *
   * @param aItem &lt;T&gt; - искомый элемент, может быть null
   * @return {@link ITsNode} - найденнй узел или null
   */
  ITsNode nodeFromItem( T aItem ) {
    if( aItem != null ) {
      return rootNode.findByEntity( aItem, true );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IM5TreeViewer
  //

  @Override
  public ITsTreeMaker<T> treeMaker() {
    return treeMaker;
  }

  @Override
  public void setTreeMaker( ITsTreeMaker<T> aTreeMaker ) {
    ITsTreeMaker<T> tm = aTreeMaker;
    if( tm == null ) {
      tm = DEFAULT_TREE_MAKER;
    }
    if( Objects.equals( treeMaker, tm ) ) {
      return;
    }
    T sel = null;
    if( getControl() != null ) {
      sel = selectedItem();
    }
    treeMaker = tm;
    doRefreshAll();
    if( getControl() != null ) {
      setSelectedItem( sel );
      if( !columnManager().columns().isEmpty() ) {
        columnManager().columns().values().get( 0 ).pack();
      }
    }
  }

  @Override
  public ITsNode rootNode() {
    return rootNode;
  }

  @Override
  public ITsTreeViewerConsole console() {
    checkStateValidity();
    return console;
  }

  @Override
  public void setTreePaintHelper( ViewerPaintHelper<Tree> aHelper ) {
    checkStateValidity();
    if( aHelper == null ) {
      if( paintHelper != null ) {
        paintHelper.deinstall();
        paintHelper = null;
        treeViewer.refresh();
      }
      return;
    }
    aHelper.install( treeViewer.getTree() );
    paintHelper = aHelper;
    treeViewer.refresh();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T selectedItem() {
    if( !isStateValid() ) {
      return null;
    }
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    ITsNode selNode = (ITsNode)ss.getFirstElement();
    if( selNode == null ) {
      return null;
    }
    if( treeMaker.isItemNode( selNode ) ) {
      return (T)selNode.entity();
    }
    return null;
  }

  @Override
  public void setSelectedItem( T aItem ) {
    if( isStateValid() ) {
      IStructuredSelection selection = StructuredSelection.EMPTY;
      if( aItem != null ) {
        ITsNode node = nodeFromItem( aItem );
        if( node != null ) {
          selection = new StructuredSelection( node );
        }
      }
      treeViewer.setSelection( selection, true );
    }
  }

  @Override
  public void reveal( T aItem ) {
    checkStateValidity();
    TsNullArgumentRtException.checkNull( aItem );
    ITsNode toSel = nodeFromItem( aItem );
    if( toSel != null ) {
      treeViewer.reveal( toSel );
    }
  }

  @Override
  public T getTopItem() {
    checkStateValidity();
    return itemFromNode( (ITsNode)treeViewer.getTree().getTopItem() );
  }

  @Override
  public int getVisibleRowsCount() {
    checkStateValidity();
    Tree tree = treeViewer.getTree();
    Rectangle rect = tree.getClientArea();
    int itemHeight = tree.getItemHeight();
    int headerHeight = tree.getHeaderHeight();
    return (rect.height - headerHeight + itemHeight - 1) / itemHeight;
  }

  // ------------------------------------------------------------------------------------
  // M5AbstractCollectionViewer
  //

  @Override
  protected ColumnViewer doCreateJfaceViewer( Composite aParent ) {
    internalInitViewers( aParent );
    Tree tree = treeViewer.getTree();
    tree.setHeaderVisible( isColumnHeaderVisible() );
    tree.setLinesVisible( true );
    tree.addMenuDetectListener( menuManager );
    // dima 24.10.25 auto tune column width
    tree.addListener( SWT.Expand, aEvent -> Display.getDefault().asyncExec( () -> {
      // должно вызываться после окончания расхлопывания любого узла дерева
      if( !columnManager().columns().isEmpty() ) {
        for( int i = 0; i < columnManager().columns().size() - 1; i++ ) {
          IM5Column<T> col = columnManager().columns().values().get( i );
          col.pack();
        }
      }
    } ) );

    console = new TsTreeViewerConsole( treeViewer );
    return treeViewer;

  }

  @Override
  protected void doRefreshItem( T aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    ITsNode node = rootNode().findByEntity( aItem, false );
    // for check states correct visualization all nodes up to root must be updated
    while( node != null ) {
      treeViewer.update( node, null );
      node = node.parent();
    }
  }

  @Override
  protected void doRefreshAll() {
    IList<ITsNode> nn = treeMaker.makeRoots( rootNode, filterManager().items() );
    rootNode.setNodes( nn );
    if( treeViewer != null ) {
      treeViewer.setInput( rootNode );
    }
  }

  @Override
  protected void doSortInJfaceViewer( IM5Column<T> aColumnOrNull, int aSwtSortOrder ) {
    Column col = (Column)aColumnOrNull;
    TreeColumn tcol = null;
    if( col != null ) {
      tcol = col.tvColumn.getColumn();
    }
    treeViewer.getTree().setSortColumn( tcol );
    treeViewer.getTree().setSortDirection( aSwtSortOrder );
    treeViewer.refresh( true );
  }

  @Override
  int[] doGetJfaceViewerColumnOrder() {
    return treeViewer.getTree().getColumnOrder();
  }

  @Override
  Rectangle doGetJfaceViewerClientArea() {
    return treeViewer.getTree().getClientArea();
  }

  @Override
  int doGetJfaceViewerHeaderHeight() {
    return treeViewer.getTree().getHeaderHeight();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  T doGetJfaceViewerItemAt( Point aCoors ) {
    ITsNode node = doGetJfaceViewerNodeAt( aCoors );
    if( node != null && treeMaker.isItemNode( node ) ) {
      return (T)node.entity();
    }
    return null;
  }

  @Override
  ITsNode doGetJfaceViewerNodeAt( Point aCoors ) {
    TreeItem item = treeViewer.getTree().getItem( aCoors );
    if( item != null ) {
      return (ITsNode)item.getData();
    }
    return null;
  }

  @Override
  void doSetJFaceColumnHeaderVisible( boolean aVisible ) {
    treeViewer.getTree().setHeaderVisible( aVisible );
  }

}
