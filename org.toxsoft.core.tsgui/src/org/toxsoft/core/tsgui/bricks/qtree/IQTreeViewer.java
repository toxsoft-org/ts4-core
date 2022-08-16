package org.toxsoft.core.tsgui.bricks.qtree;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * TODO under development!
 *
 * @author hazard157
 */
public interface IQTreeViewer
    extends ITsSelectionProvider<IQNode>, ITsDoubleClickEventProducer<IQNode>, ITsUserInputProducer,
    ILazyControl<Control> {

  /**
   * Retruns the invisible root node of the tree.
   *
   * @return {@link IQRootNode} - the invisible root node
   */
  IQRootNode rootNode();

  /**
   * Sets root node and refreshes the tree.
   *
   * @param aRootNode {@link IQRootNode} - the root node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setRoot( IQRootNode aRootNode );

  /**
   * Returns console to work with tree.
   *
   * @return {@link IQTreeConsole} - the managing console
   */
  IQTreeConsole console();

}
