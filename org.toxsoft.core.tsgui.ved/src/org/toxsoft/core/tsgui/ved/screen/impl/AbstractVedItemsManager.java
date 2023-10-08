package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;

import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedItemsManager} implementation.
 *
 * @author hazard157
 * @param <T> - the type of the VED items
 */
abstract class AbstractVedItemsManager<T extends VedAbstractItem>
    implements IVedItemsManager<T>, ICloseable {

  class Eventer
      extends AbstractSimpleCrudOpTsEventer<IVedItemsManagerListener<T>, String, IVedItemsManager<T>> {

    public Eventer( IVedItemsManager<T> aSource ) {
      super( aSource );
    }

    @Override
    protected void doReallyFireEvent( IVedItemsManagerListener<T> aListener, IVedItemsManager<T> aSource, ECrudOp aOp,
        String aItem ) {
      aListener.onVedItemsListChange( aSource, aOp, aItem );
    }

  }

  private final IStridablesListEdit<T> activeList = new StridablesList<>();
  private final IStridablesListEdit<T> allList    = new StridablesList<>();
  private final IListReorderer<T>      reorderer;

  private final Eventer   activeItemsEventer;
  private final Eventer   allItemsEventer;
  private final VedScreen screen;

  AbstractVedItemsManager( VedScreen aScreen ) {
    screen = aScreen;
    reorderer = new ListReorderer<>( allList );
    activeItemsEventer = new Eventer( this );
    allItemsEventer = new Eventer( this );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    if( allList.isEmpty() ) {
      return;
    }
    while( !allList.isEmpty() ) {
      T item = allList.removeByIndex( 0 );
      item.dispose();
    }
    if( !activeList.isEmpty() ) {
      activeList.clear();
      activeItemsEventer.fireEvent( ECrudOp.LIST, null );
    }
    allItemsEventer.fireEvent( ECrudOp.LIST, null );
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
    if( allList.hasKey( aCfg.id() ) ) {
      throw new TsItemAlreadyExistsRtException( FMT_ERR_ITEM_ALREADY_EXISTS, aCfg.id() );
    }
    IVedItemFactoryBase<T> factory = doFindFactory( aCfg );
    TsItemNotFoundRtException.checkNull( factory, FMT_WARN_UNKNON_ITEM_FACTORY, aCfg.factoryId() );
    T item = factory.create( aCfg, screen );
    TsInternalErrorRtException.checkNull( item );
    item.props().propsEventer().addListener( ( src, news, olds ) -> {
      if( allList.hasKey( item.id() ) ) {
        allItemsEventer.fireEvent( ECrudOp.EDIT, item.id() );
        if( activeList.hasKey( item.id() ) ) {
          activeItemsEventer.fireEvent( ECrudOp.EDIT, item.id() );
        }
      }
      else {
        throw new TsInternalErrorRtException();
      }
    } );
    allList.add( item );
    allItemsEventer.fireEvent( ECrudOp.CREATE, item.id() );
    if( item.isActive() ) {
      activeList.add( item );
      activeItemsEventer.fireEvent( ECrudOp.CREATE, item.id() );
    }
    return item;
  }

  @Override
  public void remove( String aId ) {
    T item = allList.removeById( aId );
    if( item != null ) {
      if( activeList.remove( item ) >= 0 ) {
        activeItemsEventer.fireEvent( ECrudOp.REMOVE, aId );
      }
      allItemsEventer.fireEvent( ECrudOp.REMOVE, aId );
      item.dispose();
    }
  }

  @Override
  public ITsEventer<IVedItemsManagerListener<T>> activeItemsEventer() {
    return activeItemsEventer;
  }

  @Override
  public ITsEventer<IVedItemsManagerListener<T>> allItemsEventer() {
    return allItemsEventer;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must find factory in the appropriate registry.
   *
   * @param aCfg {@link IVedItemCfg} - configuration of the item to be created
   * @return {@link IVedItemFactoryBase} - the found factory or <code>null</code>
   */
  protected abstract IVedItemFactoryBase<T> doFindFactory( IVedItemCfg aCfg );

}
