package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.toxsoft.core.tsgui.bricks.tstree.ITsTreeViewerConsole;
import org.toxsoft.core.tsgui.utils.jface.ViewerPaintHelper;
import org.toxsoft.core.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.core.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link ITsTreeViewerConsole} по умолчанию.
 *
 * @author hazard157
 */
public class TsTreeViewerConsole
    implements ITsTreeViewerConsole {

  private final TreeViewer        treeViewer;
  private final Tree              tree;
  private ViewerPaintHelper<Tree> paintHelper = null;

  /**
   * Конструктор с привязкой в JFace дереву.
   *
   * @param aTreeViewer {@link TreeViewer} - JFace дерево
   * @throws TsNullArgumentRtException любой = null
   */
  public TsTreeViewerConsole( TreeViewer aTreeViewer ) {
    TsNullArgumentRtException.checkNull( aTreeViewer );
    treeViewer = aTreeViewer;
    tree = aTreeViewer.getTree();
    tree.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent aE ) {
        onDispose();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Задает текущий выбранный элемент.
   * <p>
   * Внимание: выбор происходит по ссылке на элемент дерева {@link TreeItem}, а не сущности в узде &lt;T&gt;.
   *
   * @param aNode {@link TreeItem} - выбираемый элемент дерева
   */
  private void internalSelectItem( TreeItem aNode ) {
    ISelection selection = null;
    if( aNode != null ) {
      selection = new StructuredSelection( aNode.getData() );
    }
    treeViewer.setSelection( selection, true );
  }

  /**
   * Возвращает следующую/предыдущую сестру запрошенного узла.
   *
   * @param aSelItem {@link TreeItem} - запрошенный узел
   * @param aNext boolean - признак поиска следующего (а не предыдущего) узла
   * @return {@link TreeItem} - айденный узел или null, если запрошен последний/первый узел среди сестер
   */
  private TreeItem internalFindSibling( TreeItem aSelItem, boolean aNext ) {
    // найдем сестер
    TreeItem parentItem = aSelItem.getParentItem();
    TreeItem[] siblings;
    if( parentItem != null ) {
      siblings = parentItem.getItems();
    }
    else {
      siblings = tree.getItems();
    }
    // найдем индекс среди сестер
    int selIndexInParent = -1; // индекс выбранного элемента в родителе
    for( int i = 0; i < siblings.length; i++ ) {
      if( aSelItem.equals( siblings[i] ) ) {
        selIndexInParent = i;
        break;
      }
    }
    TsInternalErrorRtException.checkTrue( selIndexInParent < 0 );
    // вернем следующую сестру
    if( aNext ) {
      if( selIndexInParent < siblings.length - 1 ) {
        return siblings[selIndexInParent + 1];
      }
    }
    else {
      if( selIndexInParent > 0 ) {
        return siblings[selIndexInParent - 1];
      }
    }
    return null;
  }

  /**
   * Возвращает последнего наследника запрошенного узла.
   *
   * @param aSelItem {@link TreeItem} - запрошенный узел или null для последнего элемента всего дерева
   * @return {@link TreeItem} - последний (самый глубокий нследник) или сам запрошенный узел, если он бездетный
   */
  private TreeItem internalFindLastDescendant( TreeItem aSelItem ) {
    if( aSelItem == null ) {
      treeViewer.expandToLevel( 1 );
      int rootsCount = tree.getItemCount();
      if( rootsCount != 0 ) {
        return internalFindLastDescendant( tree.getItem( rootsCount - 1 ) );
      }
      return null;
    }
    if( !aSelItem.getExpanded() ) {
      treeViewer.expandToLevel( aSelItem.getData(), 1 );
    }
    int count = aSelItem.getItemCount();
    if( count == 0 ) {
      return aSelItem;
    }
    TreeItem lastItem = aSelItem.getItem( count - 1 );
    return internalFindLastDescendant( lastItem );
  }

  void onDispose() {
    if( paintHelper != null ) {
      paintHelper.deinstall();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsTreeViewerConsole
  //

  @Override
  public void expandNode( Object aNode ) {
    if( aNode != null ) {
      treeViewer.expandToLevel( aNode, AbstractTreeViewer.ALL_LEVELS );
    }
  }

  @Override
  public void expandNodeTo( Object aNode, int aLevel ) {
    if( aNode != null ) {
      treeViewer.expandToLevel( aNode, aLevel );
    }
  }

  @Override
  public void expandTreeTo( int aLevel ) {
    treeViewer.expandToLevel( aLevel );
  }

  @Override
  public void expandAll() {
    treeViewer.expandAll();
  }

  @Override
  public void collapseNode( Object aNode ) {
    if( aNode != null ) {
      treeViewer.collapseToLevel( aNode, AbstractTreeViewer.ALL_LEVELS );
    }
  }

  @Override
  public void collapseAll() {
    treeViewer.collapseAll();
  }

  @Override
  public void refresh( Object aNode ) {
    if( aNode != null ) {
      treeViewer.refresh( aNode, true );
    }
    else {
      treeViewer.refresh( true );
    }
  }

  @Override
  public void updateNode( Object aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    treeViewer.update( aNode, null );
  }

  @Override
  public Object selectedNode() {
    IStructuredSelection ss = (IStructuredSelection)treeViewer.getSelection();
    if( ss.isEmpty() ) {
      return null;
    }
    return ss.getFirstElement();
  }

  @Override
  public void setSelectedNode( Object aItem ) {
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aItem != null ) {
      selection = new StructuredSelection( aItem );
    }
    treeViewer.setSelection( selection, true );
  }

  @Override
  public boolean selectPrev( boolean aOnlySiblings ) {
    TreeItem[] selection = tree.getSelection();
    // если нет выбранного, выбираем последний элемент
    if( selection == null || selection.length == 0 ) {
      TreeItem lastItem = internalFindLastDescendant( null );
      internalSelectItem( lastItem );
      return lastItem != null;
    }
    TreeItem selItem = selection[0];
    // ищем предыдущую сестру
    TreeItem found = internalFindSibling( selItem, false );
    if( found != null ) {
      // выбьерем последний элемент предыдущей сестры
      internalSelectItem( internalFindLastDescendant( found ) );
      return true;
    }
    if( aOnlySiblings ) {
      return false;
    }
    // текущй узел первый среди сестер, выберем родителя
    TreeItem parentItem = selItem.getParentItem();
    if( parentItem != null ) {
      internalSelectItem( parentItem );
      return true;
    }
    // текущий узел первый среди корневых узлов, выберем последний элемент дерева
    internalSelectItem( internalFindLastDescendant( null ) );
    return true;
  }

  @Override
  public boolean selectNext( boolean aOnlySiblings ) {
    TreeItem[] selection = tree.getSelection();
    // если нет выбранного, выбираем первый элемент
    if( selection == null || selection.length == 0 ) {
      treeViewer.expandToLevel( 1 );
      if( tree.getItemCount() > 0 ) {
        internalSelectItem( tree.getItem( 0 ) );
        return true;
      }
      return false;
    }
    TreeItem selItem = selection[0];
    // ищем первого ребенка, если нужно
    if( !aOnlySiblings ) {
      if( !selItem.getExpanded() ) {
        treeViewer.expandToLevel( selItem.getData(), 1 );
      }
      int childsCount = selItem.getItemCount();
      if( childsCount > 0 ) {
        internalSelectItem( selItem.getItem( 0 ) );
        return true;
      }
    }
    // ищем следущую сестру начиная с себя и в родительской иерархии узлов
    TreeItem found = null;
    while( found == null ) {
      found = internalFindSibling( selItem, true );
      selItem = selItem.getParentItem();
      // если дошли до корня, значит выбран последний элемент дерева, выбираем первый
      if( selItem == null ) {
        found = tree.getItem( 0 );
        break;
      }
    }
    internalSelectItem( found );
    return true;
  }

  @Override
  public void installPaintHelper( ViewerPaintHelper<Tree> aPaintHelper ) {
    if( paintHelper != null ) {
      paintHelper.deinstall();
    }
    paintHelper = aPaintHelper;
    if( paintHelper != null ) {
      paintHelper.install( tree );
    }
  }

  @Override
  public void reveal( Object aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    treeViewer.reveal( aNode );
  }

  @Override
  public ITsPoint getTreeArea() {
    Rectangle r = treeViewer.getTree().getClientArea();
    return new TsPoint( r.width, r.height );
  }

  @Override
  public int getColumnsCount() {
    return treeViewer.getTree().getColumnCount();
  }

  @Override
  public int getColumnWidth( int aColumnIndex ) {
    TsIllegalArgumentRtException.checkTrue( aColumnIndex < 0 || aColumnIndex >= getColumnsCount() );
    return treeViewer.getTree().getColumn( aColumnIndex ).getWidth();
  }

}
