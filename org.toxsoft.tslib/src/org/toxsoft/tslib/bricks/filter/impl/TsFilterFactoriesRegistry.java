package org.toxsoft.tslib.bricks.filter.impl;

import static org.toxsoft.tslib.bricks.filter.impl.ITsResources.*;
import static org.toxsoft.tslib.bricks.filter.impl.TsFilterUtils.*;

import org.toxsoft.tslib.bricks.filter.ITsFilterFactoriesRegistry;
import org.toxsoft.tslib.bricks.filter.ITsSingleFilterFactory;
import org.toxsoft.tslib.bricks.strid.more.IStridablesRegisrtyValidator;
import org.toxsoft.tslib.bricks.strid.more.StridablesRegisrty;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link ITsFilterFactoriesRegistry} implementation.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public class TsFilterFactoriesRegistry<T>
    extends StridablesRegisrty<ITsSingleFilterFactory<T>>
    implements ITsFilterFactoriesRegistry<T> {

  private final Class<T> objectClass;

  /**
   * Constructor.
   *
   * @param aObjClass {@link Class}&lt;T&gt; - accpeted objects class
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public TsFilterFactoriesRegistry( Class<T> aObjClass ) {
    super( (Class)ITsSingleFilterFactory.class );
    TsNullArgumentRtException.checkNull( aObjClass );
    objectClass = aObjClass;
    registerBuiltin( (ITsSingleFilterFactory)NONE_FILTER_FACTORY );
    registerBuiltin( (ITsSingleFilterFactory)ALL_FILTER_FACTORY );
    svs().addValidator( new IStridablesRegisrtyValidator<ITsSingleFilterFactory<T>>() {

      @Override
      public ValidationResult canRegister( ITsSingleFilterFactory<T> aItem ) {
        if( !aItem.objectClass().isAssignableFrom( objectClass ) ) {
          return ValidationResult.error( FMT_ERR_INV_OBJ_CLASS, aItem.id(), aItem.objectClass().getName(),
              objectClass.getName() );
        }
        return ValidationResult.SUCCESS;
      }
    } );
  }

  @Override
  public Class<T> objectClass() {
    return objectClass;
  }

  @Override
  public void register( ITsSingleFilterFactory<T> aFactory ) {
    TsNullArgumentRtException.checkNull( aFactory );
    super.register( aFactory );
  }

  @Override
  public void unregister( ITsSingleFilterFactory<T> aFactory ) {
    TsNullArgumentRtException.checkNull( aFactory );
    super.unregister( aFactory.id() );
  }

}
