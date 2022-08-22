package org.toxsoft.core.tsgui.bricks.qtree.impl;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.services.*;
import org.toxsoft.core.tsgui.bricks.qtree.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQTreeColumn} implementation.
 *
 * @author hazard157
 */
class QTreeColumn
    implements IQTreeColumn, IDisposable {

  /**
   * Text to add to {@link IQTreeColumn#adjustWidth(String)} method argument for an extra space.
   */
  private static final String COLUMN_WIDTH_ADJUST = "ww"; //$NON-NLS-1$

  private final TreeColumn                 treeColumn;
  private final ITsVisualsProvider<IQNode> visProvider;

  private int     lastVisibleWidth = -1;    // width of columnt, saved while it's hidden
  private boolean hidden           = false; // hidden coleumn flag
  private boolean useThumb         = false; // flags to use visProvider.getThumb() for icons

  public QTreeColumn( TreeColumn aTreeColumn, ITsVisualsProvider<IQNode> aVisProvider ) {
    treeColumn = aTreeColumn;
    visProvider = aVisProvider;
  }

  @Override
  public void dispose() {
    treeColumn.dispose();
  }

  // ------------------------------------------------------------------------------------
  // IQTreeColumn
  //

  @Override
  public String title() {
    return treeColumn.getText();
  }

  @Override
  public void setTitle( String aTitle ) {
    TsNullArgumentRtException.checkNull( aTitle );
    treeColumn.setText( aTitle );
  }

  @Override
  public ITsVisualsProvider<IQNode> visProvider() {
    return visProvider;
  }

  @Override
  public EHorAlignment alignment() {
    return EHorAlignment.findBySwtStyle( treeColumn.getAlignment() );
  }

  @Override
  public void setAlignment( EHorAlignment aAlignment ) {
    TsNullArgumentRtException.checkNull( aAlignment );
    EHorAlignment alignment = switch( aAlignment ) {
      case CENTER, LEFT, RIGHT -> aAlignment;
      case FILL -> EHorAlignment.CENTER;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    treeColumn.setAlignment( alignment.swtStyle() );
  }

  @Override
  public String tooltip() {
    return treeColumn.getToolTipText();
  }

  @Override
  public void setTooltip( String aTooltip ) {
    TsNullArgumentRtException.checkNull( aTooltip );
    treeColumn.setToolTipText( aTooltip );
  }

  @Override
  public Image headerImage() {
    return treeColumn.getImage();
  }

  @Override
  public void setHeaderImage( Image aImage ) {
    treeColumn.setImage( aImage );
  }

  @Override
  public int width() {
    if( isHidden() ) {
      return lastVisibleWidth;
    }
    return treeColumn.getWidth();
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
      treeColumn.setWidth( aPixelWidth );
    }
  }

  @Override
  public void pack() {
    treeColumn.pack();
  }

  @Override
  public void adjustWidth( String aSampleString ) {
    TsNullArgumentRtException.checkNull( aSampleString );
    Control control = treeColumn.getParent().getParent();
    Font font = control.getFont();
    GC gc = null;
    try {
      gc = new GC( control.getShell().getDisplay() );
      gc.setFont( font );
      // add an extra space string to the sample string
      Point extent = gc.stringExtent( aSampleString + COLUMN_WIDTH_ADJUST );
      treeColumn.setWidth( extent.x );
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
      treeColumn.setWidth( 0 );
    }
    else {
      treeColumn.setWidth( lastVisibleWidth );
    }
    treeColumn.setResizable( !aHidden );
    hidden = aHidden;
  }

  @Override
  public boolean isUseThumb() {
    return useThumb;
  }

  @Override
  public void setUseThumb( boolean aUseThumb ) {
    useThumb = aUseThumb;
  }

}
