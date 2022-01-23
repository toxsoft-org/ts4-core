package org.toxsoft.tslib.av.validators;

import static org.toxsoft.tslib.av.validators.ITsResources.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.validator.*;
import org.toxsoft.tslib.bricks.validator.std.NameStringValidator;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Check the name {@link EAtomicType#STRING} value.
 * <p>
 * For name checking {@link NameStringValidator} is used.
 * <p>
 * The following additional checks are performed specific for {@link IAtomicValue}:
 * <ul>
 * <li>an {@link TsNullArgumentRtException} exception is thrown if {@link ITsValidator#validate(Object)} argument is
 * <code>null</code>;</li>
 * <li>if argument does nat has atomic type {@link EAtomicType#STRING}, validation returns
 * {@link EValidationResultType#ERROR};</li>
 * <li>for {@link IAtomicValue#NULL} value either common empty name warning or special NULL-value warning is returned.
 * Warning type depends on constructor {@link NameStringAvValidator#NameStringAvValidator(boolean)} argument.</li>
 * <li>for all other values validation returns result of {@link #doAdditionalCheck(String)}, usually
 * {@link ValidationResult#SUCCESS}.</li>
 * </ul>
 *
 * @author hazard157
 */
public class NameStringAvValidator
    extends AbstractAvValidator {

  /**
   * Validator singleton.
   */
  public static ITsValidator<IAtomicValue> VALIDATOR = new NameStringAvValidator( true );

  private final boolean isWarnOnNullAv;

  /**
   * Constructor.
   *
   * @param aSpecialWarnOnNullAv boolean - determines if special warning for NULL will be returned
   */
  public NameStringAvValidator( boolean aSpecialWarnOnNullAv ) {
    super( EAtomicType.STRING );
    isWarnOnNullAv = aSpecialWarnOnNullAv;
  }

  /**
   * Creates validator with {@link #isWarnOnNullAv} = <code>false</code>.
   */
  public NameStringAvValidator() {
    this( false );
  }

  @Override
  public ValidationResult doValidate( IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( !aValue.isAssigned() ) {
      String msg = isWarnOnNullAv ? MSG_WARN_NULL_AV_NAME : NameStringValidator.getEmptyNameMessage();
      return ValidationResult.warn( msg );
    }
    return NameStringValidator.VALIDATOR.validate( aValue.asString() );
  }

  // ------------------------------------------------------------------------------------
  // To be overridden
  //

  /**
   * Subclasses may override to perform additional checks.
   * <p>
   * In the base class simply returns {@link ValidationResult#SUCCESS}, no need to call when overriding.
   *
   * @param aName String - name to be checked, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  protected ValidationResult doAdditionalCheck( String aName ) {
    return ValidationResult.SUCCESS;
  }

}
