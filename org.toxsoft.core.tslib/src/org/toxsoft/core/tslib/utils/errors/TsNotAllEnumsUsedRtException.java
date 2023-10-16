package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Unchecked exception for <code>default</code> of <code>switch</code> on <code>enum</code> values.
 * <p>
 * Useful in <code>switch</code> expression where all <code>enum</code> constants must have corresponding
 * <code>case</code>. Throwing this exception in <code>default</code> case locates an error when new <code>enum</code>
 * constant is declared but correspondent switch <code>case</code> was not introduced.
 *
 * @author hazard157
 */
public class TsNotAllEnumsUsedRtException
    extends TsInternalErrorRtException {

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
  public TsNotAllEnumsUsedRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public TsNotAllEnumsUsedRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsNotAllEnumsUsedRtException( Throwable aCause ) {
    super( LOG_STR_ERR_NOT_ALL_ENUMS_USED, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsNotAllEnumsUsedRtException() {
    super( LOG_STR_ERR_NOT_ALL_ENUMS_USED );
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
   * @throws TsNotAllEnumsUsedRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsNotAllEnumsUsedRtException {
    if( !aCondition ) {
      throw new TsNotAllEnumsUsedRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsNotAllEnumsUsedRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsNotAllEnumsUsedRtException {
    if( !aCondition ) {
      throw new TsNotAllEnumsUsedRtException( LOG_STR_ERR_NOT_ALL_ENUMS_USED );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNotAllEnumsUsedRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsNotAllEnumsUsedRtException {
    if( aCondition ) {
      throw new TsNotAllEnumsUsedRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsNotAllEnumsUsedRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsNotAllEnumsUsedRtException {
    if( aCondition ) {
      throw new TsNotAllEnumsUsedRtException( LOG_STR_ERR_NOT_ALL_ENUMS_USED );
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
   * @throws TsNotAllEnumsUsedRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsNotAllEnumsUsedRtException {
    if( aRef == null ) {
      throw new TsNotAllEnumsUsedRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsNotAllEnumsUsedRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsNotAllEnumsUsedRtException {
    if( aRef == null ) {
      throw new TsNotAllEnumsUsedRtException( LOG_STR_ERR_NOT_ALL_ENUMS_USED );
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
   * @throws TsNotAllEnumsUsedRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsNotAllEnumsUsedRtException {
    if( aRef != null ) {
      throw new TsNotAllEnumsUsedRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsNotAllEnumsUsedRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws TsNotAllEnumsUsedRtException {
    if( aRef != null ) {
      throw new TsNotAllEnumsUsedRtException( LOG_STR_ERR_NOT_ALL_ENUMS_USED );
    }
    return aRef;
  }

}
