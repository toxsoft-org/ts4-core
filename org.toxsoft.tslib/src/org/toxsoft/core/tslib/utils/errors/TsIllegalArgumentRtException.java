package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Uncheckable exception of illegal argument.
 * <p>
 * This excpetion is like {@link java.lang.IllegalArgumentException}.
 * <p>
 * This is method's precondition violation exception. Happens when methods argument is out of range allowed by API
 * contract.
 * <p>
 * Exception may be used directly and as base for other exceptions like {@link TsIllegalArgumentRtException}.
 *
 * @author hazard157
 */
public class TsIllegalArgumentRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor for wrapper exception.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsIllegalArgumentRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ), aCause );
  }

  /**
   * Constructor for wrapper exception.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   * @param aMessage String - message string
   */
  public TsIllegalArgumentRtException( Throwable aCause, String aMessage ) {
    super( aMessage, aCause );
  }

  /**
   * Constructor.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsIllegalArgumentRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ) );
  }

  /**
   * Constructor.
   *
   * @param aMessage String - message string
   */
  public TsIllegalArgumentRtException( String aMessage ) {
    super( aMessage );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsIllegalArgumentRtException( Throwable aCause ) {
    super( MSG_ERR_ILLEGAL_ARG, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsIllegalArgumentRtException() {
    super( MSG_ERR_ILLEGAL_ARG );
  }

  // ------------------------------------------------------------------------------------
  // Check & throw methods
  //

  /**
   * Throws an exception if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsIllegalArgumentRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsIllegalArgumentRtException {
    if( !aCondition ) {
      throw new TsIllegalArgumentRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsIllegalArgumentRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsIllegalArgumentRtException {
    if( !aCondition ) {
      throw new TsIllegalArgumentRtException( MSG_ERR_ILLEGAL_ARG );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsIllegalArgumentRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsIllegalArgumentRtException {
    if( aCondition ) {
      throw new TsIllegalArgumentRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsIllegalArgumentRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsIllegalArgumentRtException {
    if( aCondition ) {
      throw new TsIllegalArgumentRtException( MSG_ERR_ILLEGAL_ARG );
    }
  }

  /**
   * Throws an exception if any reference is <code>null</code>.
   *
   * @param aRefs Object[] - checked references
   * @throws TsIllegalArgumentRtException aRefs == {@link NullPointerException}
   * @throws TsIllegalArgumentRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object... aRefs )
      throws TsIllegalArgumentRtException {
    if( aRefs == null ) {
      throw new TsIllegalArgumentRtException( MSG_ERR_ILLEGAL_ARG );
    }
    for( int i = aRefs.length - 1; i >= 0; i-- ) {
      if( aRefs[i] == null ) {
        throw new TsIllegalArgumentRtException( MSG_ERR_ILLEGAL_ARG );
      }
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
   * @throws TsIllegalArgumentRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsIllegalArgumentRtException {
    if( aRef == null ) {
      throw new TsIllegalArgumentRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsIllegalArgumentRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsIllegalArgumentRtException {
    if( aRef == null ) {
      throw new TsIllegalArgumentRtException( MSG_ERR_ILLEGAL_ARG );
    }
    return aRef;
  }

}
