package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.m5.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GUI The pane contains list of VED model actors {@link IVedScreenModel#actors()}.
 *
 * @author hazard157
 */
public class VedPanelActorsList
    extends TsStdEventsProducerPanel<IVedActor> {

  private final IVedScreen vedScreen;

  private final IM5CollectionPanel<IVedActor> panel;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aVedScreen {@link IVedScreen} - the VED screen to display it's model's content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedPanelActorsList( Composite aParent, ITsGuiContext aContext, IVedScreen aVedScreen ) {
    super( aParent, aContext );
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    this.setLayout( new BorderLayout() );
    //
    IM5Model<IVedActor> model = m5().getModel( IVedM5Constants.MID_VED_ACTOR, IVedActor.class );
    IM5LifecycleManager<IVedActor> lm = model.getLifecycleManager( vedScreen );
    panel = model.panelCreator().createCollEditPanel( tsContext(), lm.itemsProvider(), lm );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.addTsSelectionListener( selectionChangeEventHelper );
    panel.addTsDoubleClickListener( doubleClickEventHelper );
    vedScreen.model().actors().eventer().addListener( this::onVedActorsListChange );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void onVedActorsListChange( IVedItemsManager<?> aSource, ECrudOp aOp, String aId ) {
    // TODO perform minimal action for not to change selection
    switch( aOp ) {
      case CREATE: {
        panel.refresh();
        break;
      }
      case EDIT: {
        IVedActor actor = vedScreen.model().actors().list().findByKey( aId );
        if( actor != null ) {
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
  public IVedActor selectedItem() {
    return panel.selectedItem();
  }

  @Override
  public void setSelectedItem( IVedActor aItem ) {
    panel.setSelectedItem( aItem );
  }

}
