package org.toxsoft.core.tsgui.ved.incub.drag;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Поддержка изменения порядка элементов M5 дерева с помощью операций "перетаскивания".
 *
 * @author vs
 * @param <T> - тип элемента коллекции
 */
public class M5TreeViewerReorderDragSupport<T> {

  private final IM5TreeViewer<T> viewer;

  class DragInfo {

    ECollectionDropPlace place = ECollectionDropPlace.NOTHING;

    ITsNode source;
    ITsNode target;
  }

  DragInfo dragInfo = null;

  private static final String DRAG_DATA = "ts.M5Tree"; //$NON-NLS-1$

  /**
   * Слушатель событий перетаскивания.
   *
   * @author vs
   */
  public class DragListener
      implements DragSourceListener {

    @Override
    public void dragFinished( DragSourceEvent event ) {
      dragInfo = null;
      // System.out.println( "Finshed Drag" );
    }

    @Override
    public void dragSetData( DragSourceEvent aEvent ) {
      ITsNode node = (ITsNode)viewer.console().selectedNode();
      if( node != null ) {
        dragInfo.source = node;
        aEvent.data = DRAG_DATA;
      }
    }

    @Override
    public void dragStart( DragSourceEvent event ) {
      dragInfo = new DragInfo();
      // System.out.println( "Start Drag" );
    }

  }

  public class DropListener
      extends ViewerDropAdapter {

    public DropListener( Viewer aViewer ) {
      super( aViewer );
    }

    @Override
    public void drop( DropTargetEvent event ) {
      int location = this.determineLocation( event );
      ITsNode target = (ITsNode)determineTarget( event );
      // String translatedLocation = "";
      ECollectionDropPlace dropPlace = switch( location ) {
        case 1 -> ECollectionDropPlace.BEFORE;
        case 2 -> ECollectionDropPlace.AFTER;
        case 3 -> ECollectionDropPlace.ON;
        case 4 -> ECollectionDropPlace.NOTHING;
        default -> throw new TsNotAllEnumsUsedRtException();
      };
      // System.out.println( translatedLocation );
      // System.out.println( "The drop was done on the element: " + target );
      dragInfo.target = target;
      dragInfo.place = dropPlace;
      fireReorderEvent();
    }

    @Override
    public boolean performDrop( Object data ) {
      // ContentProviderTree.INSTANCE.getModel().add( data.toString() );
      // viewer.setInput( ContentProviderTree.INSTANCE.getModel() );
      if( data.toString().equals( DRAG_DATA ) ) {
        return true;
      }
      return false;
    }

    @Override
    public boolean validateDrop( Object target, int operation, TransferData transferType ) {
      return true;
    }

  }

  private final IListEdit<IReorderByDragEventListener<T>> listeners = new ElemLinkedBundleList<>();

  private final DragSource dragSource;

  private final DropTarget dropTarget;

  private final DragListener dragListener = new DragListener();

  private final DropListener dropListener;

  /**
   * Constructor.
   *
   * @param aViewer {@link IM5CollectionViewer} - viewer
   */
  public M5TreeViewerReorderDragSupport( IM5TreeViewer<T> aViewer ) {
    viewer = aViewer;
    int operations = DND.DROP_COPY | DND.DROP_MOVE;
    Transfer[] transferTypes = { TextTransfer.getInstance() };

    Tree control = (Tree)aViewer.getControl();

    dragSource = new DragSource( control, operations );
    dragSource.setTransfer( transferTypes );
    dragSource.addDragListener( dragListener );

    dropTarget = new DropTarget( control, operations );
    dropTarget.setTransfer( transferTypes );
    dropListener = new DropListener( new TreeViewer( control ) );
    dropTarget.addDropListener( dropListener );

    // viewer.getViewer().addDropSupport( operations, transferTypes, new DropListener( viewer.getViewer() ) );
  }

  public void addReorderListener( IReorderByDragEventListener<T> aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  public void removeReorderListener( IReorderByDragEventListener<T> aListener ) {
    listeners.remove( aListener );
  }

  public void dispose() {
    dragSource.removeDragListener( dragListener );
    listeners.clear();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void fireReorderEvent() {
    for( IReorderByDragEventListener<T> l : listeners ) {
      l.onReorderRequest( viewer, dragInfo.source, dragInfo.target, dragInfo.place );
    }
  }

}
