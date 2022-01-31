package org.toxsoft.core.tsgui.valed.api;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The registry of factories {@link IValedControlFactory},
 * <p>
 * Usually there is the one registry in application and all Valeds must be registered there.
 *
 * @author hazard157
 */
public interface IValedControlFactoriesRegistry {

  /**
   * Determines factory with specified name is registered.
   *
   * @param aName String - factory name
   * @return boolean - <code>true</code> if factory with soecified name is registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean hasFactory( String aName );

  /**
   * Registers the factory.
   *
   * @param aFactory {@link IValedControlFactory} - the factory to be registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException factory with the same name is already registered
   */
  void registerFactory( IValedControlFactory aFactory );

  /**
   * Finds the factory by name.
   * <p>
   * If there is no factory with specified name, nethod tries to create a factory instance assuming that
   * <code>aName</code> is {@link Class#getName()}. In case of success, the created factory will be registered and
   * returned.
   *
   * @param aName String - factory name
   * @return {@link IValedControlFactory} - the found factory or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IValedControlFactory findFactory( String aName );

  /**
   * Returns the factory by name.
   * <p>
   * Just adds <code>null</code> return value check to the {@link #findFactory(String)}.
   *
   * @param aName String - factory name
   * @return {@link IValedControlFactory} - the found factory or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such factoy found
   */
  default IValedControlFactory getFactory( String aName ) {
    IValedControlFactory f = findFactory( aName );
    TsItemNotFoundRtException.checkNull( f );
    return f;
  }

}
