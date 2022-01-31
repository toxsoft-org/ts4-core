package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.impl.ITsResources.*;

import java.lang.reflect.InvocationTargetException;

import org.toxsoft.core.tsgui.valed.api.IValedControlFactoriesRegistry;
import org.toxsoft.core.tsgui.valed.api.IValedControlFactory;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.ValedEnumCombo;
import org.toxsoft.core.tsgui.valed.controls.time.*;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * {@link IValedControlFactoriesRegistry} implementation.
 *
 * @author hazard157
 */
public class ValedControlFactoriesRegistry
    implements IValedControlFactoriesRegistry {

  /**
   * Карта фабрик редакторов.
   */
  private final IStringMapEdit<IValedControlFactory> factoriesMap = new StringMap<>();

  /**
   * Constructor.
   * <p>
   * Registers all known Valeds from <code>org.toxsoft.core.valed.std.*</code> package.
   */
  public ValedControlFactoriesRegistry() {
    registerFactory( ValedEnumCombo.FACTORY );
    registerFactory( ValedBooleanCheck.FACTORY );
    registerFactory( ValedIntegerSpinner.FACTORY );
    registerFactory( ValedIntegerText.FACTORY );
    registerFactory( ValedDoubleSpinner.FACTORY );
    registerFactory( ValedStringText.FACTORY );
    registerFactory( ValedTimestampMpv.FACTORY );
    registerFactory( ValedLocalTimeMpv.FACTORY );
    registerFactory( ValedLocalDateMpv.FACTORY );
    registerFactory( ValedLocalDateTimeMpv.FACTORY );
    registerFactory( ValedSecsDurationMpv.FACTORY );
    registerFactory( ValedComboSelector.FACTORY );
    // av
    registerFactory( ValedAvBooleanCheck.FACTORY );
    registerFactory( ValedAvIntegerSpinner.FACTORY );
    registerFactory( ValedAvIntegerText.FACTORY );
    registerFactory( ValedAvIntegerSecsDurationMpv.FACTORY );
    registerFactory( ValedAvFloatSpinner.FACTORY );
    registerFactory( ValedAvStringText.FACTORY );
    registerFactory( ValedAvTimestampMpv.FACTORY );
    registerFactory( ValedAvValobjRoText.FACTORY );
    registerFactory( ValedAvValobjEnumCombo.FACTORY );
    registerFactory( ValedAvValobjLocalTimeMpv.FACTORY );
    registerFactory( ValedAvValobjLocalDateMpv.FACTORY );
    registerFactory( ValedAvValobjLocalDateTimeMpv.FACTORY );
  }

  // ------------------------------------------------------------------------------------
  // IValedControlFactoriesRegistry
  //

  @Override
  public boolean hasFactory( String aName ) {
    return factoriesMap.hasKey( aName );
  }

  @Override
  public void registerFactory( IValedControlFactory aFactory ) {
    TsNullArgumentRtException.checkNull( aFactory );
    TsItemAlreadyExistsRtException.checkTrue( factoriesMap.hasKey( aFactory.factoryName() ) );
    factoriesMap.put( aFactory.getClass().getName(), aFactory );
  }

  @Override
  public IValedControlFactory findFactory( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    // is registered?
    IValedControlFactory f = factoriesMap.findByKey( aName );
    if( f != null ) {
      return f;
    }
    // try to interpret aName as factory class name
    Class<?> rawFactoryClass = null;
    try {
      rawFactoryClass = Class.forName( aName );
    }
    catch( @SuppressWarnings( "unused" ) ClassNotFoundException ex ) {
      return null; // aName is not a class name, exception is ignored
    }
    // probably factory class is loaded, trying to create factory instance
    Object rawFactoryInstance = null;
    try {
      rawFactoryInstance = rawFactoryClass.getDeclaredConstructor().newInstance();
    }
    catch( InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException ex ) {
      LoggerUtils.errorLogger().warning( ex, FMT_WARN_CANT_CREATE_CLASS_INSTANCE, aName );
      return null;
    }
    // check if instance is a factory
    if( !(rawFactoryInstance instanceof IValedControlFactory) ) {
      LoggerUtils.errorLogger().warning( FMT_WARN_INSTANCE_NOT_FACTORY, aName );
      return null;
    }
    f = (IValedControlFactory)rawFactoryInstance;
    factoriesMap.put( aName, f );
    return f;
  }

}
