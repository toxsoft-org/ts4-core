package org.toxsoft.core.tsgui.ved.runtime;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedRuntimePanel} implementation.
 *
 * @author hazard157
 */
public class VedRutimePanel
    extends AbstractLazyPanel<Control>
    implements IVedRuntimePanel {

  private Canvas        theCanvas    = null;
  private boolean       paused       = false;
  private IVedScreenCfg vedScreenCfg = IVedScreenCfg.NONE;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedRutimePanel( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalSetActorMethodsCalling( boolean aEnable ) {
    // TODO VedRutimePanel.internalSetActorMethodsCalling()
  }

  private void internalDisposeScreen() {
    // TODO VedRutimePanel.internalDisposeCreen()
  }

  private void internalInitScreen() {
    // TODO VedRutimePanel.internalInitScreen()
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Canvas doCreateControl( Composite aParent ) {
    theCanvas = new Canvas( aParent, SWT.NONE );

    // TODO Auto-generated method stub

    return theCanvas;
  }

  // ------------------------------------------------------------------------------------
  // IPausableAnimation

  //

  @Override
  public boolean isPaused() {
    return paused;
  }

  @Override
  public void pause() {
    if( !isPaused() ) {
      paused = true;
      internalSetActorMethodsCalling( !paused );
    }
  }

  @Override
  public void resume() {
    if( isPaused() ) {
      paused = false;
      internalSetActorMethodsCalling( !paused );
    }
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    internalDisposeScreen();
  }

  // ------------------------------------------------------------------------------------
  // IVedRuntimePanel
  //

  @Override
  public IVedScreenCfg getVedScreenCfg() {
    return vedScreenCfg;
  }

  @Override
  public void setVedScreenCfg( IVedScreenCfg aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    vedScreenCfg = aCfg;
    internalDisposeScreen();
    internalInitScreen();
  }

}
