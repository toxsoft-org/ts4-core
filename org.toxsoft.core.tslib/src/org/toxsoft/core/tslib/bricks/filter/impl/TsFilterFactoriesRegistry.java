package org.toxsoft.core.tslib.bricks.filter.impl;

import static org.toxsoft.core.tslib.bricks.filter.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.filter.impl.TsFilterUtils.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsFilterFactoriesRegistry} implementation.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public final class TsFilterFactoriesRegistry<T>
    extends StridablesRegisrty<ITsSingleFilterFactory<T>>
    implements ITsFilterFactoriesRegistry<T> {

  private final Class<T> objectClass;

  /**
   * Constructor.
   *
   * @param aObjClass {@link Class}&lt;T&gt; - accpeted objects class
   * @param aFactories {@link IStridablesList}&lt;{@link ITsSingleFilterFactory}&gt; - initial list of factories
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsValidationFailedRtException factory from list can't be registered
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public TsFilterFactoriesRegistry( Class<T> aObjClass, IStridablesList<ITsSingleFilterFactory<T>> aFactories ) {
    super( (Class)ITsSingleFilterFactory.class );
    TsNullArgumentRtException.checkNull( aObjClass );
    objectClass = aObjClass;
    registerBuiltin( (ITsSingleFilterFactory)NONE_FILTER_FACTORY );
    registerBuiltin( (ITsSingleFilterFactory)ALL_FILTER_FACTORY );
    svs().addValidator( aItem -> {
      if( !aItem.objectClass().isAssignableFrom( objectClass ) ) {
        return ValidationResult.error( FMT_ERR_INV_OBJ_CLASS, aItem.id(), aItem.objectClass().getName(),
            objectClass.getName() );
      }
      return ValidationResult.SUCCESS;
    } );
    for( ITsSingleFilterFactory<T> f : aFactories ) {
      register( f );
    }
  }

  /**
   * Constructor.
   *
   * @param aObjClass {@link Class}&lt;T&gt; - accepted objects class
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public TsFilterFactoriesRegistry( Class<T> aObjClass ) {
    this( aObjClass, IStridablesList.EMPTY );
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
