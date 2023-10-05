package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;

import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedItemsManager} implementation.
 *
 * @author hazard157
 * @param <T> - the type of the VED items
 */
abstract class AbstractVedItemsManager<T extends VedAbstractItem>
    implements IVedItemsManager<T> {

  class Eventer
      extends AbstractSimpleCrudOpTsEventer<IVedItemsManagerListener<T>, String, IVedItemsManager<T>> {

    public Eventer( IVedItemsManager<T> aSource ) {
      super( aSource );
    }

    @Override
    protected void doReallyFireEvent( IVedItemsManagerListener<T> aListener, IVedItemsManager<T> aSource, ECrudOp aOp,
        String aItem ) {
      aListener.onListChange( aSource, aOp, aItem );
    }

  }

  private final IStridablesListEdit<T> activeList = new StridablesList<>();
  private final IStridablesListEdit<T> allList    = new StridablesList<>();
  private final IListReorderer<T>      reorderer;

  private final Eventer   eventer;
  private final VedScreen screen;

  AbstractVedItemsManager( VedScreen aScreen ) {
    screen = aScreen;
    reorderer = new ListReorderer<>( allList );
    eventer = new Eventer( this );
  }

  // ------------------------------------------------------------------------------------
  // IVedItemsManager
  //

  @Override
  public IStridablesList<T> list() {
    return activeList;
  }

  @Override
  public IStridablesList<T> listAllItems() {
    return allList;
  }

  @Override
  public IListReorderer<T> reorderer() {
    return reorderer;
  }

  @Override
  public T create( int aIndex, IVedItemCfg aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    TsErrorUtils.checkCollIndex( allList.size(), aIndex );
    IVedItemFactoryBase<T> factory = doFindFactory( aCfg );
    TsItemNotFoundRtException.checkNull( factory, FMT_WARN_UNKNON_ITEM_FACTORY, aCfg.factoryId() );
    T item = factory.create( aCfg, screen );
    TsInternalErrorRtException.checkNull( item );
    item.props().propsEventer()
        .addListener( ( src, pId, oldVal, newVal ) -> eventer.fireEvent( ECrudOp.EDIT, item.id() ) );
    allList.add( item );
    if( item.isActive() ) {
      activeList.add( item );
    }
    return item;
  }

  @Override
  public void remove( String aId ) {
    T item = allList.removeById( aId );
    if( item != null ) {
      activeList.remove( item );
      item.dispose();
    }
  }

  @Override
  public ITsEventer<IVedItemsManagerListener<T>> eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract IVedItemFactoryBase<T> doFindFactory( IVedItemCfg aCfg );

}
