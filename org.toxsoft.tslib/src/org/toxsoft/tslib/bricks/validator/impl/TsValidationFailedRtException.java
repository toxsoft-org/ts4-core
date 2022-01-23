package org.toxsoft.tslib.bricks.validator.impl;

import org.toxsoft.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Validation failure exception.
 *
 * @author hazard157
 */
public class TsValidationFailedRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  private final IList<ValidationResult> validationResults;

  /**
   * Constructs exception based on specified result.
   * <p>
   * Although argument can be success or warning result, it is strongly recommended to use only error results for
   * exception.
   *
   * @param aValidationResult {@link ValidationResult} - cause of exception
   */
  public TsValidationFailedRtException( ValidationResult aValidationResult ) {
    super( aValidationResult.message() );
    validationResults = new ElemArrayList<>( aValidationResult );
  }

  /**
   * Constructs exception based on multiple results.
   *
   * @param aValidationResults {@link ValidationResult}[] - an array of exception causes
   */
  public TsValidationFailedRtException( ValidationResult... aValidationResults ) {
    super( messageFromArray( aValidationResults ) );
    validationResults = new ElemArrayList<>( aValidationResults );
  }

  /**
   * Constructs exception based on multiple results.
   * <p>
   * Error message is based on first error or last warning in list. If there is no eeror or warning results, message
   * will be empty string.
   *
   * @param aValidationResults {@link ValidationResult}[] - an array of exception causes
   */
  public TsValidationFailedRtException( ITsCollection<ValidationResult> aValidationResults ) {
    super( messageFromColl( aValidationResults ) );
    validationResults = new ElemArrayList<>( aValidationResults );
  }

  private static String messageFromArray( ValidationResult[] aValRes ) {
    if( aValRes == null || aValRes.length == 0 ) {
      return TsLibUtils.EMPTY_STRING;
    }
    String msg = TsLibUtils.EMPTY_STRING;
    for( int i = 0; i < aValRes.length; i++ ) {
      ValidationResult vr = aValRes[i];
      switch( vr.type() ) {
        case ERROR:
          return vr.message();
        case WARNING:
          msg = vr.message();
          break;
        case OK:
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
    return msg;
  }

  private static String messageFromColl( ITsCollection<ValidationResult> aValRes ) {
    if( aValRes == null || aValRes.isEmpty() ) {
      return TsLibUtils.EMPTY_STRING;
    }
    String msg = TsLibUtils.EMPTY_STRING;
    for( ValidationResult vr : aValRes ) {
      switch( vr.type() ) {
        case ERROR:
          return vr.message();
        case WARNING:
          msg = vr.message();
          break;
        case OK:
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
    return msg;
  }

  /**
   * Throws an exception if specified validation result is of type {@link EValidationResultType#ERROR}.
   *
   * @param aValidationResult {@link ValidationResult} - specified result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static void checkError( ValidationResult aValidationResult ) {
    TsNullArgumentRtException.checkNull( aValidationResult );
    if( aValidationResult.isError() ) {
      throw new TsValidationFailedRtException( aValidationResult );
    }
  }

  /**
   * Throws an exception if any of the specified validation results is of type {@link EValidationResultType#ERROR}.
   *
   * @param aValidationResults {@link ValidationResult}[] - specified results
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void checkError( ValidationResult... aValidationResults ) {
    TsErrorUtils.checkArrayArg( aValidationResults );
    for( int i = 0; i < aValidationResults.length; i++ ) {
      ValidationResult vr = aValidationResults[i];
      if( vr.isError() ) {
        throw new TsValidationFailedRtException( aValidationResults );
      }
    }
  }

  /**
   * Throws an exception if any of the specified validation results is of type {@link EValidationResultType#ERROR}.
   *
   * @param aValidationResults {@link ITsCollection} - specified results
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static void checkError( ITsCollection<ValidationResult> aValidationResults ) {
    TsNullArgumentRtException.checkNull( aValidationResults );
    for( ValidationResult vr : aValidationResults ) {
      if( vr.isError() ) {
        throw new TsValidationFailedRtException( aValidationResults );
      }
    }
  }

  /**
   * Throws an exception if specified validation result is of type {@link EValidationResultType#ERROR} or
   * {@link EValidationResultType#WARNING}.
   *
   * @param aValidationResult {@link ValidationResult} - specified result
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static void checkWarn( ValidationResult aValidationResult ) {
    TsNullArgumentRtException.checkNull( aValidationResult );
    if( aValidationResult.isError() || aValidationResult.isWarning() ) {
      throw new TsValidationFailedRtException( aValidationResult );
    }
  }

  /**
   * Throws an exception if any of the specified validation results is of type {@link EValidationResultType#ERROR} or
   * {@link EValidationResultType#WARNING}.
   *
   * @param aValidationResults {@link ValidationResult}[] - specified results
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void checkWarn( ValidationResult... aValidationResults ) {
    TsErrorUtils.checkArrayArg( aValidationResults );
    for( int i = 0; i < aValidationResults.length; i++ ) {
      ValidationResult vr = aValidationResults[i];
      if( vr.isError() || vr.isWarning() ) {
        throw new TsValidationFailedRtException( aValidationResults );
      }
    }
  }

  /**
   * Throws an exception if any of the specified validation results is of type {@link EValidationResultType#ERROR} or
   * {@link EValidationResultType#WARNING}.
   *
   * @param aValidationResults {@link ITsCollection} - specified results
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static void checkWarn( ITsCollection<ValidationResult> aValidationResults ) {
    TsNullArgumentRtException.checkNull( aValidationResults );
    for( ValidationResult vr : aValidationResults ) {
      if( vr.isError() || vr.isWarning() ) {
        throw new TsValidationFailedRtException( aValidationResults );
      }
    }
  }

  /**
   * Returns validation results causing exception.
   *
   * @return IList&lt;{@link ValidationResult}&gt; - list of all checked resuls, some with errors/warnings
   */
  public IList<ValidationResult> validationResults() {
    return validationResults;
  }

}
