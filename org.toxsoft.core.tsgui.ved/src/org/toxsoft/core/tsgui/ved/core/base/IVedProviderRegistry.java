package org.toxsoft.core.tsgui.ved.core.base;

import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The registry of VED entities providers.
 *
 * @author hazard157
 * @param <P> - type of the provider
 */
public interface IVedProviderRegistry<P extends IVedEntityProviderBase> {

  /**
   * Returns the registered providers.
   *
   * @return {@link IMap}&lt;{@link IdPair},P&gt; - map "providers ID" - "the provider"
   */
  IMap<IdPair, P> items();

  /**
   * Registers the provider.
   *
   * @param aProvider &lt;P&gt; - the provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException provider with the same PID is already registered
   * @throws ClassCastException the argument is not of class {@link #providerClass()}
   */
  void registerProvider( P aProvider );

  /**
   * Returns the class of providers.
   *
   * @return {@link Class}&lt;P&gt; - the class of providers
   */
  Class<P> providerClass();

}
