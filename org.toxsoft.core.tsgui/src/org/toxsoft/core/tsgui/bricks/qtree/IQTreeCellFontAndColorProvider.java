package org.toxsoft.core.tsgui.bricks.qtree;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;

/**
 * Specifies text font and color of the individual cells in {@link IQTreeViewer}.
 *
 * @author hazard157
 */
public interface IQTreeCellFontAndColorProvider {

  /**
   * Default font and color provider singleton.
   */
  IQTreeCellFontAndColorProvider DEFAULT = new InternalNondeQTreeCellFontAndColorProvider();

  /**
   * Returns cell text font.
   *
   * @param aNode {@link IQNode} - the tree node (row)
   * @param aColumnIndex int index of column in {@link IQTreeColumnManager#columns()}
   * @return {@link Font} - cell text font or <code>null</code> for default font
   */
  Font getCellFont( IQNode aNode, int aColumnIndex );

  /**
   * Returns cell foreground (text) color.
   *
   * @param aNode {@link IQNode} - the tree node (row)
   * @param aColumnIndex int index of column in {@link IQTreeColumnManager#columns()}
   * @return {@link Color} - cell text color or <code>null</code> for default color
   */
  Color getCellForeground( IQNode aNode, int aColumnIndex );

  /**
   * Returns cell background color.
   *
   * @param aNode {@link IQNode} - the tree node (row)
   * @param aColumnIndex int index of column in {@link IQTreeColumnManager#columns()}
   * @return {@link Color} - cell background color or <code>null</code> for default color
   */
  Color getCellBackground( IQNode aNode, int aColumnIndex );

}

class InternalNondeQTreeCellFontAndColorProvider
    implements IQTreeCellFontAndColorProvider {

  @Override
  public Font getCellFont( IQNode aNode, int aColumnIndex ) {
    return null;
  }

  @Override
  public Color getCellForeground( IQNode aNode, int aColumnIndex ) {
    return null;
  }

  @Override
  public Color getCellBackground( IQNode aNode, int aColumnIndex ) {
    return null;
  }

}
