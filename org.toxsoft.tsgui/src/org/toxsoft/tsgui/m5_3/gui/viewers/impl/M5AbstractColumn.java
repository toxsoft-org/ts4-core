package org.toxsoft.tsgui.m5_3.gui.viewers.impl;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.graphics.EHorAlignment;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.image.EThumbSize;
import org.toxsoft.tsgui.graphics.image.TsImage;
import org.toxsoft.tsgui.m5_3.IM5FieldDef;
import org.toxsoft.tsgui.m5_3.IM5Getter;
import org.toxsoft.tsgui.m5_3.gui.viewers.IM5Column;
import org.toxsoft.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IM5Column} abstract implementation.
 * <p>
 * This base class and both subclasses exist just for one reason - defect of design in Jface/SWT table/tree realization.
 * Pairs of VERY similar classes {@link TableViewerColumn} / {@link TreeViewerColumn} and {@link TableColumn} /
 * {@link TreeColumn} does not have common base class.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
abstract class M5AbstractColumn<T>
    implements IM5Column<T> {

  /**
   * Text addon string to calculate column width in {@link M5AbstractColumn#adjustWidth(String)}.
   */
  private static final String STR_COLUMN_WIDTH_ADJUST = "ww"; //$NON-NLS-1$

  private final IM5FieldDef<T, ?> fieldDef;
  private final IM5Getter<T, ?>   getter;
  private int                     lastVisibleWidth; // column width in pixels (to restore after hiding)
  private boolean                 hidden = false;   // hidden column flag (that is column temporary has width = 0)

  /**
   * This column owner viewer.
   */
  protected final M5AbstractCollectionViewer<T> ownerM5Viewer;

  M5AbstractColumn( M5AbstractCollectionViewer<T> aOwner, String aFieldId, IM5Getter<T, ?> aGetter ) {
    ownerM5Viewer = aOwner;
    fieldDef = ownerM5Viewer.model().fieldDefs().getByKey( aFieldId );
    if( aGetter != null ) {
      getter = aGetter;
    }
    else {
      getter = fieldDef.getter();
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * This method must be called in subclass constructor when methods doXetJfaceColumXxx() are correctly working.
   */
  void init() {
    doSetJfaceColumnWidth( 50 ); // will be recalculated later
    doSetJfaceColumnAlignment( EHorAlignment.LEFT.swtStyle() );
    doSetJfaceColumnText( fieldDef.nmName() ); // column header text
    doSetJfaceColumnTooltip( fieldDef.description() );
    // adjust column width on on default text or column header text
    String sampleText = getter.getName( null );
    if( sampleText.isEmpty() ) {
      sampleText = fieldDef.nmName();
    }
    adjustWidth( sampleText + STR_COLUMN_WIDTH_ADJUST );
  }

  // ------------------------------------------------------------------------------------
  // IM5Column
  //

  @Override
  final public String fieldId() {
    return fieldDef.id();
  }

  @Override
  final public EHorAlignment alignment() {
    return EHorAlignment.findBySwtStyle( doGetJfaceColumnAlignment() );
  }

  @Override
  final public void setAlignment( EHorAlignment aAlignment ) {
    TsNullArgumentRtException.checkNull( aAlignment );
    EHorAlignment alignment = switch( aAlignment ) {
      case CENTER, LEFT, RIGHT -> aAlignment;
      case FILL -> EHorAlignment.CENTER;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    doSetJfaceColumnAlignment( alignment.swtStyle() );
  }

  @Override
  final public String title() {
    return doGetJfaceColumnText();
  }

  @Override
  final public void setTitle( String aTitle ) {
    TsNullArgumentRtException.checkNull( aTitle );
    doSetJfaceColumnText( aTitle );
  }

  @Override
  final public String tooltip() {
    return doGetJfaceColumnTooltip();
  }

  @Override
  final public void setTooltip( String aTooltip ) {
    TsNullArgumentRtException.checkNull( aTooltip );
    doSetJfaceColumnTooltip( aTooltip );
  }

  @Override
  final public Image headerImage() {
    return doGetJfaceColumnImage();
  }

  @Override
  final public void setHeaderImage( Image aImage ) {
    doSetJfaceColumnImage( aImage );
  }

  @Override
  final public int width() {
    if( isHidden() ) {
      return lastVisibleWidth;
    }
    return doGetJfaceColumnWidth();
  }

  @Override
  final public void setWidth( int aPixelWidth ) {
    if( aPixelWidth <= 0 ) {
      return;
    }
    if( isHidden() ) {
      lastVisibleWidth = aPixelWidth;
    }
    else {
      doSetJfaceColumnWidth( aPixelWidth );
    }
  }

  @Override
  public void pack() {
    doJfacePack();
  }

  @Override
  final public void adjustWidth( String aSampleString ) {
    TsNullArgumentRtException.checkNull( aSampleString );
    Control control = ownerM5Viewer.getControl();
    Font font = control.getFont();
    GC gc = null;
    try {
      gc = new GC( control.getShell().getDisplay() );
      gc.setFont( font );
      // additional width for first column for the tree expand/collapse icon
      int addToWidth = getFirstColumnWidthAddition();
      // additional width for better text rendering
      doSetJfaceColumnWidth( gc.stringExtent( aSampleString + STR_COLUMN_WIDTH_ADJUST ).x + addToWidth );
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  @Override
  final public boolean isHidden() {
    return hidden;
  }

  @Override
  final public void setHidden( boolean aHidden ) {
    if( hidden == aHidden ) {
      return;
    }
    if( aHidden ) {
      lastVisibleWidth = width();
      doSetJfaceColumnWidth( 0 );
    }
    else {
      doSetJfaceColumnWidth( lastVisibleWidth );
    }
    doSetJfaceColumnResizable( !aHidden );
    hidden = aHidden;
  }

  @Override
  final public String getCellText( T aEntity ) {
    return getter.getName( aEntity );
  }

  @Override
  public String getCellTooltip( T aEntity ) {
    return getter.getDescription( aEntity );
  }

  @Override
  final public TsImage getCellThumb( T aEntity, EThumbSize aThubSize ) {
    return getter.getThumb( aEntity, aThubSize );
  }

  @Override
  final public Image getCellIcon( T aEntity, EIconSize aIconSize ) {
    return getter.getIcon( aEntity, aIconSize );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ':' + fieldDef.id();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  abstract void doSetJfaceColumnWidth( int aWidth );

  abstract int doGetJfaceColumnWidth();

  abstract void doSetJfaceColumnAlignment( int aSwtStyleBit );

  abstract int doGetJfaceColumnAlignment();

  abstract void doSetJfaceColumnText( String aText );

  abstract String doGetJfaceColumnText();

  abstract void doSetJfaceColumnTooltip( String aTooltip );

  abstract String doGetJfaceColumnTooltip();

  abstract void doSetJfaceColumnImage( Image aImage );

  abstract Image doGetJfaceColumnImage();

  abstract void doSetJfaceColumnResizable( boolean aResizable );

  abstract int getFirstColumnWidthAddition();

  abstract void doJfacePack();

}
