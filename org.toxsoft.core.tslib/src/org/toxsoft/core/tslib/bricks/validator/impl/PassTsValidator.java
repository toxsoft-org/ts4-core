package org.toxsoft.core.tslib.bricks.validator.impl;

import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * {@link ITsValidator} implementation always returning success.
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
