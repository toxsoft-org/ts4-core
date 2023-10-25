package org.toxsoft.core.tsgui.ved.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * LM for {@link VedViselM5Model}.
 *
 * @author hazard157
 */
class VedViselM5LifecycleManager
    extends M5LifecycleManager<IVedVisel, IVedScreen> {

  public VedViselM5LifecycleManager( IM5Model<IVedVisel> aModel, IVedScreen aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  // TODO CRUD Visel

  @Override
  protected ValidationResult doBeforeRemove( IVedVisel aEntity ) {
    return master().model().visels().svs().validator().canRemove( aEntity.id() );
  }

  @Override
  public void remove( IVedVisel aEntity ) {
    master().model().visels().remove( aEntity.id() );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  protected IList<IVedVisel> doListEntities() {
    return (IList)master().model().visels().list();
  }

}
