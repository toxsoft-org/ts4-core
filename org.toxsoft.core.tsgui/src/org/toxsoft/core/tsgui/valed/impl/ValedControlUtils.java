package org.toxsoft.core.tsgui.valed.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods to work with VALEDs.
 *
 * @author hazard157
 */
public class ValedControlUtils {

  /**
   * Helper method to crate atomic value VALED.
   * <p>
   * Usage: TODO ???
   * <p>
   * <b>Important:</b> argument <code>aContext</code> must be the new instance created for the VALED. Content of the
   * context may be changed by the method.
   *
   * @param aDataType {@link IDataType} - the data type to edit
   * @param aContext {@link ITsGuiContext} - the editor context
   * @return {@link IValedControl} - atomic value editor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsRuntimeException VALED can ot be created
   */
  public static IValedControl<IAtomicValue> createAvValedControl( IDataType aDataType, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aDataType, aContext );
    aContext.params().addAll( aDataType.params() );
    IValedControlFactoriesRegistry vcfReg = aContext.get( IValedControlFactoriesRegistry.class );
    IValedControlFactory f = vcfReg.getSuitableAvEditor( aDataType.atomicType(), aContext );
    return f.createEditor( aContext );
  }

  /**
   * Returns the default factory name for the specified atomic type.
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @return String - name of the editor factory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String getDefaultFactoryName( EAtomicType aAtomicType ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    return getDefaultFactory( aAtomicType ).factoryName();
  }

  /**
   * Returns the default factory for the specified atomic type.
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @return {@link AbstractValedControlFactory} - the factory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static AbstractValedControlFactory getDefaultFactory( EAtomicType aAtomicType ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    return switch( aAtomicType ) {
      case NONE -> ValedAvAnytypeText.FACTORY;
      case BOOLEAN -> ValedAvBooleanCheck.FACTORY;
      case INTEGER -> ValedAvIntegerSpinner.FACTORY;
      case FLOATING -> ValedAvFloatSpinner.FACTORY;
      case TIMESTAMP -> ValedAvTimestampMpv.FACTORY;
      case STRING -> ValedAvStringText.FACTORY;
      case VALOBJ -> ValedAvValobjRoText.FACTORY;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  /**
   * No subclasses.
   */
  private ValedControlUtils() {
    // nop
  }

}
