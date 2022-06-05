package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

/**
 * Базовый класс столбцов дерева {@link Table}.
 *
 * @author hazard157
 */
public abstract class AbstractTsTableColumn
    extends AbstractTsColumn {

  private final TableColumn tableColumn;

  AbstractTsTableColumn( TableColumn aCol ) {
    tableColumn = aCol;
  }

  @Override
  void doSetJfaceColumnWidth( int aWidth ) {
    tableColumn.setWidth( aWidth );
  }

  @Override
  int doGetJfaceColumnWidth() {
    return tableColumn.getWidth();
  }

  @Override
  void doSetJfaceColumnAlignment( int aSwtStyleBit ) {
    tableColumn.setAlignment( aSwtStyleBit );
  }

  @Override
  int doGetJfaceColumnAlignment() {
    return tableColumn.getAlignment();
  }

  @Override
  void doSetJfaceColumnText( String aText ) {
    tableColumn.setText( aText );
  }

  @Override
  String doGetJfaceColumnText() {
    return tableColumn.getText();
  }

  @Override
  void doSetJfaceColumnTooltip( String aTooltip ) {
    tableColumn.setToolTipText( aTooltip );
  }

  @Override
  String doGetJfaceColumnTooltip() {
    return tableColumn.getToolTipText();
  }

  @Override
  void doSetJfaceColumnImage( Image aImage ) {
    tableColumn.setImage( aImage );
  }

  @Override
  Image doGetJfaceColumnImage() {
    return tableColumn.getImage();
  }

  @Override
  void doSetJfaceColumnResizable( boolean aResizable ) {
    tableColumn.setResizable( aResizable );
  }

  @Override
  Control doGetJfaceViewerControl() {
    return tableColumn.getParent().getParent();
  }

  @Override
  void doJfacePack() {
    tableColumn.pack();
  }

  @Override
  abstract int getFirstColumnWidthAddition();

  // @Override
  // abstract IStringList doGetCellTexts( int aRowsCount );

}
