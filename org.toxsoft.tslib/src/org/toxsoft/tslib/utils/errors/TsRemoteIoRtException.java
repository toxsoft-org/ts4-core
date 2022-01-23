package org.toxsoft.tslib.utils.errors;

import static org.toxsoft.tslib.utils.errors.ITsResources.*;

/**
 * Unchecked exception during remote or network operations.
 * <p>
 * It may be used as wrapper for RemoteException.
 *
 * @author hazard157
 */
public class TsRemoteIoRtException
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
  public TsRemoteIoRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public TsRemoteIoRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsRemoteIoRtException( Throwable aCause ) {
    super( aCause, ERR_MSG_REMOTE );
  }

  /**
   * Constructor with preset message.
   */
  public TsRemoteIoRtException() {
    super( ERR_MSG_REMOTE );
  }

  // ------------------------------------------------------------------------------------
  // Check & throw methods
  //

  /**
   * Throws exception if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsRemoteIoRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsRemoteIoRtException {
    if( !aCondition ) {
      throw new TsRemoteIoRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsRemoteIoRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsRemoteIoRtException {
    if( !aCondition ) {
      throw new TsRemoteIoRtException( ERR_MSG_REMOTE );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsRemoteIoRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsRemoteIoRtException {
    if( aCondition ) {
      throw new TsRemoteIoRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsRemoteIoRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsRemoteIoRtException {
    if( aCondition ) {
      throw new TsRemoteIoRtException( ERR_MSG_REMOTE );
    }
  }

}
