package org.toxsoft.tslib.utils.errors;

import static org.toxsoft.tslib.utils.errors.ITsResources.*;

/**
 * This code is not written and is under development yet.
 *
 * @author hazard157
 */
public class TsUnderDevelopmentRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor for wrapper exception.
   * <p>
   * Message string is created useing {@link String#format(String, Object...)}.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsUnderDevelopmentRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( aCause, aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor.
   * <p>
   * Message string is created useing {@link String#format(String, Object...)}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsUnderDevelopmentRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsUnderDevelopmentRtException( Throwable aCause ) {
    super( ERR_MSG_UNDER_DEVELOPMENT, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsUnderDevelopmentRtException() {
    super( ERR_MSG_UNDER_DEVELOPMENT );
  }

}
