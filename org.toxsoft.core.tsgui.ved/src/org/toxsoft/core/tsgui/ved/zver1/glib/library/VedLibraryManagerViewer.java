package org.toxsoft.core.tsgui.ved.zver1.glib.library;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.zver1.core.*;
import org.toxsoft.core.tsgui.ved.zver1.core.library.*;
import org.toxsoft.core.tsgui.ved.zver1.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Shows library manager {@link IVedEnvironment#libraryManager()} contents.
 *
 * @author hazard157
 */
public class VedLibraryManagerViewer
    extends TsPanel
    implements IVedContextable {

  private final IM5CollectionPanel<IVedLibrary>           libsPanel;
  private final IM5CollectionPanel<IVedComponentProvider> compsPanel;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedLibraryManagerViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.VERTICAL );
    sfMain.setLayoutData( BorderLayout.CENTER );
    // libsPanel
    IM5Model<IVedLibrary> libsModel = m5().getModel( VedLibraryM5Model.MODEL_ID, IVedLibrary.class );
    IVedLibraryManager libMan = vedEnv().libraryManager();
    IM5LifecycleManager<IVedLibrary> libsLm = libsModel.getLifecycleManager( libMan );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().setBool( OPDEF_IS_TOOLBAR.id(), false );
    ctx.params().setValobj( OPDEF_NODE_ICON_SIZE.id(), LIST_ICON_SIZE );
    libsPanel = libsModel.panelCreator().createCollViewerPanel( ctx, libsLm.itemsProvider() );
    libsPanel.createControl( sfMain );
    libsPanel.addTsSelectionListener( ( src, sel ) -> whenLibsSelectionChanged( sel ) );
    // compsPanel
    IM5Model<IVedComponentProvider> compsModel =
        m5().getModel( VedComponentProviderM5Model.MODEL_ID, IVedComponentProvider.class );
    ctx = new TsGuiContext( tsContext() );
    ctx.params().setBool( OPDEF_IS_TOOLBAR.id(), false );
    ctx.params().setValobj( OPDEF_NODE_ICON_SIZE.id(), LIST_ICON_SIZE );
    compsPanel = compsModel.panelCreator().createCollViewerPanel( ctx, null );
    compsPanel.createControl( sfMain );
    whenLibsSelectionChanged( null );
  }

  private void whenLibsSelectionChanged( IVedLibrary aSel ) {
    IM5ItemsProvider<IVedComponentProvider> ip = null;
    if( aSel != null ) {
      IM5Model<IVedComponentProvider> compsModel =
          m5().getModel( VedComponentProviderM5Model.MODEL_ID, IVedComponentProvider.class );
      IM5LifecycleManager<IVedComponentProvider> lm = compsModel.getLifecycleManager( aSel );
      ip = lm.itemsProvider();
    }
    compsPanel.setItemsProvider( ip );
    compsPanel.refresh();
  }

}
