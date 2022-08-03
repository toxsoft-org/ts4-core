package org.toxsoft.core.tsgui.ved.glib.content;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel displayes components in the data model {@link IVedDataModel#listComponents()}.
 *
 * @author hazard157
 */
public class VedContentPanel
    extends TsPanel
    implements IVedContextable, ITsSelectionChangeListener<IVedComponent> {

  private final ITsCollectionChangeListener canvasConfigChangeListener = ( aSource, aOp, aItem ) -> {
    this.compsPanel.refresh();
  };

  private final ITsCollectionChangeListener componentsListChangeListener = ( aSource, aOp, aItem ) -> {
    switch( aOp ) {
      case CREATE:
      case REMOVE:
      case LIST:
        this.compsPanel.refresh();
        break;
      case EDIT:
        break;
      default:
        throw new TsNotAllEnumsUsedRtException( aOp.id() );
    }

  };

  IM5CollectionPanel<IVedComponent> compsPanel;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedContentPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    IM5Model<IVedComponent> compsModel = m5().getModel( VedComponentM5Model.MODEL_ID, IVedComponent.class );
    IM5LifecycleManager<IVedComponent> lm = compsModel.getLifecycleManager( vedEnv().dataModel() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().setBool( OPDEF_IS_TOOLBAR.id(), false );
    ctx.params().setValobj( OPDEF_NODE_ICON_SIZE.id(), LIST_ICON_SIZE );
    compsPanel = compsModel.panelCreator().createCollViewerPanel( ctx, lm.itemsProvider() );
    compsPanel.createControl( this );
    compsPanel.getControl().setLayoutData( BorderLayout.CENTER );
    vedEnv().dataModel().canvasConfig().addCollectionChangeListener( canvasConfigChangeListener );
    vedEnv().dataModel().listComponents().addCollectionChangeListener( componentsListChangeListener );
    compsPanel.addTsSelectionListener( this::onTsSelectionChanged );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // public void refresh() {
  // compsPanel.refresh();
  // }

  // ------------------------------------------------------------------------------------
  // ITsSelectionChangeListener
  //

  @Override
  public void onTsSelectionChanged( Object aSource, IVedComponent aSel ) {
    IVedScreen screen = vedEnv().screenManager().activeScreen();
    if( screen != null && aSel != null ) {
      screen.selectionManager().setSelectedComponentView( screen.listViews().getByKey( aSel.id() ) );
    }
  }

}
