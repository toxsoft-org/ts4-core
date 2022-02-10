package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.bricks.tstree.ITsViewerColumn;
import org.toxsoft.core.tsgui.graphics.EHorAlignment;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Абстрактный класс реализации {@link ITsViewerColumn}.
 *
 * @author goga
 */
abstract class AbstractTsColumn
    implements ITsViewerColumn {

  /**
   * Строка, добавляемая к образцу, для вычисления ширины колонки в {@link AbstractTsColumn#adjustWidth(String)}.
   */
  private static final String COLUMN_WIDTH_ADJUST = "ww";  //$NON-NLS-1$
  private int                 lastVisibleWidth    = -1;    // ширина колонки (для восстановления после скрытия)
  private boolean             hidden              = false; // признак скрытой колонки (т.е. колонки с нулевой шириной)

  protected AbstractTsColumn() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsColumn
  //

  @Override
  public EHorAlignment alignment() {
    return EHorAlignment.findBySwtStyle( doGetJfaceColumnAlignment() );
  }

  @Override
  public void setAlignment( EHorAlignment aAlignment ) {
    TsNullArgumentRtException.checkNull( aAlignment );
    EHorAlignment alignment;
    switch( aAlignment ) {
      case CENTER:
      case LEFT:
      case RIGHT:
        alignment = aAlignment;
        break;
      case FILL:
        alignment = EHorAlignment.CENTER;
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    doSetJfaceColumnAlignment( alignment.swtStyle() );
  }

  @Override
  public String title() {
    return doGetJfaceColumnText();
  }

  @Override
  public void setTitle( String aTitle ) {
    TsNullArgumentRtException.checkNull( aTitle );
    doSetJfaceColumnText( aTitle );
  }

  @Override
  public String tooltip() {
    return doGetJfaceColumnTooltip();
  }

  @Override
  public void setTooltip( String aTooltip ) {
    TsNullArgumentRtException.checkNull( aTooltip );
    doSetJfaceColumnTooltip( aTooltip );
  }

  @Override
  public Image headerImage() {
    return doGetJfaceColumnImage();
  }

  @Override
  public void setHeaderImage( Image aImage ) {
    doSetJfaceColumnImage( aImage );
  }

  @Override
  public int width() {
    if( isHidden() ) {
      return lastVisibleWidth;
    }
    return doGetJfaceColumnWidth();
  }

  @Override
  public void setWidth( int aPixelWidth ) {
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
  public void adjustWidth( String aSampleString ) {
    TsNullArgumentRtException.checkNull( aSampleString );
    Control control = doGetJfaceViewerControl();
    Font font = control.getFont();
    GC gc = null;
    try {
      gc = new GC( control.getShell().getDisplay() );
      gc.setFont( font );
      // добавим к ширине некоторый зазор шириной в указанную строку
      doSetJfaceColumnWidth( gc.stringExtent( aSampleString + COLUMN_WIDTH_ADJUST ).x );
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  @Override
  public boolean isHidden() {
    return hidden;
  }

  @Override
  public void setHidden( boolean aHidden ) {
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

  // ------------------------------------------------------------------------------------
  // Методы для реализации наследниками
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

  abstract Control doGetJfaceViewerControl();

  // abstract IStringList doGetCellTexts( int aRowsCount );

  abstract int getFirstColumnWidthAddition();

  abstract void doJfacePack();

}