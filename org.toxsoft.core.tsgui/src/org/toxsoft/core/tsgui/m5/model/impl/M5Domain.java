package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5Domain} implementation
 *
 * @author hazard157
 */
class M5Domain
    extends Stridable
    implements IM5Domain {

  private final IStridablesListEdit<IM5Model<?>> models = new StridablesList<>();

  private final ITsGuiContext tsContext;
  private final M5Domain      parent;

  M5Domain( String aId, M5Domain aParent, ITsGuiContext aContext ) {
    super( aId );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    parent = aParent;
  }

  M5Domain( String aId, ITsGuiContext aContext ) {
    super( aId );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    parent = null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private IM5Model<?> doFindModel( String aModelId ) {
    IM5Model<?> foundModel = models.findByKey( aModelId );
    if( foundModel == null && parent != null ) {
      foundModel = parent.doFindModel( aModelId );
    }
    return foundModel;
  }

  // ------------------------------------------------------------------------------------
  // IM5Domain
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  @Override
  public IStridablesList<IM5Model<?>> models() {
    if( parent == null ) {
      return models;
    }
    IStridablesListEdit<IM5Model<?>> ll = new StridablesList<>();
    ll.addAll( parent.models() );
    ll.addAll( models );
    return ll;
  }

  @Override
  public IStridablesList<IM5Model<?>> selfModels() {
    return models;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public <T> IM5Model<T> findModel( String aModelId ) {
    return (M5Model<T>)doFindModel( aModelId );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public <T> IM5Model<T> getModel( String aModelId, Class<T> aEntityClass ) {
    TsNullArgumentRtException.checkNull( aEntityClass );
    IM5Model<?> foundModel = doFindModel( aModelId );
    if( foundModel == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MODEL_ID_IN_DOMAIN, id(), aModelId );
    }
    if( !aEntityClass.isAssignableFrom( foundModel.entityClass() ) ) {
      throw new ClassCastException( String.format( FMT_ERR_CANT_USE_MODEL_FOR_ENTITY_CLASS, foundModel.id(),
          foundModel.entityClass().getSimpleName(), aEntityClass.getSimpleName() ) );
    }
    return (M5Model<T>)foundModel;
  }

  @Override
  public <T> M5Model<T> addModel( M5Model<T> aModel ) {
    TsNullArgumentRtException.checkNull( aModel );
    if( models.hasKey( aModel.id() ) ) {
      throw new TsItemAlreadyExistsRtException( FMT_ERR_MODEL_ID_ALREADY_IN_DOMAIN, id(), aModel.id() );
    }
    aModel.papiSetDomain( this );
    models.add( aModel );
    return aModel;
  }

  @Override
  public <T> M5Model<T> replaceModel( M5Model<T> aModel ) {
    models.put( aModel );
    aModel.papiSetDomain( this );
    return aModel;
  }

  @Override
  public void removeModel( String aModelId ) {
    models.removeByKey( aModelId );
  }

  @Override
  public <T> M5Model<T> initTemporaryModel( M5Model<T> aModel ) {
    TsNullArgumentRtException.checkNull( aModel );
    if( models.hasKey( aModel.id() ) ) {
      throw new TsItemAlreadyExistsRtException( FMT_ERR_MODEL_ID_ALREADY_IN_DOMAIN, id(), aModel.id() );
    }
    aModel.papiSetDomain( this );
    return aModel;
  }

  @Override
  public IM5Domain parent() {
    return parent;
  }

  @Override
  public IM5Domain createChildDomain( String aId, ITsGuiContext aContext ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aContext );
    IM5Domain child = new M5Domain( aId, this, aContext );
    return child;
  }

}
