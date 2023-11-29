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
 * LM for {@link VedViselM5Model}.
 *
 * @author hazard157
 */
class VedViselM5LifecycleManager
    extends M5LifecycleManager<IVedVisel, IVedScreen> {

  public VedViselM5LifecycleManager( IM5Model<IVedVisel> aModel, IVedScreen aMaster ) {
    super( aModel, false, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  // TODO CRUD Visel

  @Override
  protected IVedVisel doEdit( IM5Bunch<IVedVisel> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    VedAbstractVisel item = master().model().visels().list().getByKey( id );
    IOptionSetEdit props = new OptionSet();
    props.setValue( FID_NAME, aValues.getAsAv( FID_NAME ) );
    props.setValue( FID_DESCRIPTION, aValues.getAsAv( FID_DESCRIPTION ) );
    // HERE add more properties (if any defined in model) here
    item.props().setProps( props );
    return item;
  }

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

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  protected IListReorderer<IVedVisel> doGetItemsReorderer() {
    return (IListReorderer)master().model().visels().reorderer();
  }

}
