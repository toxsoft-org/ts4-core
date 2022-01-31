package org.toxsoft.core.tslib.av.validators.defav;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;

/**
 * Валидатор типа {@link EAtomicType#NONE}.
 *
 * @author hazard157
 */
class NoneValidator
    implements ITsValidator<IAtomicValue> {

  @SuppressWarnings( "unused" )
  private IOptionSet constraints;

  public NoneValidator( IOptionSet aConstraints ) {
    constraints = aConstraints;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    return ValidationResult.SUCCESS;
  }
}
