package org.toxsoft.core.tsgui.valed.impl;

import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods to work with VALEDs.
 *
 * @author hazard157
 */
public class ValedControlUtils {

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
    switch( aAtomicType ) {
      case NONE:
        return ValedAvAnytypeText.FACTORY;
      case BOOLEAN:
        return ValedAvBooleanCheck.FACTORY;
      case INTEGER:
        return ValedAvIntegerSpinner.FACTORY;
      case FLOATING:
        return ValedAvFloatSpinner.FACTORY;
      case TIMESTAMP:
        return ValedAvTimestampMpv.FACTORY;
      case STRING:
        return ValedAvStringText.FACTORY;
      case VALOBJ:
        return ValedAvValobjRoText.FACTORY;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * No subclasses.
   */
  private ValedControlUtils() {
    // nop
  }

}
