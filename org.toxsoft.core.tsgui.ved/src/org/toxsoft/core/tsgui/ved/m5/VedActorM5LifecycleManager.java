package org.toxsoft.core.tsgui.ved.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * LM for {@link VedActorM5Model}.
 *
 * @author hazard157
 */
class VedActorM5LifecycleManager
    extends M5LifecycleManager<IVedActor, IVedScreen> {

  public VedActorM5LifecycleManager( IM5Model<IVedActor> aModel, IVedScreen aMaster ) {
    super( aModel, false, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  // TODO CRUD Actor

  @Override
  protected IVedActor doEdit( IM5Bunch<IVedActor> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    VedAbstractActor item = master().model().actors().list().getByKey( id );
    IOptionSetEdit props = new OptionSet();
    props.setValue( FID_NAME, aValues.getAsAv( FID_NAME ) );
    props.setValue( FID_DESCRIPTION, aValues.getAsAv( FID_DESCRIPTION ) );
    // HERE add more properties (if any defined in model) here
    item.props().setProps( props );
    return item;
  }

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

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  protected IListReorderer<IVedActor> doGetItemsReorderer() {
    return (IListReorderer)master().model().actors().reorderer();
  }

}
