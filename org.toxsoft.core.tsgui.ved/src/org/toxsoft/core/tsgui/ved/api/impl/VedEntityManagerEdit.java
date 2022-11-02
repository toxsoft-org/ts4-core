package org.toxsoft.core.tsgui.ved.api.impl;

import static org.toxsoft.core.tsgui.ved.api.impl.ITsResources.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEntityManagerEdit} implementation.
 *
 * @author hazard157
 * @param <T> - entities Java type
 */
class VedEntityManagerEdit<T extends IVedEntity>
    implements IVedEntityManagerEdit<T>, ICloseable {

  private final GenericChangeEventer   genericChangeEventer;
  private final IStridablesListEdit<T> itemsListSource = new StridablesList<>();
  private final IListReorderer<T>      reorderer;

  private final INotifierStridablesListEdit<T> items;

  private final EVedEntityKind  kind;
  private final IVedEnvironment vedEnv;

  /**
   * Constructor.
   *
   * @param aKind {@link EVedEntityKind} - entity kind
   * @param aVedEnv {@link IVedEnvironment} - the VED environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEntityManagerEdit( EVedEntityKind aKind, IVedEnvironment aVedEnv ) {
    TsNullArgumentRtException.checkNulls( aKind, aVedEnv );
    genericChangeEventer = new GenericChangeEventer( this );
    kind = aKind;
    vedEnv = aVedEnv;
    items = new NotifierStridablesListEditWrapper<>( itemsListSource );
    items.addCollectionChangeListener( genericChangeEventer );
    reorderer = new ListReorderer<T, IStridablesListEdit<T>>( items );
  }

  // ------------------------------------------------------------------------------------
  // IVedEntityManager
  //

  @Override
  public EVedEntityKind entityKind() {
    return kind;
  }

  @Override
  public INotifierStridablesList<T> items() {
    return items;
  }

  // ------------------------------------------------------------------------------------
  // IVedEntityManagerEdit
  //

  @Override
  public void insertEntity( int aIndex, IVedEntityConfig aEntityCfg ) {
    TsNullArgumentRtException.checkNull( aEntityCfg );
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > items.size() );
    TsIllegalArgumentRtException.checkTrue( aEntityCfg.entityKind() != kind );
    TsItemAlreadyExistsRtException.checkTrue( items.hasKey( aEntityCfg.id() ) );
    IVedEntityProvidersRegistry registry = vedEnv.vedFramework().getEntityProvidersRegistry( kind );
    IVedEntityProvider provider = registry.providers().findByKey( aEntityCfg.providerId() );
    if( provider == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_ENTITY_PROVIDER, kind.nmName(), aEntityCfg.providerId() );
    }
    @SuppressWarnings( "unchecked" )
    T entity = (T)kind.entityClass().cast( provider.create( aEntityCfg, vedEnv ) );
    TsInternalErrorRtException.checkNull( entity );
    items.insert( aIndex, entity );
    entity.genericChangeEventer().addListener( genericChangeEventer );
  }

  @Override
  public void removeEntity( String aEntityId ) {
    items.removeById( aEntityId );
  }

  @Override
  public IListReorderer<T> itemsReorderer() {
    return reorderer;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    items.clear();
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    genericChangeEventer.clearListenersList();
    items.removeCollectionChangeListener( genericChangeEventer );
    clear();
  }

}
