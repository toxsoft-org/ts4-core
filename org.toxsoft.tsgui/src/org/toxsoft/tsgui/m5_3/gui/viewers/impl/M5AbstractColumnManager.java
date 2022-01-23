package org.toxsoft.tsgui.m5_3.gui.viewers.impl;

import static org.toxsoft.tsgui.m5_3.IM5Constants.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;
import org.toxsoft.tsgui.m5_3.IM5FieldDef;
import org.toxsoft.tsgui.m5_3.IM5Getter;
import org.toxsoft.tsgui.m5_3.gui.viewers.*;
import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.tslib.utils.ESortOrder;
import org.toxsoft.tslib.utils.errors.TsInternalErrorRtException;

/**
 * {@link IM5ColumnManager} base implementation.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
abstract class M5AbstractColumnManager<T>
    implements IM5ColumnManager<T> {

  private static final int INITIAL_DEFAULT_COLUMN_WIDTH = 150;

  /**
   * Column header click handler - changes the sort order.
   * <p>
   * Set to columnt at creation in method {@link #doInsert(int, String, IM5Getter)}.
   */
  final SelectionListener headerClickHandler = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent aEvent ) {
      IM5Column<T> column = findM5ColumnBySwtColumn( aEvent.widget );
      TsInternalErrorRtException.checkNull( column );
      // for unsorted column will be set ascending order
      ESortOrder newSortOrder = ESortOrder.ASCENDING;
      IM5SortManager sm = owner.sortManager();
      // for sorted column next sort order will be set
      if( sm.sortOrder() != ESortOrder.NONE && column.fieldId().equals( sm.sortFieldId() ) ) {
        newSortOrder = sm.sortOrder().nextSortOrderW();
      }
      owner.sortManager().sort( newSortOrder, column.fieldId() );
    }

  };

  private final IStringMapEdit<IM5Column<T>>    columns = new StringMap<>();
  protected final M5AbstractCollectionViewer<T> owner;

  /**
   * Constructor for subclasses.
   *
   * @param aOwner {@link M5AbstractCollectionViewer} - the owner
   */
  protected M5AbstractColumnManager( M5AbstractCollectionViewer<T> aOwner ) {
    owner = aOwner;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private <V> IM5Column<T> insert( int aColumnIndex, String aFieldId, IM5Getter<T, V> aGetter ) {
    IM5FieldDef<T, ?> fDef = owner.model().fieldDefs().getByKey( aFieldId );
    IM5Column<T> col = doInsert( aColumnIndex, aFieldId, aGetter );
    TsInternalErrorRtException.checkNull( col );
    Image img = fDef.getter().getIcon( null, owner.iconSize() );
    col.setHeaderImage( img );
    col.setHidden( false );
    col.setTitle( fDef.nmName() );
    col.setTooltip( fDef.description() );
    col.setWidth( INITIAL_DEFAULT_COLUMN_WIDTH );
    col.setAlignment( M5_OPDEF_COLUMN_ALIGN.getValue( fDef.params() ).asValobj() );
    owner.refresh();
    return col;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Finds M5-column index in {@link #columns} by the SWT column.
   *
   * @param aJfaceViewerColumn {@link Widget} - the SWT column
   * @return int - M5-column index in {@link #columns} or -1
   */
  abstract IM5Column<T> findM5ColumnBySwtColumn( Widget aJfaceViewerColumn );

  abstract protected IM5Column<T> doInsert( int aColumnIndex, String aFieldId, IM5Getter<T, ?> aGetter );

  abstract protected void doRemoveControlFromControl( IM5Column<T> aRemovedColumn );

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5ColumnManager
  //

  @Override
  final public IStringMapEdit<IM5Column<T>> columns() {
    return columns;
  }

  @Override
  final public IM5Column<T> add( String aFieldId ) {
    owner.checkStateValidity();
    return add( aFieldId, null );
  }

  @Override
  final public IM5Column<T> add( String aFieldId, IM5Getter<T, ?> aGetter ) {
    owner.checkStateValidity();
    return insert( columns.size(), aFieldId, aGetter );
  }

  @Override
  public void remove( String aFieldId ) {
    owner.checkStateValidity();
    IM5Column<T> col = columns.removeByKey( aFieldId );
    doRemoveControlFromControl( col );
  }

}
