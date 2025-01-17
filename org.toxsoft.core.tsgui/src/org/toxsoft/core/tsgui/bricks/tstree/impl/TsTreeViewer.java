package org.toxsoft.core.tsgui.bricks.tstree.impl;

import static org.toxsoft.core.tsgui.bricks.tstree.impl.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsTreeViewer} implementation.
 *
 * @author hazard157
 */
public class TsTreeViewer
    implements ITsTreeViewer, ITsGuiContextable {

  /**
   * ID of option {@link #OPDEF_IS_HEADER_SHOWN}.
   */
  public static final String OPID_IS_HEADER_SHOWN = "TsTreeViewer.isHeaderShown"; //$NON-NLS-1$

  /**
   * Creation option: The flag to display the table header with column names.
   */
  public static final IDataDef OPDEF_IS_HEADER_SHOWN = DataDef.create( OPID_IS_HEADER_SHOWN, EAtomicType.BOOLEAN, //
      TSID_NAME, STR_IS_HEADER_SHOWN, //
      TSID_DESCRIPTION, STR_IS_HEADER_SHOWN_D, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Tree column.
   *
   * @author hazard157
   */
  class Column
      extends AbstractTsTreeColumn {

    // private static final int MAX_RC = 100;
    private final TreeViewerColumn            tvColumn;
    private final int                         columnIndex;
    private final ITsVisualsProvider<ITsNode> nameProvider;

    Column( TreeViewerColumn aTvColumn, int aColumnIndex, ITsVisualsProvider<ITsNode> aNameProvider ) {
      super( aTvColumn.getColumn() );
      columnIndex = aColumnIndex;
      tvColumn = aTvColumn;
      nameProvider = aNameProvider;
    }

    TreeViewerColumn getTreeViewerColumn() {
      return tvColumn;
    }

    ITsVisualsProvider<ITsNode> nameProvider() {
      return nameProvider;
    }

    @Override
    int getFirstColumnWidthAddition() {
      if( columnIndex == 0 ) {
        return 20; // TODO how to calculate this width?
      }
      return 0;
    }

    @Override
    void doJfacePack() {
      tvColumn.getColumn().pack();
    }

    @Override
    public void setValedEditingSupport( ITsNodeValedProvider aValedProvider ) {
      TsNullArgumentRtException.checkNull( aValedProvider );
      tvColumn.setEditingSupport( new TsNodeEditingSupport( treeViewer, tsContext, aValedProvider ) );
    }

  }

  class InternalLabelProvider
      extends TsTreeLabelProvider {

    @Override
    protected Image doGetColumnImage( ITsNode aNode, int aColumnIndex, EIconSize aIconSize ) {
      if( columns().isEmpty() ) {
        return null;
      }
      ITsViewerColumn c = columns().get( aColumnIndex );
      if( c.isUseThumb() ) {
        TsImage thumb = ((Column)c).nameProvider().getThumb( aNode, labelProvider.thumbSize() );
        if( thumb != null ) {
          return thumb.image();
        }
        return null;
      }
      return ((Column)c).nameProvider().getIcon( aNode, labelProvider.iconSize() );
    }

    @Override
    protected String doGetColumnText( ITsNode aNode, int aColumnIndex ) {
      if( columns().isEmpty() ) {
        return null;
      }
      ITsViewerColumn c = columns().get( aColumnIndex );
      return ((Column)c).nameProvider().getName( aNode );
    }

  }

  private final ISelectionChangedListener treeSelectionChangedListener = new ISelectionChangedListener() {

    @Override
    public void selectionChanged( SelectionChangedEvent aEvent ) {
      selectionChangeEventHelper.fireTsSelectionEvent( selectedItem() );
    }
  };

  private final IDoubleClickListener treeDoubleClickListener = new IDoubleClickListener() {

    @Override
    public void doubleClick( DoubleClickEvent aEvent ) {
      doubleClickEventHelper.fireTsDoublcClickEvent( selectedItem() );
    }
  };

  final TsSelectionChangeEventHelper<ITsNode> selectionChangeEventHelper;
  final TsDoubleClickEventHelper<ITsNode>     doubleClickEventHelper;
  final TsUserInputEventsBinder               keyInputEventsBinder;
  final InternalLabelProvider                 labelProvider = new InternalLabelProvider();

  private final GenericChangeEventer iconSizeEventer;
  private final GenericChangeEventer thumbSizeEventer;

  private final ITsGuiContext              tsContext;
  private final TsTreeContentProvider      contentProvider = new TsTreeContentProvider();
  private final IListEdit<ITsNode>         rootNodes       = new ElemLinkedBundleList<>();
  private final IListEdit<ITsViewerColumn> columns         = new ElemLinkedBundleList<>();
  private ViewerPaintHelper<Tree>          paintHelper     = null;

  TreeViewer                   treeViewer = null;
  private ITsTreeViewerConsole console    = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsTreeViewer( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
    keyInputEventsBinder = new TsUserInputEventsBinder( this );
    iconSizeEventer = new GenericChangeEventer( this );
    thumbSizeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  public Control createControl( Composite aParent ) {
    treeViewer = doCreateTreeViewer( aParent );
    console = new TsTreeViewerConsole( treeViewer );
    treeViewer.setContentProvider( contentProvider );
    treeViewer.setLabelProvider( labelProvider );
    treeViewer.addSelectionChangedListener( treeSelectionChangedListener );
    treeViewer.addDoubleClickListener( treeDoubleClickListener );
    boolean isHeaderVisible = OPDEF_IS_HEADER_SHOWN.getValue( params() ).asBool();
    treeViewer.getTree().setHeaderVisible( isHeaderVisible );
    treeViewer.getTree().setLinesVisible( true );
    treeViewer.setInput( this );
    keyInputEventsBinder.bindToControl( treeViewer.getControl(), TsUserInputEventsBinder.BIND_KEY_DOWN_UP );
    return treeViewer.getControl();
  }

  @Override
  public Control getControl() {
    if( treeViewer == null ) {
      return null;
    }
    return treeViewer.getControl();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected TreeViewer doCreateTreeViewer( Composite aParent ) {
    return new TreeViewer( aParent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public ITsNode selectedItem() {
    if( getControl() == null ) {
      return null;
    }
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    return (ITsNode)ss.getFirstElement();
  }

  @Override
  public void setSelectedItem( ITsNode aItem ) {
    if( getControl() != null ) {
      IStructuredSelection selection = StructuredSelection.EMPTY;
      if( aItem != null ) {
        selection = new StructuredSelection( aItem );
      }
      treeViewer.setSelection( selection, true );
    }
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<ITsNode> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<ITsNode> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<ITsNode> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<ITsNode> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyEventProducer
  //

  @Override
  public void addTsKeyInputListener( ITsKeyInputListener aListener ) {
    keyInputEventsBinder.addTsKeyInputListener( aListener );
  }

  @Override
  public void removeTsKeyInputListener( ITsKeyInputListener aListener ) {
    keyInputEventsBinder.removeTsKeyInputListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return tsContext.params();
  }

  // ------------------------------------------------------------------------------------
  // ITsNode
  //

  @Override
  public ITsGuiContext context() {
    return tsContext;
  }

  @Override
  public ITsTreeViewer root() {
    return this;
  }

  @Override
  public ITsNodeKind<?> kind() {
    return TREE_KIND;
  }

  @Override
  public String name() {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public Image getIcon( EIconSize aIconSize ) {
    return null;
  }

  @Override
  public Object entity() {
    return null;
  }

  @Override
  public String iconId() {
    return null;
  }

  @Override
  public ITsNode parent() {
    return null;
  }

  @Override
  public IList<ITsNode> childs() {
    return rootNodes;
  }

  @Override
  public IList<ITsNode> listExistingChilds() {
    return rootNodes;
  }

  @Override
  public void rebuildSubtree( boolean aRebuild, boolean aQuerySubtree ) {
    for( ITsNode root : rootNodes ) {
      root.rebuildSubtree( aRebuild, aQuerySubtree );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsTreeViewer
  //

  @Override
  public IList<ITsViewerColumn> columns() {
    return columns;
  }

  @Override
  public ITsViewerColumn addColumn( String aTitle, EHorAlignment aAlignment,
      ITsVisualsProvider<ITsNode> aNameProvider ) {
    TsNullArgumentRtException.checkNulls( aTitle, aAlignment, aNameProvider );
    int colIndex = columns.size();
    TreeViewerColumn tvCol = new TreeViewerColumn( treeViewer, aAlignment.swtStyle(), colIndex );
    treeViewer.setLabelProvider( labelProvider ); // refresh column cells label provider
    ITsViewerColumn col = new Column( tvCol, colIndex, aNameProvider );
    columns.add( col );
    if( !treeViewer.getTree().getHeaderVisible() ) {
      treeViewer.getTree().setHeaderVisible( true );
    }
    tvCol.getColumn().setText( aTitle );
    tvCol.getColumn().setAlignment( aAlignment.swtStyle() );
    tvCol.getColumn().setWidth( 50 );
    // adding a column changes the header display state - restore
    boolean isHeaderVisible = OPDEF_IS_HEADER_SHOWN.getValue( params() ).asBool();
    treeViewer.getTree().setHeaderVisible( isHeaderVisible );
    return col;
  }

  @Override
  public void removeColumns() {
    while( !columns.isEmpty() ) {
      Column c = (Column)columns.removeByIndex( 0 );
      c.getTreeViewerColumn().getColumn().dispose();
    }
  }

  @Override
  public void setRootNodes( ITsCollection<ITsNode> aRootNodes ) {
    if( aRootNodes != null ) {
      for( ITsNode node : rootNodes ) {
        TsIllegalArgumentRtException.checkTrue( node.parent() != this );
      }
      rootNodes.setAll( aRootNodes );
      rebuildSubtree( false, false );
    }
    else {
      rootNodes.clear();
    }
    if( treeViewer != null ) {
      treeViewer.refresh();
    }
  }

  @Override
  public void clear() {
    setRootNodes( IList.EMPTY );
  }

  @Override
  public ITsNode findByEntity( Object aEntity, boolean aQuerySubtree ) {
    for( ITsNode root : rootNodes ) {
      ITsNode node = root.findByEntity( aEntity, aQuerySubtree );
      if( node != null ) {
        return node;
      }
    }
    return null;
  }

  @Override
  public ITsNode findChildByEntity( Object aEntity, boolean aQueryChildren ) {
    for( ITsNode root : rootNodes ) {
      if( Objects.equals( root.entity(), aEntity ) ) {
        return root;
      }
    }
    return null;
  }

  @Override
  public ITsTreeViewerConsole console() {
    TsIllegalStateRtException.checkNull( console );
    return console;
  }

  @Override
  public IIntList getSelectedPath() {
    ITsNode sel = selectedItem();
    if( sel == null ) {
      return IIntList.EMPTY;
    }
    ITsNode parent = sel.parent();
    IIntListEdit result = new IntArrayList();
    while( parent != null ) {
      int index = parent.childs().indexOf( sel );
      result.insert( 0, index );
      sel = parent;
      parent = sel.parent();
    }
    return result;
  }

  @Override
  public void setSelectedPath( IIntList aPathIndexes ) {
    TsNullArgumentRtException.checkNull( aPathIndexes );
    if( aPathIndexes.isEmpty() ) {
      setSelectedItem( null );
      return;
    }
    int index = aPathIndexes.getValue( 0 );
    if( index < 0 || index >= rootNodes.size() ) {
      setSelectedItem( null );
      return;
    }
    ITsNode sel = rootNodes.get( index );

    for( int i = 1, count = aPathIndexes.size(); i < count; i++ ) {
      IList<ITsNode> list = sel.childs();
      index = aPathIndexes.getValue( i );
      if( index < 0 || index >= list.size() ) {
        break;
      }
      sel = list.get( index );
    }
    setSelectedItem( sel );
  }

  @Override
  public void setTreePaintHelper( ViewerPaintHelper<Tree> aHelper ) {
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

  @Override
  public void setFontProvider( ITableFontProvider aFontProvider ) {
    labelProvider.setFontProvider( aFontProvider );
    treeViewer.setLabelProvider( labelProvider );
  }

  @Override
  public void setColorProvider( ITableColorProvider aColorProvider ) {
    labelProvider.setColorProvider( aColorProvider );
    treeViewer.setLabelProvider( labelProvider );
  }

  // ------------------------------------------------------------------------------------
  // IIconSizeableEx
  //

  @Override
  public EIconSize iconSize() {
    return labelProvider.iconSize();
  }

  @Override
  public EIconSize defaultIconSize() {
    return labelProvider.defaultIconSize();
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    if( iconSize() != aIconSize ) {
      labelProvider.setIconSize( aIconSize );
      iconSizeEventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer iconSizeChangeEventer() {
    return iconSizeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return labelProvider.thumbSize();
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return labelProvider.defaultThumbSize();
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize() != aThumbSize ) {
      labelProvider.setThumbSize( aThumbSize );
      thumbSizeEventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return thumbSizeEventer;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Returns the implementation - JFace-viewer.
   *
   * @return {@link TreeViewer} - JFace-viewer or <code>null</code> before {@link #createControl(Composite)} call
   */
  public TreeViewer getJfaceTreeViewer() {
    return treeViewer;
  }

}
