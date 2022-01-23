package org.toxsoft.tslib.bricks.validator;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Information about validation process result.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class ValidationResult
    implements Serializable {

  private static final long serialVersionUID = -5298394134973519237L;

  /**
   * Singleton instance of validation success result (with empty {@link #message()}).
   */
  public static final ValidationResult SUCCESS =
      new ValidationResult( EValidationResultType.OK, TsLibUtils.EMPTY_STRING );

  private final EValidationResultType type;
  private final String                msg;

  private ValidationResult( EValidationResultType aType, String aMessageFormat, Object... aMsgArgs ) {
    TsNullArgumentRtException.checkNulls( aType, aMessageFormat, aMsgArgs );
    msg = String.format( aMessageFormat, aMsgArgs );
    type = aType;
  }

  /**
   * Method correctly deserializes {@link ValidationResult#SUCCESS} value.
   *
   * @return {@link ObjectStreamException} - {@link ValidationResult#SUCCESS}
   * @throws ObjectStreamException is declared but newer throw by this method
   */
  private Object readResolve()
      throws ObjectStreamException {
    if( type == EValidationResultType.OK && msg.isEmpty() ) {
      return SUCCESS;
    }
    return this;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns type of validation result.
   *
   * @return {@link EValidationResultType} - type of validation result
   */
  public EValidationResultType type() {
    return type;
  }

  /**
   * Returns human-readable message about warning or error.
   * <p>
   * For {@link #SUCCESS} returns empty string.
   *
   * @return String - human-readable message
   */
  public String message() {
    return msg;
  }

  /**
   * Returns <code>true</code> if result is {@link EValidationResultType#OK}.
   *
   * @return boolean - <code>true</code> if result is {@link EValidationResultType#OK}
   */
  public boolean isOk() {
    return type == EValidationResultType.OK;
  }

  /**
   * Returns <code>true</code> if result is {@link EValidationResultType#WARNING}.
   *
   * @return boolean - <code>true</code> if result is {@link EValidationResultType#WARNING}
   */
  public boolean isWarning() {
    return type == EValidationResultType.WARNING;
  }

  /**
   * Returns <code>true</code> if result is {@link EValidationResultType#ERROR}.
   *
   * @return boolean - <code>true</code> if result is {@link EValidationResultType#ERROR}
   */
  public boolean isError() {
    return type == EValidationResultType.ERROR;
  }

  // ------------------------------------------------------------------------------------
  // Static constructors with null source and data
  //

  /**
   * Creates success (informational) result with {@link #type()} = {@link EValidationResultType#OK}.
   * <p>
   * If {@link #message()} is empty, {@link #SUCCESS} instance will be returned instead of a newly created instance.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return {@link ValidationResult} - created validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult info( String aMessageFormat, Object... aMsgArgs ) {
    ValidationResult vr = new ValidationResult( EValidationResultType.OK, aMessageFormat, aMsgArgs );
    if( vr.message().isEmpty() ) {
      return SUCCESS;
    }
    return vr;
  }

  /**
   * Creates warning result with {@link #type()} = {@link EValidationResultType#WARNING}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return {@link ValidationResult} - created validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult warn( String aMessageFormat, Object... aMsgArgs ) {
    return new ValidationResult( EValidationResultType.WARNING, aMessageFormat, aMsgArgs );
  }

  /**
   * Creates error result with {@link #type()} = {@link EValidationResultType#ERROR}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return {@link ValidationResult} - created validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult error( String aMessageFormat, Object... aMsgArgs ) {
    return new ValidationResult( EValidationResultType.ERROR, aMessageFormat, aMsgArgs );
  }

  /**
   * Creates error result based on exception.
   *
   * @param aError {@link Throwable} - exception
   * @return {@link ValidationResult} - created validation result
   */
  public static ValidationResult error( Throwable aError ) {
    return new ValidationResult( EValidationResultType.ERROR, aError.getLocalizedMessage() );
  }

  // ------------------------------------------------------------------------------------
  // Handling multiple validation result priorities
  //

  /**
   * Returns "earlier" warning or error result.
   * <p>
   * First argument is considered as "earlier" validation result.
   * <p>
   * First checks for errors and returns error if any. Then warnings are checked.
   *
   * @param aVr1 {@link ValidationResult} - first (earlier) validation result
   * @param aVr2 {@link ValidationResult} - second (later) validation result
   * @return {@link ValidationResult} - earlier warning/error messsage if any or success
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult firstNonOk( ValidationResult aVr1, ValidationResult aVr2 ) {
    TsNullArgumentRtException.checkNulls( aVr1, aVr2 );
    switch( aVr1.type() ) {
      case ERROR:
        return aVr1;
      case WARNING:
        if( aVr2.isError() ) {
          return aVr2;
        }
        return aVr1;
      case OK:
        return aVr2;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Returns later warning or error result.
   * <p>
   * First argument is considered as "earlier" validation result.
   * <p>
   * First checks for errors and returns error if any. Then warnings are checked.
   *
   * @param aVr1 {@link ValidationResult} - first (earlier) validation result
   * @param aVr2 {@link ValidationResult} - second (later) validation result
   * @return {@link ValidationResult} - later warning/error messsage if any or success
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult lastNonOk( ValidationResult aVr1, ValidationResult aVr2 ) {
    TsNullArgumentRtException.checkNulls( aVr1, aVr2 );
    switch( aVr2.type() ) {
      case OK:
        return aVr1;
      case WARNING:
        if( aVr1.isError() ) {
          return aVr1;
        }
        return aVr2;
      case ERROR:
        return aVr2;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    if( msg.isEmpty() ) {
      return type.id();
    }
    return type.id() + ": " + msg; //$NON-NLS-1$
  }

}
