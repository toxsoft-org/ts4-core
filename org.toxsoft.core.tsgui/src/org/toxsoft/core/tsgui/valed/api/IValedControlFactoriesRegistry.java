package org.toxsoft.core.tsgui.valed.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
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
   * If there is no factory with specified name, tries to create a factory instance assuming that <code>aName</code> is
   * {@link Class#getName()}. In case of success, the created factory will be registered and returned.
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

  /**
   * Chooses an editor factory based on atomic type and context parameters.
   * <p>
   * The argument <code>aEditorContext</code> must be the context used to create an editor instance. This method may
   * change context parameters and/or references.
   * <p>
   * Initially tries to create an editor as it is intended: using the factory name from option
   * {@link IValedControlConstants#OPID_EDITOR_FACTORY_NAME} if it is present in the context.
   * <p>
   * Otherwise tries to find suitable editor based on atomic type default
   * {@link ValedControlUtils#getDefaultFactory(EAtomicType)} and/or other options in context (such as
   * {@link IAvMetaConstants#TSID_KEEPER_ID} for {@link EAtomicType#VALOBJ}).
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @param aEditorContext {@link ITsGuiContext} - VALED creation context
   * @return {@link IValedControlFactory} - most suitable editor, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IValedControlFactory getSuitableAvEditor( EAtomicType aAtomicType, ITsGuiContext aEditorContext );

  /**
   * Chooses an editor factory based on solely on edited value class.
   * <p>
   * The argument <code>aEditorContext</code> must the context used to create an editor instance. This method may change
   * context parameters and/or references.
   *
   * @param aValueClass {@link Class} - the edited value class
   * @param aEditorContext {@link ITsGuiContext} - VALED creation context
   * @return {@link IValedControlFactory} - most suitable editor, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IValedControlFactory findSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext );

}
