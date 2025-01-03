package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.bricks.validator.std.*;
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

  /**
   * {@link IVedItemsManager#svs()} implementation.
   *
   * @author hazard157
   */
  static class Svs
      extends AbstractTsValidationSupport<IVedItemsManagerValidator>
      implements IVedItemsManagerValidator {

    @Override
    public IVedItemsManagerValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canCreate( int aIndex, IVedItemCfg aCfg ) {
      TsNullArgumentRtException.checkNull( aCfg );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IVedItemsManagerValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canCreate( aIndex, aCfg ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemove( String aId ) {
      TsNullArgumentRtException.checkNull( aId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IVedItemsManagerValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemove( aId ) );
      }
      return vr;
    }

  }

  /**
   * {@link IVedItemsManager#eventer()} implementation.
   *
   * @author hazard157
   */
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

  /**
   * Built-in validation rules.
   */
  private final IVedItemsManagerValidator builtinValidator = new IVedItemsManagerValidator() {

    @SuppressWarnings( "boxing" )
    @Override
    public ValidationResult canCreate( int aIndex, IVedItemCfg aCfg ) {
      // check for index validity
      if( aIndex < 0 || aIndex > list().size() ) {
        return ValidationResult.error( FMT_ERR_VED_ITEM_CREATION_INV_INDEX, aIndex, list().size() );
      }
      // check if same ID already exists
      if( list().hasKey( aCfg.id() ) ) {
        return ValidationResult.error( FMT_ERR_VED_ITEM_CREATION_DUP_ID, aCfg.id() );
      }
      // check if factory ID is not registered
      if( doFindFactory( aCfg ) == null ) {
        return ValidationResult.error( FMT_ERR_VED_ITEM_UNKNOWN_FACTORY, aCfg.factoryId() );
      }
      // warn of unassigned name
      ValidationResult vr = NameStringValidator.VALIDATOR.validate( aCfg.nmName() );
      if( !vr.isOk() ) {
        return vr;
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemove( String aId ) {
      // warn if no such item exists
      if( !list().hasKey( aId ) ) {
        return ValidationResult.warn( FMT_WARN_CANT_REMOVE_ABSENT_ITEM, aId );
      }
      return ValidationResult.SUCCESS;
    }

  };

  private final IStridablesListEdit<T>      itemsList = new StridablesList<>();
  private final IStridablesListReorderer<T> reorderer;

  private final Svs       svs = new Svs();
  private final Eventer   eventer;
  private final VedScreen vedScreen;

  AbstractVedItemsManager( VedScreen aScreen ) {
    vedScreen = aScreen;
    eventer = new Eventer( this );
    INotifierStridablesListEdit<T> tmpList = new NotifierStridablesListEditWrapper<>( itemsList );
    tmpList.addCollectionChangeListener( ( sec, op, item ) -> eventer.fireEvent( op, (String)item ) );
    reorderer = new StridablesListReorderer<>( tmpList );
    svs.addValidator( builtinValidator );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private boolean hasItemWithName( String aName ) {
    for( IVedItem item : itemsList ) {
      if( item.nmName().equals( aName ) ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( itemsList.isEmpty() ) {
      return;
    }
    while( !itemsList.isEmpty() ) {
      T item = itemsList.removeByIndex( 0 );
      item.dispose();
    }
    eventer.fireEvent( ECrudOp.LIST, null );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    while( !itemsList.isEmpty() ) {
      T item = itemsList.removeByIndex( 0 );
      item.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedItemsManager
  //

  @Override
  public IStridablesList<T> list() {
    return itemsList;
  }

  @Override
  public IStridablesListReorderer<T> reorderer() {
    return reorderer;
  }

  @Override
  public VedItemCfg prepareFromTemplate( IVedItemCfg aTemplateCfg ) {
    TsNullArgumentRtException.checkNull( aTemplateCfg );
    IVedItemFactoryBase<T> factory = doFindFactory( aTemplateCfg );
    TsItemNotFoundRtException.checkNull( factory );
    // generate ID
    String id;
    int counter = 0;
    String prefix = StridUtils.getLast( aTemplateCfg.factoryId() );
    prefix = prefix.toLowerCase().substring( 0, 1 ) + prefix.substring( 1 ); // convert first char to lower case
    String name;
    do {
      id = prefix + Integer.toString( ++counter ); // "prefixNN"
      name = factory.nmName() + ' ' + Integer.toString( counter ); // "Factory name NN"
    } while( itemsList.hasKey( id ) || hasItemWithName( name ) );
    // create config
    VedItemCfg cfg = new VedItemCfg( id, aTemplateCfg );
    cfg.propValues().setStr( DDEF_NAME, name );
    return cfg;
  }

  @Override
  public T create( int aIndex, IVedItemCfg aCfg ) {
    TsValidationFailedRtException.checkError( svs.canCreate( aIndex, aCfg ) );
    IVedItemFactoryBase<T> factory = doFindFactory( aCfg );
    T item = factory.create( aCfg, vedScreen );
    TsInternalErrorRtException.checkNull( item );
    item.props().propsEventer().addListener( ( src, news, olds ) -> {
      if( itemsList.hasKey( item.id() ) ) {
        eventer.fireEvent( ECrudOp.EDIT, item.id() );
      }
      else {
        throw new TsInternalErrorRtException(); // just in case someone is working with removed item
      }
    } );
    itemsList.add( item );
    eventer.fireEvent( ECrudOp.CREATE, item.id() );
    return item;
  }

  @Override
  public void remove( String aId ) {
    TsValidationFailedRtException.checkError( svs.canRemove( aId ) );
    T item = itemsList.removeById( aId );
    if( item != null ) {
      eventer.fireEvent( ECrudOp.REMOVE, aId );
      item.dispose();
    }
  }

  @Override
  public ITsValidationSupport<IVedItemsManagerValidator> svs() {
    return svs;
  }

  @Override
  public ITsEventer<IVedItemsManagerListener<T>> eventer() {
    return eventer;
  }

  @Override
  public void informOnModelChange( ECrudOp aOp, String aItemId ) {
    TsNullArgumentRtException.checkNull( aOp );
    switch( aOp ) {
      case EDIT: {
        TsNullArgumentRtException.checkNull( aItemId );
        eventer.fireEvent( aOp, aItemId );
        break;
      }
      case LIST: {
        eventer.fireEvent( aOp, null );
        break;
      }
      case CREATE:
      case REMOVE: {
        throw new TsIllegalArgumentRtException();
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aOp.id() );
    }

    // TODO Auto-generated method stub

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
