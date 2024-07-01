package org.toxsoft.core.tslib.bricks.validator.impl;

import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * {@link ITsValidator} implementation always fails.
 *
 * @author hazard157
 * @param <V> - checked entity class
 */
public final class FailTsValidator<V>
    implements ITsValidator<V> {

  static final ValidationResult ERROR = ValidationResult.error( EValidationResultType.ERROR.description() );

  @Override
  public ValidationResult validate( V aValue ) {
    return ERROR;
  }

}
