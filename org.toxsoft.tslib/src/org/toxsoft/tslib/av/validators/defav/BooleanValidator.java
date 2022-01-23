package org.toxsoft.tslib.av.validators.defav;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.bricks.validator.ValidationResult;

/**
 * Валидатор типа {@link EAtomicType#BOOLEAN}.
 *
 * @author hazard157
 */
class BooleanValidator
    implements ITsValidator<IAtomicValue> {

  @SuppressWarnings( "unused" )
  private IOptionSet constraints;

  public BooleanValidator( IOptionSet aConstraints ) {
    constraints = aConstraints;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    return ValidationResult.SUCCESS;
  }
}
