package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Specified element not found (eg, map does not contains element with specified key).
 * <p>
 * In most cases this is method's precondition violation exception.
 *
 * @author hazard157
 */
public class TsItemNotFoundRtException
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
  public TsItemNotFoundRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public TsItemNotFoundRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsItemNotFoundRtException( Throwable aCause ) {
    super( ERR_MSG_ITEM_NOT_FOUND, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsItemNotFoundRtException() {
    super( ERR_MSG_ITEM_NOT_FOUND );
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
   * @throws TsItemNotFoundRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsItemNotFoundRtException {
    if( !aCondition ) {
      throw new TsItemNotFoundRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsItemNotFoundRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsItemNotFoundRtException {
    if( !aCondition ) {
      throw new TsItemNotFoundRtException( ERR_MSG_ITEM_NOT_FOUND );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsItemNotFoundRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsItemNotFoundRtException {
    if( aCondition ) {
      throw new TsItemNotFoundRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsItemNotFoundRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsItemNotFoundRtException {
    if( aCondition ) {
      throw new TsItemNotFoundRtException( ERR_MSG_ITEM_NOT_FOUND );
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
   * @throws TsItemNotFoundRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsItemNotFoundRtException {
    if( aRef == null ) {
      throw new TsItemNotFoundRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsItemNotFoundRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsItemNotFoundRtException {
    if( aRef == null ) {
      throw new TsItemNotFoundRtException( ERR_MSG_ITEM_NOT_FOUND );
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
   * @throws TsItemNotFoundRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsItemNotFoundRtException {
    if( aRef != null ) {
      throw new TsItemNotFoundRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsItemNotFoundRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws TsItemNotFoundRtException {
    if( aRef != null ) {
      throw new TsItemNotFoundRtException( ERR_MSG_ITEM_NOT_FOUND );
    }
    return aRef;
  }

}
