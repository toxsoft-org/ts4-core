package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility and helper methods.
 *
 * @author hazard157
 */
public class VedScreenUtils {

  /**
   * Creates current configuration of the VED screen model.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link VedScreenCfg} - created instance of the configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static VedScreenCfg getVedScreenConfig( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    VedScreenCfg scrCfg = new VedScreenCfg();
    IVedScreenModel sm = aVedScreen.model();
    for( VedAbstractVisel item : sm.visels().list() ) {
      VedItemCfg cfg = VedItemCfg.ofVisel( item.id(), item.factoryId(), item.params(), item.props() );
      scrCfg.viselCfgs().add( cfg );
    }
    for( VedAbstractActor item : sm.actors().list() ) {
      VedItemCfg cfg = VedItemCfg.ofActor( item.id(), item.factoryId(), item.params(), item.props() );
      scrCfg.actorCfgs().add( cfg );
    }
    scrCfg.canvasCfg().copyFrom( aVedScreen.view().canvasConfig() );
    return scrCfg;
  }

  /**
   * Sets configuration to the VED screen.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @param aScreenCfg {@link IVedScreenCfg} - configuration to be applied to the screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void setVedScreenConfig( IVedScreen aVedScreen, IVedScreenCfg aScreenCfg ) {
    TsNullArgumentRtException.checkNulls( aVedScreen, aScreenCfg );
    IVedScreenModel sm = aVedScreen.model();
    try {
      sm.actors().eventer().pauseFiring();
      sm.visels().eventer().pauseFiring();
      sm.actors().clear();
      sm.visels().clear();
      for( IVedItemCfg cfg : aScreenCfg.viselCfgs() ) {
        sm.visels().create( cfg );
      }
      for( IVedItemCfg cfg : aScreenCfg.actorCfgs() ) {
        sm.actors().create( cfg );
      }
      aVedScreen.view().setCanvasConfig( aScreenCfg.canvasCfg() );
    }
    finally {
      sm.visels().eventer().resumeFiring( true );
      sm.actors().eventer().resumeFiring( true );
    }
  }

  /**
   * No subclasses.
   */
  private VedScreenUtils() {
    // nop
  }

}
