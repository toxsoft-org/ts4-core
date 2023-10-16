package org.toxsoft.core.tslib.av.errors;

import static org.toxsoft.core.tslib.av.errors.ITsResources.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Loss of data when converting to a type with a lower bit depth.
 *
 * @author hazard157
 */
public class AvDataLossRtException
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
  public AvDataLossRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public AvDataLossRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, may be <code>null</code>
   */
  public AvDataLossRtException( Throwable aCause ) {
    super( LOG_STR_ERR_STD_DATA_LOSS, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public AvDataLossRtException() {
    super( LOG_STR_ERR_STD_DATA_LOSS );
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
   * @throws AvDataLossRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws AvDataLossRtException {
    if( !aCondition ) {
      throw new AvDataLossRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws AvDataLossRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws AvDataLossRtException {
    if( !aCondition ) {
      throw new AvDataLossRtException( LOG_STR_ERR_STD_DATA_LOSS );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws AvDataLossRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws AvDataLossRtException {
    if( aCondition ) {
      throw new AvDataLossRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws AvDataLossRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws AvDataLossRtException {
    if( aCondition ) {
      throw new AvDataLossRtException( LOG_STR_ERR_STD_DATA_LOSS );
    }
  }

  /**
   * Throws an exception if any reference is <code>null</code>.
   *
   * @param aRefs Object[] - checked references
   * @throws AvDataLossRtException aRefs == {@link NullPointerException}
   * @throws AvDataLossRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object... aRefs )
      throws AvDataLossRtException {
    if( aRefs == null ) {
      throw new AvDataLossRtException( LOG_STR_ERR_STD_DATA_LOSS );
    }
    for( int i = aRefs.length - 1; i >= 0; i-- ) {
      if( aRefs[i] == null ) {
        throw new AvDataLossRtException( LOG_STR_ERR_STD_DATA_LOSS );
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
   * @throws AvDataLossRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws AvDataLossRtException {
    if( aRef == null ) {
      throw new AvDataLossRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws AvDataLossRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws AvDataLossRtException {
    if( aRef == null ) {
      throw new AvDataLossRtException( LOG_STR_ERR_STD_DATA_LOSS );
    }
    return aRef;
  }

}
