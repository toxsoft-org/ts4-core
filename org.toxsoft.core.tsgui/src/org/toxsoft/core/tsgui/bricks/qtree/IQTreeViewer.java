package org.toxsoft.core.tsgui.bricks.qtree;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Viewer for {@link IQRootNode} tree structore.
 * <p>
 * TODO under development! For each cell there is a flag {@link IQTreeColumn#isUseThumb()} determining what to show:
 * either an icon {@link ITsVisualsProvider#getIcon(Object, EIconSize)} or still image
 * {@link ITsVisualsProvider#getThumb(Object, EThumbSize)}. However, there is some restirctions from the SWT side:
 * <ul>
 * <li>all rows have same size of images;</li>
 * <li>size of row images are determined by first row's first cell image size;</li>
 * <li>so images size is determined by the settings of {@link #iconSize()}, {@link #thumbSize()} and first drawn cell's
 * {@link IQTreeColumn#isUseThumb()} flag.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IQTreeViewer
    extends ITsSelectionProvider<IQNode>, ITsDoubleClickEventProducer<IQNode>, ITsUserInputProducer, IIconSizeableEx,
    IThumbSizeableEx, ILazyControl<Control> {

  /**
   * Retruns the invisible root node of the tree.
   *
   * @return {@link IQRootNode} - the invisible root node
   */
  IQRootNode rootNode();

  /**
   * Sets root node and refreshes the tree.
   * <p>
   * Setting root node to <code>null</code> causes viewer to set internal root node implementation that is equivalent to
   * clear tree.
   *
   * @param aRootNode {@link IQRootNode} - the root node or <code>null</code>
   */
  void setRoot( IQRootNode aRootNode );

  /**
   * Returns the colmn manager.
   *
   * @return {@link IQTreeColumnManager} - the colmn manager
   */
  IQTreeColumnManager columnManager();

  /**
   * Sets text font and color provider of the individual cells.
   *
   * @param aProvider {@link InternalNondeQTreeCellFontAndColorProvider} - font and color provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setCellFontAndColorProvider( IQTreeCellFontAndColorProvider aProvider );

  /**
   * Returns console to work with tree.
   *
   * @return {@link IQTreeConsole} - the managing console
   */
  IQTreeConsole console();

}
