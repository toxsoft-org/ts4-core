package org.toxsoft.core.tsgui.bricks.qtree.impl;

import static org.toxsoft.core.tsgui.bricks.qtree.IQTreeViewerConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.qtree.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Provides cell texts, icons, font and color for {@link QTreeViewer}.
 *
 * @author hazard157
 */
class QTreeLabelProvider
    extends TableLabelProviderAdapter
    implements ITableColorProvider, ITableFontProvider, IIconSizeable, IThumbSizeable, ITsGuiContextable {

  private final QTreeViewer owner;

  private EIconSize  iconSize;
  private EThumbSize thumbSize;

  private IQTreeCellFontAndColorProvider fontAndColorProvider = IQTreeCellFontAndColorProvider.DEFAULT;

  public QTreeLabelProvider( QTreeViewer aOwner ) {
    owner = aOwner;
    iconSize = defaultIconSize();
    thumbSize = defaultThumbSize();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return owner.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ITableLabelProvider
  //

  @Override
  public String getColumnText( Object aElement, int aColumnIndex ) {
    if( aElement instanceof IQNode node ) {
      IQTreeColumn column = owner.columnManager().columns().get( aColumnIndex );
      return column.visProvider().getName( node );
    }
    return null;
  }

  @Override
  public Image getColumnImage( Object aElement, int aColumnIndex ) {
    Image image = null;
    if( aElement instanceof IQNode node ) {
      IQTreeColumn column = owner.columnManager().columns().get( aColumnIndex );
      if( column.isUseThumb() ) {
        TsImage tsimg = column.visProvider().getThumb( node, thumbSize );
        if( tsimg != null ) {
          image = tsimg.image();
        }
      }
      else {
        image = column.visProvider().getIcon( node, iconSize );
      }
    }
    return image;
  }

  // ------------------------------------------------------------------------------------
  // ITableFontProvider
  //

  @Override
  public Font getFont( Object aElement, int aColumnIndex ) {
    IQNode node = IQNode.class.cast( aElement );
    return fontAndColorProvider.getCellFont( node, aColumnIndex );
  }

  // ------------------------------------------------------------------------------------
  // ITableColorProvider
  //

  @Override
  public Color getForeground( Object aElement, int aColumnIndex ) {
    IQNode node = IQNode.class.cast( aElement );
    return fontAndColorProvider.getCellForeground( node, aColumnIndex );
  }

  @Override
  public Color getBackground( Object aElement, int aColumnIndex ) {
    IQNode node = IQNode.class.cast( aElement );
    return fontAndColorProvider.getCellBackground( node, aColumnIndex );
  }

  // ------------------------------------------------------------------------------------
  // IIconSizeable
  //

  @Override
  public EIconSize iconSize() {
    return iconSize;
  }

  @Override
  public EIconSize defaultIconSize() {
    return tsContext().params().getValobj( OPDEF_DEFAULT_ICON_SIZE );
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    iconSize = aIconSize;
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeable
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return tsContext().params().getValobj( OPDEF_DEFAULT_THUMB_SIZE );
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    thumbSize = aThumbSize;
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  void setFontAndColorProvider( IQTreeCellFontAndColorProvider aProvider ) {
    TsNullArgumentRtException.checkNull( aProvider );
    fontAndColorProvider = aProvider;
  }

}
