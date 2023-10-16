package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Internal error - method call is valid however internal state of object is invalid due to errors in code.
 *
 * @author hazard157
 */
public class TsInternalErrorRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor for wrapper exception.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aCause Throwable - cause, may be <code>null</code>
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsInternalErrorRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( aCause, aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsInternalErrorRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, may be <code>null</code>
   */
  public TsInternalErrorRtException( Throwable aCause ) {
    super( LOG_STR_ERR_INTERNAL_ERROR, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsInternalErrorRtException() {
    super( LOG_STR_ERR_INTERNAL_ERROR );
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
   * @throws TsInternalErrorRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsInternalErrorRtException {
    if( !aCondition ) {
      throw new TsInternalErrorRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsInternalErrorRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsInternalErrorRtException {
    if( !aCondition ) {
      throw new TsInternalErrorRtException( LOG_STR_ERR_INTERNAL_ERROR );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsInternalErrorRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsInternalErrorRtException {
    if( aCondition ) {
      throw new TsInternalErrorRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsInternalErrorRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsInternalErrorRtException {
    if( aCondition ) {
      throw new TsInternalErrorRtException( LOG_STR_ERR_INTERNAL_ERROR );
    }
  }

  /**
   * Throws an exception if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return &lt;E&gt; - always returns aRef
   * @throws TsInternalErrorRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsInternalErrorRtException {
    if( aRef == null ) {
      throw new TsInternalErrorRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsInternalErrorRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsInternalErrorRtException {
    if( aRef == null ) {
      throw new TsInternalErrorRtException( LOG_STR_ERR_INTERNAL_ERROR );
    }
    return aRef;
  }

  /**
   * Throws an exception if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return &lt;E&gt; - always returns aRef
   * @throws TsInternalErrorRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsInternalErrorRtException {
    if( aRef != null ) {
      throw new TsInternalErrorRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsInternalErrorRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws TsInternalErrorRtException {
    if( aRef != null ) {
      throw new TsInternalErrorRtException( LOG_STR_ERR_INTERNAL_ERROR );
    }
    return aRef;
  }

}
