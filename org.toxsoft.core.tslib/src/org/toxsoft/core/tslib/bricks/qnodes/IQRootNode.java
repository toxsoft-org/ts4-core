package org.toxsoft.core.tslib.bricks.qnodes;

import org.toxsoft.core.tslib.bricks.events.*;

/**
 * The root node of {@link IQNode} tree hierarchy.
 *
 * @author hazard157
 */
@SuppressWarnings( "rawtypes" )
public sealed interface IQRootNode
    extends IQNode permits AbstractQRootNode {

  /**
   * Returns the tree change eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IQRootNodeChangeListener}&gt; - the eventer
   */
  ITsEventer<IQRootNodeChangeListener> eventer();

}
