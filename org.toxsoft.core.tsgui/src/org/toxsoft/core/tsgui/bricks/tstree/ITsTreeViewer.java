package org.toxsoft.core.tsgui.bricks.tstree;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Viewer of the {@link ITsNode} nodes.
 * <p>
 * Add columns and root nodes to the base interface {@link ITsBasicTreeViewer}.
 *
 * @author hazard157
 */
public interface ITsTreeViewer
    extends ITsBasicTreeViewer, IIconSizeableEx, IThumbSizeableEx {

  /**
   * Sets the nodes to be displayed as a root (top level) nodes.
   *
   * @param aRootNodes {@link ITsCollection}&lt;{@link ITsNode}&gt; - the root nodes
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any item has {@link ITsNode#parent()} != <code>this</code>
   */
  void setRootNodes( ITsCollection<ITsNode> aRootNodes );

  /**
   * Clears the tree content, removes all nodes from the tree.
   */
  void clear();

  /**
   * Returns columns of the tree.
   *
   * @return IList&lt;{@link ITsViewerColumn}&gt; - list of columns, may be an empty list
   */
  IList<ITsViewerColumn> columns();

  /**
   * Add columns to the right of current columns.
   *
   * @param aTitle String - the column title name
   * @param aAlignment {@link EHorAlignment} - text alignment in the cells of the column
   * @param aNameProvider {@link ITsVisualsProvider}&lt;{@link ITsNode}&gt; - cell text and image provider
   * @return {@link ITsViewerColumn} - created column
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ITsViewerColumn addColumn( String aTitle, EHorAlignment aAlignment, ITsVisualsProvider<ITsNode> aNameProvider );

  /**
   * Removes all columns.
   */
  void removeColumns();

  /**
   * Sets the cell font provider.
   *
   * @param aFontProvider {@link ITableFontProvider} - the cell font provider or <code>null</code> for default fonts
   */
  void setFontProvider( ITableFontProvider aFontProvider );

  /**
   * Sets the cell color provider.
   *
   * @param aColorProvider {@link ITableColorProvider} - the cell color provider or <code>null</code> for default colors
   */
  void setColorProvider( ITableColorProvider aColorProvider );

}
