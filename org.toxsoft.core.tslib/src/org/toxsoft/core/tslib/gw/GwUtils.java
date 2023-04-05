package org.toxsoft.core.tslib.gw;

import static org.toxsoft.core.tslib.gw.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Green world utility methods.
 *
 * @author hazard157
 */
public final class GwUtils {

  /**
   * GWID canonical representation validator for values packed as {@link String}.
   * <p>
   * Validator throws an {@link TsNullArgumentRtException} for <code>null</code> arguments.
   */
  public static final ITsValidator<String> GWID_STR_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    try {
      if( aValue.isBlank() ) {
        return ValidationResult.error( MSG_ERR_GWID_STR_EMPTY );
      }
      Gwid.of( aValue );
      return ValidationResult.SUCCESS;
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      return ValidationResult.error( MSG_ERR_INV_GWID_FORMAT );
    }
  };

  /**
   * GWID canonical representation validator for values packed as {@link IAtomicValue} of type
   * {@link EAtomicType#STRING}.
   * <p>
   * Validator throws an {@link TsNullArgumentRtException} for <code>null</code> arguments.
   * <p>
   * For arguments of type other than {@link EAtomicType#STRING STRING} and {@link EAtomicType#NONE} returns error. NONE
   * type value {@link IAtomicValue#NULL} is considered as an empty string and also returns error.
   */
  public static final ITsValidator<IAtomicValue> GWID_STR_AV_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue == IAtomicValue.NULL || aValue.atomicType() == EAtomicType.NONE ) {
      return ValidationResult.error( MSG_ERR_GWID_STR_EMPTY );
    }
    if( aValue.atomicType() != EAtomicType.STRING ) {
      return ValidationResult.error( MSG_ERR_INV_GWID_AV );
    }
    return GWID_STR_VALIDATOR.validate( aValue.asString() );
  };

  /**
   * Запрет на создание экземпляров.
   */
  private GwUtils() {
    // nop
  }

}
