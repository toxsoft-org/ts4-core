package org.toxsoft.core.tsgui.m5.gui.viewers.impl;

import java.util.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5CollectionViewer} base implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public abstract class M5AbstractCollectionViewer<T>
    implements IM5CollectionViewer<T> {

  // TODO add local popup menu support

  /**
   * SWT check state change listener.
   * <p>
   * When user changes check state of visible items fires check state change event to clients via
   * {@link ITsCheckSupport#checksChangeEventer()}.
   * <p>
   * Instances are created by subclasses of {@link M5AbstractCollectionViewer}.
   *
   * @author hazard157
   */
  class SwtCheckStateListener
      implements ICheckStateListener {

    SwtCheckStateListener() {
      // nop
    }

    @Override
    public void checkStateChanged( CheckStateChangedEvent aEvent ) {
      ITsNode node = ((ITsNode)aEvent.getElement());
      boolean check = aEvent.getChecked();
      IList<T> changedItems = internalListItemsOfNode( node );
      // TODO validate if check state can be set to these elements
      // change check state for subtree starting from node
      for( T item : changedItems ) {
        checks.silentlySetCheckState( item, check );
      }
      checks.fireChangeEvent();
      columnViewer.refresh( node.root() );
    }

  }

  /**
   * {@link IM5FilterManager} implementation.
   *
   * @author hazard157
   */
  class InternalFilterManager
      implements IM5FilterManager<T> {

    private final IListEdit<T> filteredItems = new ElemLinkedBundleList<>();
    private ITsFilter<T>       tsFilter      = ITsFilter.ALL;

    InternalFilterManager() {
      filteredItems.setAll( M5AbstractCollectionViewer.this.items() );
    }

    @Override
    public boolean isFiltered() {
      return tsFilter != ITsFilter.ALL;
    }

    @Override
    public IList<T> items() {
      return filteredItems;
    }

    @Override
    public void setFilter( ITsFilter<T> aFilter ) {
      if( !Objects.equals( tsFilter, aFilter ) ) {
        tsFilter = TsNullArgumentRtException.checkNull( aFilter );
        refreshFilteredItems();
      }
    }

    @Override
    public ITsFilter<T> getFilter() {
      return tsFilter;
    }

    // API
    void refreshFilteredItems() {
      if( isFiltered() ) {
        filteredItems.clear();
        for( T item : M5AbstractCollectionViewer.this.items() ) {
          if( tsFilter.accept( item ) ) {
            filteredItems.add( item );
          }
        }
      }
      else {
        filteredItems.setAll( M5AbstractCollectionViewer.this.items() );
      }
      doRefreshAll();
    }

  }

  /**
   * Basic implementatio of sorting manager.
   *
   * @author hazard157
   */
  abstract class InternalSortManager
      implements IM5SortManager {

    /**
     * SWT {@link ViewerComparator} implementation.
     *
     * @author hazard157
     */
    class InternalViewerComparator
        extends ViewerComparator {

      @Override
      public int compare( Viewer viewer, Object e1, Object e2 ) {
        if( sortOrder == ESortOrder.NONE ) {
          return 0;
        }
        int result = doCompare( e1, e2 );
        if( sortOrder() == ESortOrder.DESCENDING ) {
          result = -result;
        }
        return result;
      }
    }

    final Comparator<T> objectComparator;

    /**
     * Field ID user for sorting or <code>null</code> for {@link ESortOrder#NONE}.
     */
    IM5FieldDef<T, Object> sortFieldDef = null;

    /**
     * Current sort order.
     */
    ESortOrder sortOrder = ESortOrder.NONE;

    InternalSortManager() {
      objectComparator = ( aO1, aO2 ) -> {
        if( sortFieldDef == null || sortFieldDef.comparator() == null ) {
          return 0;
        }
        Object v1 = sortFieldDef.getFieldValue( aO1 );
        Object v2 = sortFieldDef.getFieldValue( aO2 );
        return sortFieldDef.comparator().compare( v1, v2 );
      };
    }

    // ------------------------------------------------------------------------------------
    // To implement
    //

    protected abstract int doCompare( Object aO1, Object aO2 );

    // ------------------------------------------------------------------------------------
    // IM5SortManager
    //

    @Override
    public ESortOrder sortOrder() {
      return sortOrder;
    }

    @Override
    public String sortFieldId() {
      TsIllegalStateRtException.checkTrue( sortOrder == ESortOrder.NONE );
      TsInternalErrorRtException.checkNull( sortFieldDef );
      return sortFieldDef.id();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void sort( ESortOrder aOrder, String aFieldId ) {
      checkStateValidity();
      TsNullArgumentRtException.checkNull( aOrder );
      sortOrder = aOrder;
      sortFieldDef = null;
      if( aFieldId != null ) {
        sortFieldDef = (IM5FieldDef<T, Object>)model().fieldDefs().findByKey( aFieldId );
      }
      IM5Column<T> col = null;
      col = columnManager().columns().getByKey( aFieldId );
      int swtSortStyle = (sortOrder == ESortOrder.DESCENDING) ? SWT.DOWN : SWT.UP;
      doSortInJfaceViewer( col, swtSortStyle );
    }

    @Override
    public void unsort() {
      checkStateValidity();
      doSortInJfaceViewer( null, SWT.NONE );
    }

  }

  /**
   * {@link IM5MenuManager} implementation.
   *
   * @author hazard157
   */
  class InternalMenuManager
      implements IM5MenuManager<T>, MenuDetectListener {

    /**
     * Local menu shown by right mouse click in column header area.
     */
    private Menu headerMenu = null;

    /**
     * Local menu shown by right mouse click in table/tree content area.
     */
    private Menu cellMenu = null;

    /**
     * Client handler that creates local menus.
     */
    private IM5MenuDetectHandler<T> menuDetectListener = IM5MenuDetectHandler.NULL;

    InternalMenuManager() {
      // nop
    }

    // ------------------------------------------------------------------------------------
    // MenuDetectListener
    //

    // TODO TRANSLATE

    @Override
    public void menuDetected( MenuDetectEvent e ) {
      // FIXME определиться, надо ли делать dispose() старым меню
      Point p = screenToControl( e.x, e.y );
      // определить индекс столбца в columns
      IM5Column<T> col = columnAtPoint( p );
      // вызовем меню заголовка или меню ячейки
      int headerHeight = doGetJfaceViewerHeaderHeight();
      if( headerHeight > 0 && p.y < headerHeight ) { // меню на заголовке таблицы
        e.doit = menuDetectListener.onHeaderMenu( M5AbstractCollectionViewer.this, e, col );
        columnViewer.getControl().setMenu( headerMenu );
        return;
      }
      /**
       * Warning:
       * <p>
       * Внимание: разные события мыши генерируют координаты события относительно разных частей таблицы. В частности,
       * правая кнопка мыши (при определении события локального меню) считает координаты от всего контроля (виджета ОС),
       * а например двойной щелчок возвращает координаты относительно рабочей области (т.е. без заголовка) таблицы.
       * Учетем это отличие точкой p2.
       */
      Point p2 = new Point( p.x, p.y - doGetJfaceViewerHeaderHeight() );
      // меню в области ячеек таблицы
      T item = doGetJfaceViewerItemAt( p2 );
      ITsNode node = doGetJfaceViewerNodeAt( p2 );
      e.doit = menuDetectListener.onCellMenu( M5AbstractCollectionViewer.this, e, col, item, node );
      columnViewer.getControl().setMenu( cellMenu );
    }

    // ------------------------------------------------------------------------------------
    // IM5MenuManager
    //

    @Override
    public Menu getHeaderMenu() {
      return headerMenu;
    }

    @Override
    public void setHeaderMenu( Menu aMenu ) {
      headerMenu = aMenu;
    }

    @Override
    public Menu getCellMenu() {
      return cellMenu;
    }

    @Override
    public void setCellMenu( Menu aMenu ) {
      cellMenu = aMenu;
    }

    @Override
    public void setMenuDetectListener( IM5MenuDetectHandler<T> aListener ) {
      TsNullArgumentRtException.checkNull( aListener );
      menuDetectListener = aListener;
    }

  }

  /**
   * SWT {@link ITableLabelProvider} label provider base both for tree and table.
   * <p>
   * Also implements SWT cell font and color provider.
   *
   * @author hazard157
   */
  static abstract class InternalAbstractLabelProvider
      extends TableLabelProviderAdapter
      implements ITableFontProvider, ITableColorProvider {

    /**
     * Client-specified font provider or <code>null</code> for no provider.
     */
    ITableFontProvider fontProvider = null;

    /**
     * Client-specified color provider or <code>null</code> for no provider.
     */
    ITableColorProvider colorProvider = null;

    // ------------------------------------------------------------------------------------
    // ITableColorProvider
    //

    @Override
    public Color getForeground( Object aElement, int aColumnIndex ) {
      if( colorProvider != null ) {
        return colorProvider.getForeground( aElement, aColumnIndex );
      }
      return null;
    }

    @Override
    public Color getBackground( Object aElement, int aColumnIndex ) {
      if( colorProvider != null ) {
        return colorProvider.getBackground( aElement, aColumnIndex );
      }
      return null;
    }

    // ------------------------------------------------------------------------------------
    // ITableFontProvider
    //

    @Override
    public Font getFont( Object aElement, int aColumnIndex ) {
      if( fontProvider != null ) {
        return fontProvider.getFont( aElement, aColumnIndex );
      }
      return null;
    }

    // ------------------------------------------------------------------------------------
    // TableLabelProviderAdapter
    //

    @Override
    abstract public String getColumnText( Object aElement, int aColumnIndex );

    @Override
    abstract public Image getColumnImage( Object aElement, int aColumnIndex );

    // ------------------------------------------------------------------------------------
    // API
    //

    /**
     * Sets table/tree cells font provider.
     * <p>
     * Warning: after this method call label provider also must be updated
     * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
     *
     * @param aFontProvider {@link ITableFontProvider} - the font p[rovider or <code>null</code>
     */
    public void setFontProvider( ITableFontProvider aFontProvider ) {
      fontProvider = aFontProvider;
    }

    /**
     * Sets table/tree cells color provider.
     * <p>
     * Warning: after this method call label provider also must be updated
     * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
     *
     * @param aColorProvider {@link ITableColorProvider} - thre color provider or <code>null</code>
     */
    public void setColorProvider( ITableColorProvider aColorProvider ) {
      colorProvider = aColorProvider;
    }

  }

  /**
   * {@link ITsCheckSupport} helper implementation.
   * <p>
   * Notes on implementation: in the SWT table there are items {@link TableItem} holding elements &lt;T&gt;. Such items
   * also are holding the check state. In the SWT tree items {@link TreeItem} exists only from expanded nodes so we can
   * not rely on SWT control on check state strage. For code unification both in table and tree check state is stored
   * here as list {@link M5AbstractCollectionViewer.InternalChecksImplementation#checkedItems} of checked (that is
   * checkstate = <code>true</code>) elements.
   *
   * @author hazard157
   */
  final class InternalChecksImplementation
      implements ITsCheckSupport<T> {

    private final GenericChangeEventer genericChangeEventer =
        new GenericChangeEventer( M5AbstractCollectionViewer.this );

    private final IListEdit<T> checkedItems;

    public InternalChecksImplementation( boolean aIsChecksSupported ) {
      if( aIsChecksSupported ) {
        checkedItems = new ElemLinkedBundleList<>();
      }
      else {
        checkedItems = null;
      }
    }

    /**
     * Sets item check state without notification events.
     * <p>
     * Simply adds or removes <code>aItem</code> to the list of checked ietsm {@link #checkedItems}.
     *
     * @param aItem &lt;T&gt; - the element
     * @param aState boolean - check state
     */
    void silentlySetCheckState( T aItem, boolean aState ) {
      if( aState ) {
        checkedItems.add( aItem );
      }
      else {
        checkedItems.remove( aItem );
      }
    }

    /**
     * Determines if item is in {@link #checkedItems} list.
     * <p>
     * The argument may be of any type, not only &lt;T&gt;.
     *
     * @param aItem Object - the element or <code>null</code>
     * @return boolean - <code>true</code> if argument is not <code>null</code> and is in {@link #checkedItems}
     */
    boolean internalGetCheckState( Object aItem ) {
      if( aItem == null ) {
        return false;
      }
      for( int i = 0, c = checks.checkedItems.size(); i < c; i++ ) {
        T item = checkedItems.get( i );
        if( item.equals( aItem ) ) {
          return true;
        }
      }
      return false;
    }

    // ------------------------------------------------------------------------------------
    // ITsCheckSupport
    //

    @Override
    public boolean isChecksSupported() {
      return checkedItems != null;
    }

    @Override
    public boolean getItemCheckState( T aItem ) {
      TsUnsupportedFeatureRtException.checkFalse( isChecksSupported() );

      return checkedItems.hasElem( aItem );
    }

    @Override
    public IList<T> listCheckedItems( boolean aCheckState ) {
      TsUnsupportedFeatureRtException.checkFalse( isChecksSupported() );
      if( aCheckState ) {
        return checkedItems;
      }
      IListEdit<T> ll = new ElemLinkedBundleList<>();
      for( T item : items() ) {
        if( !items().hasElem( item ) ) {
          ll.add( item );
        }
      }
      return ll;
    }

    @Override
    public IGenericChangeEventer checksChangeEventer() {
      return genericChangeEventer;
    }

    @Override
    public void setItemCheckState( T aItem, boolean aCheckState ) {
      TsUnsupportedFeatureRtException.checkFalse( isChecksSupported() );
      boolean isChecked = checkedItems.hasElem( aItem );
      if( isChecked != aCheckState ) {
        if( aCheckState ) {
          checkedItems.add( aItem );
        }
        else {
          checkedItems.remove( aItem );
        }
        doRefreshItem( aItem );
        genericChangeEventer.fireChangeEvent();
      }
    }

    @Override
    public void setItemsCheckState( IList<T> aItems, boolean aCheckState ) {
      TsNullArgumentRtException.checkNull( aItems );
      TsUnsupportedFeatureRtException.checkFalse( isChecksSupported() );
      boolean needChange = false;
      for( T elem : aItems ) {
        boolean isChecked = checkedItems.hasElem( elem );
        if( isChecked != aCheckState ) {
          needChange = true;
          if( aCheckState ) {
            checkedItems.add( elem );
          }
          else {
            checkedItems.remove( elem );
          }
        }
      }
      if( needChange ) {
        refresh();
        genericChangeEventer.fireChangeEvent();
      }
    }

    @Override
    public void setAllItemsCheckState( boolean aCheckState ) {
      TsUnsupportedFeatureRtException.checkFalse( isChecksSupported() );
      if( aCheckState ) {
        checkedItems.setAll( items() );
        refresh();
      }
      else {
        if( !checkedItems.isEmpty() ) {
          checkedItems.clear();
          refresh();
        }
      }
    }

    void fireChangeEvent() {
      TsUnsupportedFeatureRtException.checkFalse( isChecksSupported() );
      genericChangeEventer.fireChangeEvent();
    }

  }

  /**
   * Selection in viewer changed - translates to the {@link ITsSelectionChangeListener} event.
   */
  private final ISelectionChangedListener viewerSelectionChangedListener = new ISelectionChangedListener() {

    @Override
    public void selectionChanged( SelectionChangedEvent aEvent ) {
      itemSelectionChangeEventHelper.fireTsSelectionEvent( selectedItem() );
    }
  };

  /**
   * Item in viewer double-clicked - translates to the {@link ITsDoubleClickListener} event.
   */
  private final IDoubleClickListener viewerDoubleClickListener = new IDoubleClickListener() {

    @Override
    public void doubleClick( DoubleClickEvent aEvent ) {
      itemDoubleClickEventHelper.fireTsDoublcClickEvent( selectedItem() );
    }
  };

  /**
   * Items list chenged - calls {@link #processDataSourceChange(ECrudOp, Object)}.
   */
  final ITsCollectionChangeListener itemsChangeListener = new ITsCollectionChangeListener() {

    @SuppressWarnings( "unchecked" )
    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      processDataSourceChange( aOp, (T)aItem );
    }
  };

  private static final EThumbSize DEFAULT_THUMB_SIZE = EThumbSize.SZ96;

  protected final GenericChangeEventer            iconSizeEventer;
  protected final GenericChangeEventer            thumbSizeEventer;
  protected final TsDoubleClickEventHelper<T>     itemDoubleClickEventHelper;
  protected final TsSelectionChangeEventHelper<T> itemSelectionChangeEventHelper;
  protected final TsKeyDownEventHelper            keyDownEventHelper;

  private final ITsGuiContext         tsContext;
  private final IM5Model<T>           model;
  private final INotifierListEdit<T>  items;
  private final InternalFilterManager filterManager;
  protected final InternalMenuManager menuManager;
  final InternalChecksImplementation  checks;
  private EIconSize                   iconSize            = EIconSize.IS_16X16;
  private EThumbSize                  thumbSize           = EThumbSize.SZ64;
  private boolean                     columnHeaderVisible = true;

  protected InternalSortManager           sortManager     = null; // инициализируется в конструкторе наследника
  protected IStructuredContentProvider    contentProvider = null; // инициализируется в конструкторе наследника
  protected InternalAbstractLabelProvider labelProvider   = null; // инициализируется в конструкторе наследника
  protected M5AbstractColumnManager<T>    columnManager   = null; // инициализируется в конструкторе наследника

  ColumnViewer columnViewer = null;

  /**
   * Constructor for subclasses.
   * <p>
   * Does nnot creates elements list but remembers argument <code>aItems</code> as {@link #items()}.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aObjModel {@link IM5Model} - M5-model of elements
   * @param aItems {@link INotifierListEdit}&lt;T&gt; - elements list
   * @param aIsChecksSupported boolean - informs that subclass will support check states visualization
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected M5AbstractCollectionViewer( ITsGuiContext aContext, IM5Model<T> aObjModel, INotifierListEdit<T> aItems,
      boolean aIsChecksSupported ) {
    TsNullArgumentRtException.checkNulls( aContext, aObjModel, aItems );
    iconSizeEventer = new GenericChangeEventer( this );
    thumbSizeEventer = new GenericChangeEventer( this );
    itemDoubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
    itemSelectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    // обработку клавиш сделам так, чтобы сначала обрабатывались пользовательские
    keyDownEventHelper = new TsKeyDownEventHelper( this );
    tsContext = aContext;
    model = aObjModel;
    items = aItems;
    items.addCollectionChangeListener( itemsChangeListener );
    filterManager = new InternalFilterManager();
    menuManager = new InternalMenuManager();
    checks = new InternalChecksImplementation( aIsChecksSupported );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  protected void processDataSourceChange( ECrudOp aOp, T aItem ) {
    if( !isStateValid() ) {
      return;
    }
    T selItem1 = selectedItem();
    filterManager.refreshFilteredItems();
    switch( aOp ) {
      case EDIT:
        doRefreshItem( aItem );
        setSelectedItem( aItem );
        break;
      case CREATE:
      case REMOVE:
      case LIST:
        doRefreshAll();
        // try to hold selection
        setSelectedItem( selItem1 );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    // if selection was not hold fire selection change event
    T selItem2 = selectedItem();
    if( selItem2 != selItem1 ) {
      itemSelectionChangeEventHelper.fireTsSelectionEvent( selItem2 );
    }
  }

  /**
   * Возвращает столбец, на которой находится точка с координатами относительно клиентской области таблицы.
   *
   * @param aP {@link Point} - точка с координатами относительно клиентской области таблицы.
   * @return - колонка или null, если точка вне колонок таблицы
   */
  IM5Column<T> columnAtPoint( Point aP ) {
    int[] columnOrder = doGetJfaceViewerColumnOrder();
    int currW = 0;
    for( int i = 0; i < columnOrder.length; i++ ) {
      IM5Column<T> col = columnManager().columns().values().get( columnOrder[i] );
      currW += col.width();
      if( aP.x <= currW ) {
        return col;
      }
    }
    return null;
  }

  /**
   * Возвращает пересчет экранных координат в координаты внутри клиентской области таблицы.
   *
   * @param aScreenX int - экранная X коорлината
   * @param aScreenY int - экранная Y коорлината
   * @return {@link Point} - координаты относительно клиентской области таблицы
   */
  Point screenToControl( int aScreenX, int aScreenY ) {
    Rectangle rect = doGetJfaceViewerClientArea();
    return columnViewer.getControl().toControl( aScreenX - rect.x, aScreenY - rect.y );
  }

  /**
   * Возвращает список элементов из {@link #items()}, которые находятся в пддереве узла (включая сам этот узел).
   *
   * @param aNode {@link ITsNode} - корень поддерева
   * @return {@link IList}&lt;T&gt; - элементы в поддереве
   */
  IList<T> internalListItemsOfNode( ITsNode aNode ) {
    IListEdit<T> list = new ElemArrayList<>();
    for( ITsNode n : aNode.listAllTsNodesBelow( true ) ) {
      T item = internalFindDisplayedItem( n.entity() );
      if( item != null ) {
        list.add( item );
      }
    }
    return list;
  }

  private T internalFindDisplayedItem( Object aObj ) {
    for( int i = 0; i < items().size(); i++ ) {
      T tmp = items().get( i );
      if( tmp == aObj || tmp.equals( aObj ) ) {
        return tmp;
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  /**
   * Determines if control is created and is valid.
   *
   * @return boolean - GUI state validity flags
   */
  final boolean isStateValid() {
    if( getControl() != null ) {
      if( !getControl().isDisposed() ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks control is created and is valid and throws an exception if not.
   *
   * @throws TsIllegalArgumentRtException viewer control was not created yet or already was disposed
   */
  final public void checkStateValidity() {
    TsIllegalStateRtException.checkFalse( isStateValid() );
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  final public Control createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( columnViewer );
    columnViewer = doCreateJfaceViewer( aParent );
    TsInternalErrorRtException.checkNull( columnViewer );
    columnViewer.addSelectionChangedListener( viewerSelectionChangedListener );
    columnViewer.addDoubleClickListener( viewerDoubleClickListener );
    columnViewer.setContentProvider( contentProvider );
    columnViewer.setLabelProvider( labelProvider );
    columnViewer.setComparator( sortManager.new InternalViewerComparator() );
    keyDownEventHelper.bindToControl( columnViewer.getControl() );
    columnViewer.getControl().addDisposeListener( aE -> {
      keyDownEventHelper.unbind();
      items().removeCollectionChangeListener( itemsChangeListener );
    } );
    items.addCollectionChangeListener( itemsChangeListener );
    doRefreshAll();
    return columnViewer.getControl();
  }

  @Override
  final public Control getControl() {
    if( columnViewer == null ) {
      return null;
    }
    return columnViewer.getControl();
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  abstract public T selectedItem();

  @Override
  abstract public void setSelectedItem( T aItem );

  @Override
  final public void addTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    itemSelectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  final public void removeTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    itemSelectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  final public void addTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    itemDoubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  final public void removeTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    itemDoubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyDownEventProducer
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
  // ITsCheckSupportable
  //

  @Override
  public ITsCheckSupport<T> checks() {
    return checks;
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize != aThumbSize ) {
      thumbSize = aThumbSize;
      thumbSizeEventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return thumbSizeEventer;
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return DEFAULT_THUMB_SIZE;
  }

  // ------------------------------------------------------------------------------------
  // IIconSizeableEx ???
  //

  @Override
  final public EIconSize iconSize() {
    return iconSize;
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    if( iconSize != aIconSize ) {
      iconSize = aIconSize;
      iconSizeEventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer iconSizeChangeEventer() {
    return iconSizeEventer;
  }

  @Override
  public EIconSize defaultIconSize() {
    return hdpiService().getJFaceCellIconsSize();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  final public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IM5CollectionViewer
  //

  @Override
  final public IM5Model<T> model() {
    return model;
  }

  @Override
  final public INotifierListEdit<T> items() {
    return items;
  }

  @Override
  public IM5ColumnManager<T> columnManager() {
    return columnManager;
  }

  @Override
  public IM5SortManager sortManager() {
    return sortManager;
  }

  @Override
  final public IM5FilterManager<T> filterManager() {
    return filterManager;
  }

  @Override
  final public IM5MenuManager<T> menuManager() {
    return menuManager;
  }

  @Override
  abstract public void reveal( T aItem );

  @Override
  abstract public T getTopItem();

  @Override
  abstract public int getVisibleRowsCount();

  @Override
  final public void refresh() {
    filterManager.refreshFilteredItems();
    doRefreshAll();
  }

  @Override
  final public void setFontProvider( ITableFontProvider aFontProvider ) {
    labelProvider.setFontProvider( aFontProvider );
    columnViewer.setLabelProvider( labelProvider );
  }

  @Override
  final public void setColorProvider( ITableColorProvider aColorProvider ) {
    labelProvider.setColorProvider( aColorProvider );
    columnViewer.setLabelProvider( labelProvider );
  }

  @Override
  final public boolean isColumnHeaderVisible() {
    return columnHeaderVisible;
  }

  @Override
  public void setColumnHeaderVisible( boolean aVisible ) {
    if( columnHeaderVisible != aVisible ) {
      columnHeaderVisible = aVisible;
      doSetJFaceColumnHeaderVisible( columnHeaderVisible );
    }
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Неследник должен создать JFace просмотрщик {@link TableViewer} или {@link TreeViewer}.
   * <p>
   * Также наследник должен задать видимость заголовка и линии, задать слушатель меню {@link #menuManager}.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @return {@link ColumnViewer} - созданный JFace-просмотрщик
   */
  abstract protected ColumnViewer doCreateJfaceViewer( Composite aParent );

  /**
   * Наследник должен обновить визуальное представление при не-структкрном изменении в элементе.
   *
   * @param aItem &lt;T&gt; - изменившейся элемент списка {@link #items()}, не бывает null
   */
  abstract protected void doRefreshItem( T aItem );

  /**
   * Наследник должен обновить весь просмотрщик, из элементов {@link IM5FilterManager#items()}.
   */
  abstract protected void doRefreshAll();

  /**
   * Наследник должен задать сортировку средствами JFace-просмотрщика
   * <p>
   * При не-null столбце aColumnOrNull используется SWT-бит порядка сортировки aSwtSortOrder, который равен
   * {@link SWT#DOWN} или {@link SWT#UP}.
   *
   * @param aColumnOrNull {@link IM5Column} - столбец сортировки или null
   * @param aSwtSortOrder int - SWT-бит порядка сортировки
   */
  abstract protected void doSortInJfaceViewer( IM5Column<T> aColumnOrNull, int aSwtSortOrder );

  /**
   * Должен вернуть массив индексов текущего расположения столбцов.
   * <p>
   * Фактически, возвращает результат вызова одного из двух методов {@link Table#getColumnOrder()} или
   * {@link Tree#getColumnOrder()}.
   *
   * @return int[] - массив индексов текущего расположения столбцов
   */
  abstract int[] doGetJfaceViewerColumnOrder();

  /**
   * Возвращает клиентскую область контроля просмотрщика.
   * <p>
   * Фактически, возвращает результат вызова одного из двух методов {@link Table#getClientArea()} или
   * {@link Tree#getClientArea()}.
   *
   * @return {@link Rectangle} - клиентская область контроля просмотрщика
   */
  abstract Rectangle doGetJfaceViewerClientArea();

  /**
   * Возвращает высоту заголовка просмотрщика.
   * <p>
   * Фактически, возвращает результат вызова одного из двух методов {@link Table#getHeaderHeight()} или
   * {@link Tree#getHeaderHeight()}.
   *
   * @return int - высота заголовка просмотрщика
   */
  abstract int doGetJfaceViewerHeaderHeight();

  abstract T doGetJfaceViewerItemAt( Point aCoors );

  abstract ITsNode doGetJfaceViewerNodeAt( Point aCoors );

  abstract void doSetJFaceColumnHeaderVisible( boolean aVisible );

}
