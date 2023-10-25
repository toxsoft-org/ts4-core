package org.toxsoft.core.tsgui.ved.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * LM for {@link VedActorM5Model}.
 *
 * @author hazard157
 */
class VedActorM5LifecycleManager
    extends M5LifecycleManager<IVedActor, IVedScreen> {

  public VedActorM5LifecycleManager( IM5Model<IVedActor> aModel, IVedScreen aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  // TODO CRUD Actor

  @Override
  protected ValidationResult doBeforeRemove( IVedActor aEntity ) {
    return master().model().actors().svs().validator().canRemove( aEntity.id() );
  }

  @Override
  protected void doRemove( IVedActor aEntity ) {
    master().model().actors().remove( aEntity.id() );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  protected IList<IVedActor> doListEntities() {
    return (IList)master().model().actors().list();
  }

}
