package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Показывает модель дерева узлов {@link ITsNode}.
 * <p>
 * В качестве как входного элемента, так и всех узлов ожидается {@link ITsNode}.
 *
 * @author hazard157
 */
public class TsTreeContentProvider
    implements ITreeContentProvider {

  private static final ITsNode[] EMPTY_NODES_ARRAY = new ITsNode[0];

  /**
   * Конструктор.
   */
  public TsTreeContentProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private static Object[] getChildNodesArray( Object aElement ) {
    if( aElement instanceof ITsNode ) {
      ITsNode n = (ITsNode)aElement;
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
    if( aInputElement instanceof ITsNode ) {
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
    if( aElement instanceof ITsNode ) {
      ITsNode n = (ITsNode)aElement;
      return n.parent();
    }
    return null;
  }

  @Override
  public boolean hasChildren( Object aElement ) {
    if( aElement instanceof ITsNode ) {
      ITsNode n = (ITsNode)aElement;
      if( n.kind().canHaveChilds() ) {
        return !n.childs().isEmpty();
      }
    }
    return false;
  }

}
