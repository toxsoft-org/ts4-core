package org.toxsoft.tslib.bricks.validator.std;

import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.tslib.bricks.validator.std.ITsResources.*;

import org.toxsoft.tslib.av.metainfo.IAvMetaConstants;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.validator.*;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Check the name {@link String} value.
 * <p>
 * <i>Name</i> means short textual string used for some entity visual identification. Name examples are
 * {@link IStridable#nmName()}, option {@link IAvMetaConstants#DDEF_NAME}, strings identidied by
 * {@link IAvMetaConstants#TSID_NAME}, etc.
 * <p>
 * The following checks are performed:
 * <ul>
 * <li>an {@link TsNullArgumentRtException} exception is thrown if {@link ITsValidator#validate(Object)} argument is
 * <code>null</code>;</li>
 * <li>for blank strings (that is, {@link String#isBlank()} == <code>true</code>) validation returns the warning
 * {@link EValidationResultType#WARNING};</li>
 * <li>for default value (that is, {@link IAvMetaConstants#DEFAULT_NAME}) validation returns the warning
 * {@link EValidationResultType#WARNING};</li>
 * <li>for all other values validation returns result of {@link #doAdditionalCheck(String)}, usually
 * {@link ValidationResult#SUCCESS}.</li>
 * </ul>
 *
 * @author hazard157
 */
public class NameStringValidator
    implements ITsValidator<String> {

  /**
   * Validator singleton.
   */
  public static ITsValidator<String> VALIDATOR = new NameStringValidator();

  /**
   * Constructor.
   */
  public NameStringValidator() {
    // nop
  }

  @Override
  public ValidationResult validate( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    ValidationResult vr = ValidationResult.SUCCESS;
    if( aValue.isBlank() ) {
      vr = ValidationResult.warn( MSG_WARN_EMPTY_NAME );
    }
    if( aValue.equals( DEFAULT_NAME ) ) {
      vr = ValidationResult.warn( MSG_WARN_DEFAULT_NAME );
    }
    return ValidationResult.firstNonOk( vr, doAdditionalCheck( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the message for emtry name warning.
   *
   * @return String - emtry name warning message
   */
  public static String getEmptyNameMessage() {
    return MSG_WARN_EMPTY_NAME;
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
