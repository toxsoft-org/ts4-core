package org.toxsoft.core.tsgui.bricks.qtree.impl;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * {@link ITreeContentProvider} implementation assuming {@link IQNode} both as elements and input.
 *
 * @author hazard157
 */
public class QTreeContentProvider
    implements ITreeContentProvider {

  private static final IQNode[] EMPTY_NODES_ARRAY = {};

  /**
   * Constructor.
   */
  public QTreeContentProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static Object[] getChildNodesArray( Object aElement ) {
    if( aElement instanceof IQNode n ) {
      n.rebuildSubtree( false, false );
      return n.childs().toArray( EMPTY_NODES_ARRAY );
    }
    return EMPTY_NODES_ARRAY;
  }

  // ------------------------------------------------------------------------------------
  // ITreeContentProvider
  //

  @Override
  public void dispose() {
    // nop
  }

  @Override
  public void inputChanged( Viewer aViewer, Object aOldInput, Object aNewInput ) {
    // nop
  }

  @Override
  public Object[] getElements( Object aInputElement ) {
    if( aInputElement instanceof IQNode ) {
      return getChildNodesArray( aInputElement );
    }
    return TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
  }

  @Override
  public Object[] getChildren( Object aElement ) {
    return getChildNodesArray( aElement );
  }

  @Override
  public Object getParent( Object aElement ) {
    if( aElement instanceof IQNode n ) {
      return n.parent();
    }
    return null;
  }

  @Override
  public boolean hasChildren( Object aElement ) {
    if( aElement instanceof IQNode n ) {
      if( n.kind().canHaveChilds() ) {
        return !n.childs().isEmpty();
      }
    }
    return false;
  }

}
