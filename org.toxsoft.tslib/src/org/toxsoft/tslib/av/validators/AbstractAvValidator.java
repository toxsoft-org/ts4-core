package org.toxsoft.tslib.av.validators;

import static org.toxsoft.tslib.av.validators.ITsResources.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Base class of all {@link ITsValidator} implemenations for values of type {@link IAtomicValue}.
 * <p>
 * Checks argument of {@link #validate(IAtomicValue)} against expected atomic type. Also throws an
 * {@link TsNullArgumentRtException} if argument is <code>null</code>.
 * <p>
 * The {@link IAtomicValue#NULL} argument is always validated.
 *
 * @author hazard157
 */
public abstract class AbstractAvValidator
    implements ITsValidator<IAtomicValue> {

  private final EAtomicType expectedType;

  /**
   * Constructor for subclasses.
   *
   * @param aExpectedType {@link EAtomicType} - expected atomic type, {@link EAtomicType#NONE} for any types
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractAvValidator( EAtomicType aExpectedType ) {
    TsNullArgumentRtException.checkNull( aExpectedType );
    expectedType = aExpectedType;
  }

  // ------------------------------------------------------------------------------------
  // ITsValidator
  //

  @Override
  final public ValidationResult validate( IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue != IAtomicValue.NULL && expectedType != EAtomicType.NONE ) {
      if( aValue.atomicType() != expectedType ) {
        return ValidationResult.error( FMT_ERR_VALUE_WRONG_AT, //
            expectedType.id(), aValue.atomicType().id(), aValue.asString() );
      }
    }
    return doValidate( aValue );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the expected atomic type of the values to be validated.
   *
   * @return {@link EAtomicType} - the expected type
   */
  public EAtomicType expectedType() {
    return expectedType;
  }

  // ------------------------------------------------------------------------------------
  // To be implemented
  //

  /**
   * Subclasses must perform validation.
   *
   * @param aValue {@link IAtomicValue} - value of {@link #expectedType()}, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  protected abstract ValidationResult doValidate( IAtomicValue aValue );

}
