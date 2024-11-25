package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.incub.drag.*;
import org.toxsoft.core.tsgui.ved.m5.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GUI The pane contains list of VED model VISELs {@link IVedScreenModel#visels()}.
 *
 * @author hazard157, vs
 */
public class VedPanelViselsList
    extends TsStdEventsProducerPanel<IVedVisel> {

  private final IVedScreen vedScreen;

  // private final IM5CollectionPanel<IVedVisel> panel;
  // MultiPaneComponentModown<IVedVisel> mpc;
  IMultiPaneComponent<IVedVisel> mpc;

  private final IM5TreeViewer<IVedVisel> treeViewer;

  private final IListReorderer<IVedVisel> listReorderer;

  /**
   * Constructor.
   * <p>
   * If <code>aTreeModes</code> is not an empty list, this widget will have tree support with specified modes.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aVedScreen {@link IVedScreen} - the VED screen to display it's model's content
   * @param aTreeModes {@link IStridablesList}&lt;{@link TreeModeInfo}&gt; - optional tree modes
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedPanelViselsList( Composite aParent, ITsGuiContext aContext, IVedScreen aVedScreen,
      IStridablesList<TreeModeInfo<IVedVisel>> aTreeModes ) {
    super( aParent, aContext );
    TsNullArgumentRtException.checkNull( aTreeModes );
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    this.setLayout( new BorderLayout() );
    //
    IM5Model<IVedVisel> model = m5().getModel( IVedM5Constants.MID_VED_VISEL, IVedVisel.class );
    IM5LifecycleManager<IVedVisel> lm = model.getLifecycleManager( vedScreen );
    listReorderer = lm.itemsProvider().reorderer();

    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_HIDE_PANES.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), avBool( !aTreeModes.isEmpty() ) );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( aContext.params(), AV_FALSE );
    OPDEF_IS_DETAILS_PANE.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_DETAILS_PANE_HIDDEN.setValue( aContext.params(), AV_TRUE );
    mpc = new MultiPaneComponentModown<>( aContext, model, lm.itemsProvider(), lm );
    for( TreeModeInfo<IVedVisel> tm : aTreeModes ) {
      mpc.treeModeManager().addTreeMode( tm );
    }
    mpc.treeModeManager().setCurrentMode( aTreeModes.first().id() );

    mpc.createControl( this );
    mpc.getControl().setLayoutData( BorderLayout.CENTER );
    mpc.addTsSelectionListener( selectionChangeEventHelper );
    mpc.addTsDoubleClickListener( doubleClickEventHelper );
    vedScreen.model().visels().eventer().addListener( this::onVedViselsListChange );

    treeViewer = mpc.tree();
  }

  private IM5TreeDragReorderer dragReorderer = null;

  M5TreeViewerReorderDragSupport<IVedVisel> dragSupport = null;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aVedScreen {@link IVedScreen} - the VED screen to display it's model's content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedPanelViselsList( Composite aParent, ITsGuiContext aContext, IVedScreen aVedScreen ) {
    this( aParent, aContext, aVedScreen, IStridablesList.EMPTY );
  }

  /**
   * Задает класс осуществляющий перестановку элементов в дереве, вызванную перетаскиванием элемента.
   *
   * @param aReorderer {@link IM5TreeDragReorderer} - перестановщик, м.б. <code>null</code> в этом случае удаляются все
   *          слушатели, связанные с перетаскиванием
   */
  public void setParentChildReorderer( IM5TreeDragReorderer aReorderer ) {
    dragReorderer = aReorderer;
    if( aReorderer == null ) {
      if( dragSupport != null ) {
        dragSupport.dispose();
      }
    }
    else {
      dragSupport = new M5TreeViewerReorderDragSupport<>( treeViewer );
      dragSupport.addReorderListener( ( aViewer, aSource, aTarget, aPlace ) -> {
        dragReorderer.reorder( treeViewer, listReorderer, aSource, aTarget, aPlace );
        mpc.refresh();
      } );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void onVedViselsListChange( @SuppressWarnings( "unused" ) IVedItemsManager<?> aSource, ECrudOp aOp, String aId ) {
    // TODO perform minimal action for not to change selection
    switch( aOp ) {
      case CREATE: {
        mpc.refresh();
        break;
      }
      case EDIT: {
        IVedVisel visel = vedScreen.model().visels().list().findByKey( aId );
        if( visel != null ) {
          // FIXME just update item in tree
        }
        break;
      }
      case LIST: {
        mpc.refresh();
        break;
      }
      case REMOVE: {
        mpc.refresh();
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public IVedVisel selectedItem() {
    return mpc.selectedItem();
  }

  @Override
  public void setSelectedItem( IVedVisel aItem ) {
    mpc.setSelectedItem( aItem );
  }

}
