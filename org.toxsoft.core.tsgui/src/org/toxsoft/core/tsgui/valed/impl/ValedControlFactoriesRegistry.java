package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.impl.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.lang.reflect.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.valed.controls.time.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IValedControlFactoriesRegistry} implementation.
 *
 * @author hazard157
 */
public class ValedControlFactoriesRegistry
    implements IValedControlFactoriesRegistry {

  /**
   * The factories map "factory name" - "the factory"..
   */
  private final IStringMapEdit<AbstractValedControlFactory> factoriesMap = new StringMap<>();

  /**
   * Constructor.
   * <p>
   * Registers all known VALEDs from <code>org.toxsoft.core.valed.std.*</code> package.
   */
  public ValedControlFactoriesRegistry() {
    /**
     * Editor factories registration order: more concrete first, most general - last.
     */
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
    registerFactory( ValedSimpleFontInfo.FACTORY );
    registerFactory( ValedSimpleRgb.FACTORY );
    registerFactory( ValedSimpleRgba.FACTORY );
    registerFactory( ValedTsLineInfo.FACTORY );
    registerFactory( ValedTsBorderInfo.FACTORY );
    registerFactory( ValedEnumCombo.FACTORY );
    registerFactory( ValedTsImageDescriptor.FACTORY );
    registerFactory( ValedTsImageFillInfo.FACTORY );
    registerFactory( ValedD2Angle.FACTORY );
    registerFactory( ValedD2Point.FACTORY );
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
    registerFactory( ValedAvValobjSimpleFontInfo.FACTORY );
    registerFactory( ValedAvValobjSimpleRgb.FACTORY );
    registerFactory( ValedAvValobjSimpleRgba.FACTORY );
    registerFactory( ValedAvValobjTsLineInfo.FACTORY );
    registerFactory( ValedAvValobjTsBorderInfo.FACTORY );
    registerFactory( ValedAvValobjD2Angle.FACTORY );
    registerFactory( ValedAvValobjD2Point.FACTORY );

    registerFactory( ValedAvValobjTsImageFillInfo.FACTORY );
    registerFactory( ValedAvValobjTsFillInfo.FACTORY );
    registerFactory( ValedOptionSet.FACTORY );
    registerFactory( ValedAvValobjTsImageDescriptor.FACTORY );
    registerFactory( ValedAvValobjTsGradientFillInfo.FACTORY );

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
    AbstractValedControlFactory f = AbstractValedControlFactory.class.cast( aFactory );
    factoriesMap.put( aFactory.factoryName(), f );
  }

  @Override
  public IValedControlFactory findFactory( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    // is registered?
    AbstractValedControlFactory f = factoriesMap.findByKey( aName );
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
    f = (AbstractValedControlFactory)rawFactoryInstance;
    factoriesMap.put( aName, f );
    return f;
  }

  @Override
  public IValedControlFactory getSuitableAvEditor( EAtomicType aAtomicType, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aAtomicType, aContext );
    IValedControlFactory factory = null;
    // straight way to get editor - take it from OPID_EDITOR_FACTORY_NAME option
    IAtomicValue avFacName = IAtomicValue.NULL;
    if( aContext.params().hasKey( OPID_EDITOR_FACTORY_NAME ) ) {
      avFacName = aContext.params().getValue( OPID_EDITOR_FACTORY_NAME );
    }
    if( avFacName != IAtomicValue.NULL ) {
      factory = findFactory( avFacName.asString() );
    }
    // straight way not works, now search through registered editors
    if( factory == null ) {
      String keeperId = aContext.params().getStr( TSID_KEEPER_ID, null );
      if( keeperId != null ) {
        for( AbstractValedControlFactory f : factoriesMap ) {
          if( f.isSuitableAvEditor( aAtomicType, keeperId, aContext ) ) {
            factory = f;
            break;
          }
        }
      }
    }
    // suitable factory not found - using very generic editor depending on atomic type of edited value
    if( factory == null ) {
      factory = ValedControlUtils.getDefaultFactory( aAtomicType );
    }
    return factory;
  }

  @Override
  public IValedControlFactory findSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aValueClass, aContext );
    // IAtomicValue must be processed by #getSuitableAvEditor()
    if( IAtomicValue.class.isAssignableFrom( aValueClass ) ) {
      return null;
    }
    // search through registered editors
    for( AbstractValedControlFactory f : factoriesMap ) {
      if( f.isSuitableRawEditor( aValueClass, aContext ) ) {
        return f;
      }
    }
    // nothing can be done...
    return null;
  }

}
