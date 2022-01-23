package org.toxsoft.tslib.av.validators.defav;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.bricks.validator.ValidationResult;

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
