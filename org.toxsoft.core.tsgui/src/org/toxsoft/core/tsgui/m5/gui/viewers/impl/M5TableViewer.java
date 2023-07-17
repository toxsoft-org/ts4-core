package org.toxsoft.core.tsgui.m5.gui.viewers.impl;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5TableViewer} implementation.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5TableViewer<T>
    extends M5AbstractCollectionViewer<T>
    implements IM5TableViewer<T> {

  /**
   * Table column, an {@link IM5Column} implementation.
   *
   * @author hazard157
   * @param <T> - modeled entity type
   */
  static class Column<T>
      extends M5AbstractColumn<T> {

    final TableViewerColumn tvColumn;

    Column( M5AbstractCollectionViewer<T> aOwner, String aFieldId, TableViewerColumn aTvColumn,
        IM5Getter<T, ?> aGetter ) {
      super( aOwner, aFieldId, aGetter );
      tvColumn = aTvColumn;
      init();
    }

    TableViewerColumn getTableViewerColumn() {
      return tvColumn;
    }

    @Override
    public void setValedEditingSupport( ITsNodeValedProvider aValedProvider ) {
      throw new TsUnsupportedFeatureRtException();
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
      return 0;
    }

    @Override
    void doJfacePack() {
      tvColumn.getColumn().pack();
    }

  }

  /**
   * Table column manager, an {@link IM5ColumnManager} implementation.
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
        if( ((Column<T>)c).getTableViewerColumn().getColumn().equals( aSwtViewerColumn ) ) {
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
      // create SWT-column TableViewerColumn
      TableViewerColumn tvColumn = new TableViewerColumn( tableViewer, SWT.LEFT, aColumnIndex );
      tableViewer.setLabelProvider( labelProvider ); // refresh label provider for cells of this column
      tvColumn.getColumn().addSelectionListener( headerClickHandler );
      // create M5-column M5AbstractColumn
      M5AbstractColumn<T> col = new Column<>( owner, aFieldId, tvColumn, aGetter );
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
      ((Column<T>)aRemovedColumn).getTableViewerColumn().getColumn().dispose();
    }

  }

  /**
   * Table label provider returns cell text and image using {@link IM5Column}.
   *
   * @author hazard157
   */
  class InternalTableLabelProvider
      extends InternalAbstractLabelProvider {

    @SuppressWarnings( "unchecked" )
    @Override
    public String getColumnText( Object aElement, int aColumnIndex ) {
      // columns are optional so check if there is any
      if( columnManager.columns().isEmpty() ) {
        return getText( aElement );
      }
      IM5Column<T> col = columnManager().columns().values().get( aColumnIndex );
      return col.getCellText( (T)aElement );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Image getColumnImage( Object aElement, int aColumnIndex ) {
      if( columnManager.columns().isEmpty() ) {
        return getImage( aElement );
      }
      IM5Column<T> col = columnManager().columns().values().get( aColumnIndex );
      if( col.isUseThumb() ) {
        TsImage thumb = col.getCellThumb( (T)aElement, thumbSize() );
        if( thumb != null ) {
          return thumb.image();
        }
        return null;
      }
      return col.getCellIcon( (T)aElement, iconSize() );
    }

  }

  /**
   * Simple contetn provider - returns {@link M5TableViewer#items} as array.
   *
   * @author hazard157
   */
  class InternalTableContentProvider
      extends StructuredContentProviderAdapter<Object> {

    @Override
    public Object[] getElements( Object aInputElement ) {
      return filterManager().items().toArray();
    }

  }

  /**
   * SWT {@link Table} specific sort manager, an {@link IM5SortManager} implementation.
   *
   * @author hazard157
   */
  class InternalTableSortManager
      extends InternalSortManager {

    @SuppressWarnings( "unchecked" )
    @Override
    protected int doCompare( Object aO1, Object aO2 ) {
      return objectComparator.compare( (T)aO1, (T)aO2 );
    }

  }

  /**
   * SWT style of created {@link Table}.
   */
  protected static final int DEFAULT_TABLE_STYLE =
      SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION;

  private TableViewer              tableViewer         = null;
  private CheckboxTableViewer      checkboxTableViewer = null;
  private ViewerPaintHelper<Table> paintHelper         = null;

  /**
   * Constructor of non-checkable table.
   * <p>
   * Directly hold reference to the <code>aItems</code>, does not copies it.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - M5-model of entities
   * @param aItems {@link INotifierListEdit}&lt;T&gt; - the list of entities to be displayed
   * @param aIsChecksSupported boolean - <code>true</code> enables elements check state support
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5TableViewer( ITsGuiContext aContext, IM5Model<T> aModel, INotifierListEdit<T> aItems,
      boolean aIsChecksSupported ) {
    super( aContext, aModel, aItems, aIsChecksSupported );
    columnManager = new ColumnManager( this );
    contentProvider = new InternalTableContentProvider();
    labelProvider = new InternalTableLabelProvider();
    sortManager = new InternalTableSortManager();
  }

  /**
   * Constructor of non-checkable table.
   * <p>
   * Directly hold reference to the <code>aItems</code>, does not copies it.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - M5-model of entities
   * @param aItems {@link INotifierListEdit}&lt;T&gt; - the list of entities to be displayed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5TableViewer( ITsGuiContext aContext, IM5Model<T> aModel, INotifierListEdit<T> aItems ) {
    this( aContext, aModel, aItems, false );
  }

  /**
   * Constructor of non-checkable table.
   * <p>
   * Creates internal list of items.
   *
   * @param aContext {@link ITsGuiContext} - контекст просмотрщика (включает контекст приложения и параметры)
   * @param aModel {@link IM5Model} - модель отображаемых объектов
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5TableViewer( ITsGuiContext aContext, IM5Model<T> aModel ) {
    this( aContext, aModel, new NotifierListEditWrapper<>( new ElemLinkedBundleList<T>() ) );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void internalInitViewers( Composite aParent ) {
    if( !checks.isChecksSupported() ) {
      tableViewer = new TableViewer( aParent, DEFAULT_TABLE_STYLE );
      checkboxTableViewer = null;
      return;
    }
    checkboxTableViewer = CheckboxTableViewer.newCheckList( aParent, SWT.CHECK | DEFAULT_TABLE_STYLE );
    tableViewer = checkboxTableViewer;
    checkboxTableViewer.addCheckStateListener( new SwtCheckStateListener() );
    checkboxTableViewer.setCheckStateProvider( new ICheckStateProvider() {

      @Override
      public boolean isGrayed( Object aElement ) {
        return false;
      }

      @Override
      public boolean isChecked( Object aElement ) {
        if( aElement instanceof ITsNode node ) {
          Object item = node.entity();
          for( int i = 0, c = 0; i < c; i++ ) {
            if( items().get( i ).equals( item ) ) {
              return true;
            }
          }
        }
        return false;
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // IM5TableViewer
  //

  @Override
  public void setTablePaintHelper( ViewerPaintHelper<Table> aHelper ) {
    checkStateValidity();
    if( aHelper == null ) {
      if( paintHelper != null ) {
        paintHelper.deinstall();
        paintHelper = null;
        tableViewer.refresh();
      }
      return;
    }
    aHelper.install( tableViewer.getTable() );
    paintHelper = aHelper;
    tableViewer.refresh();
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @SuppressWarnings( "unchecked" )
  @Override
  public T selectedItem() {
    checkStateValidity();
    IStructuredSelection ss = (IStructuredSelection)tableViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    return (T)ss.getFirstElement();
  }

  @Override
  public void setSelectedItem( T aItem ) {
    checkStateValidity();
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aItem != null ) {
      selection = new StructuredSelection( aItem );
    }
    tableViewer.setSelection( selection, true );
  }

  // ------------------------------------------------------------------------------------
  // IM5CollectionViewer
  //

  @Override
  public void reveal( T aItem ) {
    checkStateValidity();
    TsNullArgumentRtException.checkNull( aItem );
    tableViewer.reveal( aItem );
  }

  @Override
  public T getTopItem() {
    checkStateValidity();
    checkStateValidity();
    int topIndex = tableViewer.getTable().getTopIndex();
    if( topIndex >= 0 ) {
      return filterManager().items().get( topIndex );
    }
    return null;
  }

  @Override
  public int getVisibleRowsCount() {
    checkStateValidity();
    Table table = tableViewer.getTable();
    Rectangle rect = table.getClientArea();
    int itemHeight = table.getItemHeight();
    int headerHeight = table.getHeaderHeight();
    return (rect.height - headerHeight + itemHeight - 1) / itemHeight;
  }

  // ------------------------------------------------------------------------------------
  // M5AbstractCollectionViewer
  //

  @Override
  protected ColumnViewer doCreateJfaceViewer( Composite aParent ) {
    internalInitViewers( aParent );
    Table table = tableViewer.getTable();
    table.setHeaderVisible( isColumnHeaderVisible() );
    table.setLinesVisible( true );
    table.addMenuDetectListener( menuManager );
    return tableViewer;
  }

  @Override
  protected void doRefreshItem( T aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    tableViewer.update( aItem, null );
  }

  @Override
  protected void doRefreshAll() {
    tableViewer.setInput( filterManager().items() );
    // tableViewer.refresh();
  }

  @Override
  protected void doSortInJfaceViewer( IM5Column<T> aColumnOrNull, int aSwtSortOrder ) {
    Column<T> col = (Column<T>)aColumnOrNull;
    TableColumn tcol = null;
    if( col != null ) {
      tcol = col.tvColumn.getColumn();
    }
    tableViewer.getTable().setSortColumn( tcol );
    tableViewer.getTable().setSortDirection( aSwtSortOrder );
    tableViewer.refresh( true );
  }

  @Override
  int[] doGetJfaceViewerColumnOrder() {
    return tableViewer.getTable().getColumnOrder();
  }

  @Override
  Rectangle doGetJfaceViewerClientArea() {
    return tableViewer.getTable().getClientArea();
  }

  @Override
  int doGetJfaceViewerHeaderHeight() {
    return tableViewer.getTable().getHeaderHeight();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  T doGetJfaceViewerItemAt( Point aCoors ) {
    TableItem item = tableViewer.getTable().getItem( aCoors );
    if( item != null ) {
      return (T)item.getData();
    }
    return null;
  }

  @Override
  ITsNode doGetJfaceViewerNodeAt( Point aCoors ) {
    return null;
  }

  @Override
  void doSetJFaceColumnHeaderVisible( boolean aVisible ) {
    tableViewer.getTable().setHeaderVisible( aVisible );
  }

}
