package org.toxsoft.core.tsgui.ved.runtime;

import static org.toxsoft.core.tsgui.ved.runtime.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IVedRuntimePanel} implementation.
 *
 * @author hazard157
 */
public class VedRutimePanel
    extends AbstractLazyPanel<Control>
    implements IVedRuntimePanel {

  private final VedEnvironment vedEnv;

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
    vedEnv = new VedEnvironment( tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalSetActorMethodsCalling( boolean aEnable ) {
    // TODO VedRutimePanel.internalSetActorMethodsCalling()
  }

  private void internalDisposeScreen() {
    // TODO remove VISELs from VED nevironment and dispose them
    // TODO remove actors from VED nevironment and dispose them

    // TODO VedRutimePanel.internalDisposeCreen()
  }

  /**
   * Initializes the panel content from {@link #vedScreenCfg} assuming that screen is cleared.
   */
  private void internalInitScreen() {
    // create the VISELs and add to the VED environment
    IVedViselFactoriesRegistry vfReg = tsContext().get( IVedViselFactoriesRegistry.class );
    for( IVedItemCfg cfg : vedScreenCfg.viselCfgs() ) {
      IVedViselFactory factory = vfReg.find( cfg.factoryId() );
      if( factory != null ) {
        try {
          VedAbstractVisel visel = factory.create( cfg, vedEnv );
          vedEnv.viselsList().add( visel );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex, FMT_ERR_CAN_CREATE_VISEL, ex.getMessage() );
        }
      }
      else {
        LoggerUtils.errorLogger().warning( FMT_WARN_UNKNON_VISEL_FACTORY, cfg.factoryId() );
      }
    }
    // create the actors and add to the VED environment
    IVedActorFactoriesRegistry afReg = tsContext().get( IVedActorFactoriesRegistry.class );
    for( IVedItemCfg cfg : vedScreenCfg.actorCfgs() ) {
      IVedActorFactory factory = afReg.find( cfg.factoryId() );
      if( factory != null ) {
        try {
          VedAbstractActor actor = factory.create( cfg, vedEnv );
          vedEnv.actorsList().add( actor );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex, FMT_ERR_CAN_CREATE_ACTOR, ex.getMessage() );
        }
      }
      else {
        LoggerUtils.errorLogger().warning( FMT_WARN_UNKNON_ACTOR_FACTORY, cfg.factoryId() );
      }
    }

    // TODO what else?

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
