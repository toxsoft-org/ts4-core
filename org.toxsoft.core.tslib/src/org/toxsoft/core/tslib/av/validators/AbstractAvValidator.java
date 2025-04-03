package org.toxsoft.core.tslib.av.validators;

import static org.toxsoft.core.tslib.av.validators.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class of all {@link ITsValidator} implementations for values of type {@link IAtomicValue}.
 * <p>
 * Checks argument of {@link #validate(IAtomicValue)} against expected atomic type. Also throws an
 * {@link TsNullArgumentRtException} if argument is <code>null</code>.
 * <p>
 * The {@link IAtomicValue#NULL} argument is always passed to subclass in {@link #doValidate(IAtomicValue)}.
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
