package org.toxsoft.core.tsgui.ved.api.entity;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The registry of available entities providers of the kind {@link #entityKind()}.
 *
 * @author hazard157
 */
public interface IVedEntityProvidersRegistry {

  /**
   * Returns the kind of registered providers.
   *
   * @return {@link EVedEntityKind} - the kind of entities
   */
  EVedEntityKind entityKind();

  /**
   * Returns the registered providers.
   *
   * @return {@link IStridablesList}&lt;{@link IVedEntityProvider}&gt; - the providers
   */
  IStridablesList<IVedEntityProvider> providers();

  /**
   * Registers the provider.
   *
   * @param aProvider {@link IVedEntityProvider} - the provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException provider is not of kind {@link #entityKind()}K
   * @throws TsItemAlreadyExistsRtException provider with the same ID is already registered
   */
  void registerProvider( IVedEntityProvider aProvider );

}
