package org.toxsoft.core.tsgui.ved.api.impl;

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

  private final EVedEntityKind kind;

  /**
   * Consytructor.
   *
   * @param aKind {@link EVedEntityKind} - entity kind
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEntityManagerEdit( EVedEntityKind aKind ) {
    TsNullArgumentRtException.checkNull( aKind );
    genericChangeEventer = new GenericChangeEventer( this );
    kind = aKind;
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
  public void insertEntity( int aIndex, T aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > items.size() );
    TsIllegalArgumentRtException.checkTrue( aEntity.entityKind() != kind );
    TsItemAlreadyExistsRtException.checkTrue( items.hasKey( aEntity.id() ) );
    items.insert( aIndex, aEntity );
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
