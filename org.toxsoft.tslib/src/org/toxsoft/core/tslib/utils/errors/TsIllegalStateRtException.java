package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Method call is illegal - particular instance state is not ready for processing.
 * <p>
 * In most cases this exception means that caller does not perforemed mandatory check on called object. Unlike
 * {@link TsUnsupportedFeatureRtException} maybe later, when object state changes, call of method will succeed.
 *
 * @author hazard157
 */
public class TsIllegalStateRtException
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
  public TsIllegalStateRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public TsIllegalStateRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsIllegalStateRtException( Throwable aCause ) {
    super( ERR_MSG_ILLEGAL_STATE, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsIllegalStateRtException() {
    super( ERR_MSG_ILLEGAL_STATE );
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
   * @throws TsIllegalStateRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsIllegalStateRtException {
    if( !aCondition ) {
      throw new TsIllegalStateRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsIllegalStateRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsIllegalStateRtException {
    if( !aCondition ) {
      throw new TsIllegalStateRtException( ERR_MSG_ILLEGAL_STATE );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsIllegalStateRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsIllegalStateRtException {
    if( aCondition ) {
      throw new TsIllegalStateRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsIllegalStateRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsIllegalStateRtException {
    if( aCondition ) {
      throw new TsIllegalStateRtException( ERR_MSG_ILLEGAL_STATE );
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
   * @throws TsIllegalStateRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsIllegalStateRtException {
    if( aRef == null ) {
      throw new TsIllegalStateRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsIllegalStateRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsIllegalStateRtException {
    if( aRef == null ) {
      throw new TsIllegalStateRtException( ERR_MSG_ILLEGAL_STATE );
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
   * @throws TsIllegalStateRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsIllegalStateRtException {
    if( aRef != null ) {
      throw new TsIllegalStateRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsIllegalStateRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws TsIllegalStateRtException {
    if( aRef != null ) {
      throw new TsIllegalStateRtException( ERR_MSG_ILLEGAL_STATE );
    }
    return aRef;
  }

}
