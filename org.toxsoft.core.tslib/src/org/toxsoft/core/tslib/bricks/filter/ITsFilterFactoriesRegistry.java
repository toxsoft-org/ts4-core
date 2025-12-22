package org.toxsoft.core.tslib.bricks.filter;

import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Single filter factories registry.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public interface ITsFilterFactoriesRegistry<T>
    extends IStridablesRegisrty<ITsSingleFilterFactory<T>> {

  /**
   * Returns class of the objects accepted by the filters in this registry.
   * <p>
   * Each filter must allow objects of returned class or its descendants.
   *
   * @return {@link Class}&lt;T&gt; - accpeted objects class
   */
  Class<T> objectClass();

  /**
   * Registers factory.
   *
   * @param aFactory {@link ITsSingleFilterFactory} - factory to be registered
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException factory with the same identifier is already registered
   * @throws TsIllegalArgumentRtException factory and registry object classes are not compatibe
   */
  @Override
  void register( ITsSingleFilterFactory<T> aFactory );

  /**
   * Unregisters (removes) factory.
   *
   * @param aFactory {@link ITsSingleFilterFactory} - factory to be unregistered
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsUnsupportedFeatureRtException attempt to remove builtin factory
   */
  void unregister( ITsSingleFilterFactory<T> aFactory );

  /**
   * Creates filter for the specified parameters assuming all single filter factories are already registered..
   *
   * @param aParams {@link ITsCombiFilterParams} - the filter parameters
   * @param aCompleteEvaluation boolean - indicates if created filter must always evaluate whole expression
   * @return {@link ITsFilter}&lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException parameters contains unregistered single filter
   */
  ITsFilter<T> createFilter( ITsCombiFilterParams aParams, boolean aCompleteEvaluation );

  // ------------------------------------------------------------------------------------
  // inline methods

  /**
   * Creates filter for the specified parameters assuming all single filter factories are already registered..
   * <p>
   * Calls {@link #createFilter(ITsCombiFilterParams, boolean)} with argument <code>aCompleteEvaluation</code> =
   * <code>false</code>.
   *
   * @param aParams {@link ITsCombiFilterParams} - the filter parameters
   * @return {@link ITsFilter}&lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException parameters contains unregistered single filter
   */
  default ITsFilter<T> createFilter( ITsCombiFilterParams aParams ) {
    return createFilter( aParams, false );
  }

}
