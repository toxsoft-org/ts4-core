package org.toxsoft.core.tsgui.bricks.tstree.impl;

import static org.toxsoft.core.tsgui.bricks.tstree.impl.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.graphics.EHorAlignment;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.core.tsgui.utils.jface.ViewerPaintHelper;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.coll.primtypes.IIntListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.IntArrayList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link ITsTreeViewer}.
 *
 * @author goga
 */
public class TsTreeViewer
    implements ITsTreeViewer {

  /**
   * ID of option {@link #OPDEF_IS_HEADER_SHOWN}.
   */
  public static final String OPID_IS_HEADER_SHOWN = "TsTreeViewer.isHeaderShown"; //$NON-NLS-1$

  /**
   * Параметр контекста: признак показа заголовка таблицы с именами столбцов.
   */
  public static final IDataDef OPDEF_IS_HEADER_SHOWN = DataDef.create( OPID_IS_HEADER_SHOWN, EAtomicType.BOOLEAN, //
      TSID_NAME, STR_N_IS_HEADER_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_HEADER_SHOWN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Столбец дерева.
   *
   * @author goga
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

    // @Override
    // IStringList doGetCellTexts( int aRowsCount ) {
    // int rowsCount = aRowsCount;
    // if( aRowsCount <= 0 || aRowsCount > MAX_RC ) {
    // rowsCount = MAX_RC;
    // }
    // rowsCount = Math.min( rowsCount, treeViewer.getTree().getItemCount() );
    // if( rowsCount == 0 ) {
    // return IStringList.EMPTY;
    // }
    // IStringListEdit texts = new StringArrayList( rowsCount );
    // for( int i = 0; i < rowsCount; i++ ) {
    // TreeItem treeItem = treeViewer.getTree().getItem( i );
    // texts.add( treeItem.getText( columnIndex ) );
    // }
    // return texts;
    // }

    TreeViewerColumn getTreeViewerColumn() {
      return tvColumn;
    }

    ITsVisualsProvider<ITsNode> nameProvider() {
      return nameProvider;
    }

    @Override
    int getFirstColumnWidthAddition() {
      if( columnIndex == 0 ) {
        return 20; // TODO как вычислить эту ширину?
      }
      return 0;
    }

    @Override
    void doJfacePack() {
      tvColumn.getColumn().pack();
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
  final TsKeyDownEventHelper                  keyDownEventHelper;
  final InternalLabelProvider                 labelProvider   = new InternalLabelProvider();
  private final ITsGuiContext                 context;
  private final TsTreeContentProvider         contentProvider = new TsTreeContentProvider();
  private final IListEdit<ITsNode>            rootNodes       = new ElemLinkedBundleList<>();
  private final IListEdit<ITsViewerColumn>    columns         = new ElemLinkedBundleList<>();
  private ViewerPaintHelper<Tree>             paintHelper     = null;

  TreeViewer                   treeViewer = null;
  private ITsTreeViewerConsole console    = null;

  /**
   * Конструктор дерева с указанием контекста.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsTreeViewer( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    context = aContext;
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
    keyDownEventHelper = new TsKeyDownEventHelper( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILazyControl
  //

  @Override
  public Control createControl( Composite aParent ) {
    treeViewer = doCreateTreeViewer( aParent );
    console = new TsTreeViewerConsole( treeViewer );
    treeViewer.setContentProvider( contentProvider );
    treeViewer.setLabelProvider( labelProvider );
    treeViewer.addSelectionChangedListener( treeSelectionChangedListener );
    treeViewer.addDoubleClickListener( treeDoubleClickListener );
    boolean isHeaderVisible = OPDEF_IS_HEADER_SHOWN.getValue( context.params() ).asBool();
    treeViewer.getTree().setHeaderVisible( isHeaderVisible );
    treeViewer.getTree().setLinesVisible( true );
    treeViewer.setInput( this );
    keyDownEventHelper.bindToControl( treeViewer.getControl() );
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
  // Методы для переопределения наследниками
  //

  protected TreeViewer doCreateTreeViewer( Composite aParent ) {
    return new TreeViewer( aParent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionProvider
  //

  @Override
  public ITsNode selectedItem() {
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    return (ITsNode)ss.getFirstElement();
  }

  @Override
  public void setSelectedItem( ITsNode aItem ) {
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aItem != null ) {
      selection = new StructuredSelection( aItem );
    }
    treeViewer.setSelection( selection, true );
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
  // Реализация интерфейса ITsDoubleClickEventProducer
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
  // Реализация интерфейса ITsKeyEventProducer
  //

  @Override
  public void addTsKeyDownListener( ITsKeyEventListener aListener ) {
    keyDownEventHelper.addTsKeyDownListener( aListener );
  }

  @Override
  public void removeTsKeyDownListener( ITsKeyEventListener aListener ) {
    keyDownEventHelper.removeTsKeyDownListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return context.params();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsNode
  //

  @Override
  public ITsGuiContext context() {
    return context;
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

  // GOGA157
  // @Override
  // public Image image() {
  // return null;
  // }

  @Override
  public Image getImage( EIconSize aIconSize ) {
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
  // Реализация интерфейса ITsTreeViewer
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
    // добавление столбца меняет состояние показа заголовка - восстановим
    boolean isHeaderVisible = OPDEF_IS_HEADER_SHOWN.getValue( context.params() ).asBool();
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

  /**
   * Задает поставщика шрифта для ячеек таблицы или null, если используются только шрифты по умолчанию.
   * <p>
   * Внимание: после этого метода следует обновить отрисовщики ячеек вызовом
   * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
   *
   * @param aFontProvider {@link ITableFontProvider} - поставщик шрифтов ячеек или null
   */
  @Override
  public void setFontProvider( ITableFontProvider aFontProvider ) {
    labelProvider.setFontProvider( aFontProvider );
    treeViewer.setLabelProvider( labelProvider );
  }

  /**
   * Задает поставщика цветов для ячеек таблицы или null, если используются только цвета по умолчанию.
   * <p>
   * Внимание: после этого метода следует обновить отрисовщики ячеек вызовом
   * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
   *
   * @param aColorProvider {@link ITableColorProvider} - поставщик цветов ячеек или null
   */
  @Override
  public void setColorProvider( ITableColorProvider aColorProvider ) {
    labelProvider.setColorProvider( aColorProvider );
    treeViewer.setLabelProvider( labelProvider );
  }

  @Override
  public EIconSize iconSize() {
    return labelProvider.iconSize();
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    labelProvider.setIconSize( aIconSize );
  }

  // ------------------------------------------------------------------------------------
  // Дополнительное API класса
  //

  /**
   * Возвращает реализацию -JFace-просмотрщик.
   *
   * @return {@link TreeViewer} - JFace-просмотрщик или null
   */
  public TreeViewer getJfaceTreeViewer() {
    return treeViewer;
  }

}
