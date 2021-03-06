package org.toxsoft.core.tslib.bricks.validator.impl;

import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;

/**
 * {@link ITsValidator} implemetation always returning success.
 *
 * @author hazard157
 * @param <V> - checked entity class
 */
public final class PassTsValidator<V>
    implements ITsValidator<V> {

  @Override
  public ValidationResult validate( V aValue ) {
    return ValidationResult.SUCCESS;
  }

}
