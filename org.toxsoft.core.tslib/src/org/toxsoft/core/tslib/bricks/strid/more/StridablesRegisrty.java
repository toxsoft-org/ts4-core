package org.toxsoft.core.tslib.bricks.strid.more;

import static org.toxsoft.core.tslib.bricks.strid.more.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStridablesRegisrty} implementation.
 *
 * @author hazard157
 * @param <T> - type of the items in registry
 */
public class StridablesRegisrty<T extends IStridable>
    implements IStridablesRegisrty<T> {

  class ValidationSupport
      extends AbstractTsValidationSupport<IStridablesRegisrtyValidator<T>>
      implements IStridablesRegisrtyValidator<T> {

    @Override
    public IStridablesRegisrtyValidator<T> validator() {
      return this;
    }

    @Override
    public ValidationResult canRegister( T aItem ) {
      TsNullArgumentRtException.checkNull( aItem );
      ValidationResult vr = builtinValidator.canRegister( aItem );
      if( vr.isError() ) {
        return vr;
      }
      for( IStridablesRegisrtyValidator<T> v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRegister( aItem ) );
        if( vr.isError() ) {
          return vr;
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canUnregister( String aId ) {
      TsNullArgumentRtException.checkNull( aId );
      ValidationResult vr = builtinValidator.canUnregister( aId );
      if( vr.isError() ) {
        return vr;
      }
      for( IStridablesRegisrtyValidator<T> v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canUnregister( aId ) );
        if( vr.isError() ) {
          return vr;
        }
      }
      return vr;
    }

  }

  private IStridablesRegisrtyValidator<T> builtinValidator = new IStridablesRegisrtyValidator<>() {

    @Override
    public ValidationResult canRegister( T aItem ) {
      if( items.hasKey( aItem.id() ) ) {
        return ValidationResult.error( FMT_ERR_ITEM_ID_ALREAY_REGISTERED, aItem.id() );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canUnregister( String aId ) {
      if( builtinItems.hasKey( aId ) ) {
        return ValidationResult.error( FMT_ERR_CANT_UNREG_BUILTIN_ITEM, aId );
      }
      if( !items.hasKey( aId ) ) {
        return ValidationResult.error( FMT_ERR_CANT_UNREG_UNEXISTING_ITEM, aId );
      }
      return ValidationResult.SUCCESS;
    }
  };

  private final ValidationSupport      svs          = new ValidationSupport();
  private final IStridablesListEdit<T> items        = new StridablesList<>();
  private final IStridablesListEdit<T> builtinItems = new StridablesList<>();
  private final Class<? extends T>     itemClass;

  /**
   * Constructor.
   *
   * @param aItemClass {@link Class}&lt;T&gt; - items class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StridablesRegisrty( Class<? extends T> aItemClass ) {
    TsNullArgumentRtException.checkNull( aItemClass );
    TsIllegalArgumentRtException.checkFalse( IStridable.class.isAssignableFrom( aItemClass ) );
    itemClass = aItemClass;
  }

  // ------------------------------------------------------------------------------------
  //

  @Override
  public Class<? extends T> itemClass() {
    return itemClass;
  }

  @Override
  public IStridablesList<T> items() {
    return items;
  }

  @Override
  public T get( String aId ) {
    return items.getByKey( aId );
  }

  @Override
  public T find( String aId ) {
    return items.findByKey( aId );
  }

  @Override
  public boolean has( String aId ) {
    return items.hasKey( aId );
  }

  @Override
  public boolean isBuiltin( String aId ) {
    return builtinItems.hasKey( aId );
  }

  @Override
  public void register( T aItem ) {
    TsValidationFailedRtException.checkError( svs().validator().canRegister( aItem ) );
    items.add( aItem );
  }

  @Override
  public void unregister( String aId ) {
    TsValidationFailedRtException.checkError( svs().validator().canUnregister( aId ) );
    items.removeById( aId );
  }

  @Override
  public ITsValidationSupport<IStridablesRegisrtyValidator<T>> svs() {
    return svs;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    items.clear();
    items.putAll( builtinItems );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Registers an item as builtin item.
   *
   * @param aItem &lt;T&gt; - the item to be registered as builtin one
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException item ID is already registered as builtin item
   * @throws TsIllegalStateRtException there is an item ID akready regsietred as non-buitlin item
   */
  public void registerBuiltin( T aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    TsItemAlreadyExistsRtException.checkTrue( builtinItems.hasKey( aItem.id() ) );
    TsIllegalStateRtException.checkTrue( items.hasKey( aItem.id() ) );
    builtinItems.add( aItem );
    items.add( aItem );
  }

}
