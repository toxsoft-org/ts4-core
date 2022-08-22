package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Базовый класс столбцов дерева {@link Tree}.
 *
 * @author hazard157
 */
public abstract class AbstractTsTreeColumn
    extends AbstractTsColumn {

  private final TreeColumn treeColumn;

  AbstractTsTreeColumn( TreeColumn aCol ) {
    treeColumn = aCol;
  }

  @Override
  void doSetJfaceColumnWidth( int aWidth ) {
    treeColumn.setWidth( aWidth );
  }

  @Override
  int doGetJfaceColumnWidth() {
    return treeColumn.getWidth();
  }

  @Override
  void doSetJfaceColumnAlignment( int aSwtStyleBit ) {
    treeColumn.setAlignment( aSwtStyleBit );
  }

  @Override
  int doGetJfaceColumnAlignment() {
    return treeColumn.getAlignment();
  }

  @Override
  void doSetJfaceColumnText( String aText ) {
    treeColumn.setText( aText );
  }

  @Override
  String doGetJfaceColumnText() {
    return treeColumn.getText();
  }

  @Override
  void doSetJfaceColumnTooltip( String aTooltip ) {
    treeColumn.setToolTipText( aTooltip );
  }

  @Override
  String doGetJfaceColumnTooltip() {
    return treeColumn.getToolTipText();
  }

  @Override
  void doSetJfaceColumnImage( Image aImage ) {
    treeColumn.setImage( aImage );
  }

  @Override
  Image doGetJfaceColumnImage() {
    return treeColumn.getImage();
  }

  @Override
  void doSetJfaceColumnResizable( boolean aResizable ) {
    treeColumn.setResizable( aResizable );
  }

  @Override
  Control doGetJfaceViewerControl() {
    return treeColumn.getParent().getParent();
  }

  // @Override
  // abstract IStringList doGetCellTexts( int aRowsCount );

}
