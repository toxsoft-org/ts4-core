package org.toxsoft.core.tsgui.ved.tools.palette;

import org.eclipse.jface.action.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.extra.tools.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Palette of editor tools available through library manager {@link IVedEnvironment#libraryManager()}.
 * <p>
 * This is the simple implementation where all tools are visible at the same time.
 *
 * @author hazard157
 */
public class VedEditorToolsPalette
    extends TsPanel
    implements IVedContextable {

  private final IGenericChangeListener toolChangeListener = aSource -> updateOnToolChange();

  private final IVedEnvironment vedEnv;
  private final TsToolbar       toolbar;

  private IVedScreen vedScreen = null;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aEnvironment {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEditorToolsPalette( Composite aParent, IVedEnvironment aEnvironment ) {
    super( aParent, aEnvironment.tsContext() );
    vedEnv = aEnvironment;
    this.setLayout( new BorderLayout() );
    // toolbar
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    toolbar = new TsToolbar( ctx );
    for( IVedLibrary lib : vedEnv.libraryManager().listLibs() ) {
      for( IVedEditorToolProvider p : lib.toolProviders() ) {
        ITsActionDef acdef = new TsActionDef( p.id(), IAction.AS_CHECK_BOX, p.params() );
        toolbar.addActionDef( acdef );
      }
    }
    toolbar.setIconSize( EIconSize.IS_48X48 );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    // setup
    toolbar.addListener( this::processAction );
    vedEnv.screenManager().activeScreenChangeEventer().addListener( s -> updateOnScreenChange() );
    updateOnScreenChange();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void processAction( String aActionId ) {
    if( vedScreen != null ) {
      vedScreen.toolsManager().setActiveTool( aActionId );
    }
  }

  void updateOnScreenChange() {
    if( vedScreen != null ) {
      vedScreen.toolsManager().activeToolChangeEventer().removeListener( toolChangeListener );
    }
    vedScreen = vedEnv.screenManager().activeScreen();
    if( vedScreen != null ) {
      vedScreen.toolsManager().activeToolChangeEventer().addListener( toolChangeListener );
    }
    updateOnToolChange();
  }

  void updateOnToolChange() {
    String activeToolId = TsLibUtils.EMPTY_STRING;
    boolean enabled = false;
    if( vedScreen != null ) {
      activeToolId = vedScreen.toolsManager().activeToolId();
      enabled = true;
    }
    for( ITsActionDef adef : toolbar.listButtonItems() ) {
      boolean checked = adef.id().equals( activeToolId );
      toolbar.setActionChecked( adef.id(), checked );
      toolbar.setActionEnabled( adef.id(), enabled );
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedContextable
  //

  @Override
  public IVedEnvironment vedEnv() {
    return vedEnv;
  }

}
