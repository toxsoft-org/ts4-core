package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.m5.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GUI The pane contains list of VED model VISELs {@link IVedScreenModel#visels()}.
 *
 * @author hazard157
 */
public class VedPanelViselsList
    extends TsStdEventsProducerPanel<IVedVisel> {

  private final IVedScreen vedScreen;

  private final IM5CollectionPanel<IVedVisel> panel;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aVedScreen {@link IVedScreen} - the VED screen to display it's model's content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedPanelViselsList( Composite aParent, ITsGuiContext aContext, IVedScreen aVedScreen ) {
    super( aParent, aContext );
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    this.setLayout( new BorderLayout() );
    //
    IM5Model<IVedVisel> model = m5().getModel( IVedM5Constants.MID_VED_VISEL, IVedVisel.class );
    IM5LifecycleManager<IVedVisel> lm = model.getLifecycleManager( vedScreen );

    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
    MultiPaneComponentModown<IVedVisel> mpc = new MultiPaneComponentModown<>( aContext, model, lm.itemsProvider(), lm );

    // FIXME temporary code
    TreeModeInfo<IVedVisel> tmi1 = new TreeModeInfo<>( "by1", "Name", "Descr", null, new By1TreeMaker() );
    mpc.treeModeManager().addTreeMode( tmi1 );
    //

    panel = new M5CollectionPanelMpcModownWrapper<>( mpc, false );

    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.addTsSelectionListener( selectionChangeEventHelper );
    panel.addTsDoubleClickListener( doubleClickEventHelper );
    vedScreen.model().visels().eventer().addListener( this::onVedViselsListChange );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void onVedViselsListChange( @SuppressWarnings( "unused" ) IVedItemsManager<?> aSource, ECrudOp aOp, String aId ) {
    // TODO perform minimal action for not to change selection
    switch( aOp ) {
      case CREATE: {
        panel.refresh();
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
        panel.refresh();
        break;
      }
      case REMOVE: {
        panel.refresh();
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
    return panel.selectedItem();
  }

  @Override
  public void setSelectedItem( IVedVisel aItem ) {
    panel.setSelectedItem( aItem );
  }

}
