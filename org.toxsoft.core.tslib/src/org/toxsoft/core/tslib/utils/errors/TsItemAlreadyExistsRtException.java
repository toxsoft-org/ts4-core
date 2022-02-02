package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Specified element already exists (eg, map already contains element with specified key).
 * <p>
 * In most cases this is method's precondition violation exception.
 *
 * @author hazard157
 */
public class TsItemAlreadyExistsRtException
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
  public TsItemAlreadyExistsRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public TsItemAlreadyExistsRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsItemAlreadyExistsRtException( Throwable aCause ) {
    super( MSG_ERR_ITEM_ALREADY_EXIST, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsItemAlreadyExistsRtException() {
    super( MSG_ERR_ITEM_ALREADY_EXIST );
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
   * @throws TsItemAlreadyExistsRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsItemAlreadyExistsRtException {
    if( !aCondition ) {
      throw new TsItemAlreadyExistsRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsItemAlreadyExistsRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsItemAlreadyExistsRtException {
    if( !aCondition ) {
      throw new TsItemAlreadyExistsRtException( MSG_ERR_ITEM_ALREADY_EXIST );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsItemAlreadyExistsRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsItemAlreadyExistsRtException {
    if( aCondition ) {
      throw new TsItemAlreadyExistsRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsItemAlreadyExistsRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsItemAlreadyExistsRtException {
    if( aCondition ) {
      throw new TsItemAlreadyExistsRtException( MSG_ERR_ITEM_ALREADY_EXIST );
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
   * @throws TsItemAlreadyExistsRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsItemAlreadyExistsRtException {
    if( aRef == null ) {
      throw new TsItemAlreadyExistsRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsItemAlreadyExistsRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsItemAlreadyExistsRtException {
    if( aRef == null ) {
      throw new TsItemAlreadyExistsRtException( MSG_ERR_ITEM_ALREADY_EXIST );
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
   * @throws TsItemAlreadyExistsRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsItemAlreadyExistsRtException {
    if( aRef != null ) {
      throw new TsItemAlreadyExistsRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsItemAlreadyExistsRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws TsItemAlreadyExistsRtException {
    if( aRef != null ) {
      throw new TsItemAlreadyExistsRtException( MSG_ERR_ITEM_ALREADY_EXIST );
    }
    return aRef;
  }

}
