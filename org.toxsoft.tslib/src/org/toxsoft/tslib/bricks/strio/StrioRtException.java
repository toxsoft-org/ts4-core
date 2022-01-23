package org.toxsoft.tslib.bricks.strio;

import static org.toxsoft.tslib.bricks.strio.ITsResources.*;

import org.toxsoft.tslib.utils.errors.TsRuntimeException;

/**
 * Syntax error when reading the textual representation.
 *
 * @author hazard157
 */
public class StrioRtException
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
  public StrioRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ), aCause );
  }

  /**
   * Constructor.
   * <p>
   * Message string is created useing {@link String#format(String, Object...)}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public StrioRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ) );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public StrioRtException( Throwable aCause ) {
    super( MSG_ERR_STRIO_EXCEPTION, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public StrioRtException() {
    super( MSG_ERR_STRIO_EXCEPTION );
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
   * @throws StrioRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws StrioRtException {
    if( !aCondition ) {
      throw new StrioRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws StrioRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws StrioRtException {
    if( !aCondition ) {
      throw new StrioRtException( MSG_ERR_STRIO_EXCEPTION );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws StrioRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws StrioRtException {
    if( aCondition ) {
      throw new StrioRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws StrioRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws StrioRtException {
    if( aCondition ) {
      throw new StrioRtException( MSG_ERR_STRIO_EXCEPTION );
    }
  }

  /**
   * Throws an exception if any reference is <code>null</code>.
   *
   * @param aRefs Object[] - checked references
   * @throws StrioRtException aRefs == {@link NullPointerException}
   * @throws StrioRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object... aRefs )
      throws StrioRtException {
    if( aRefs == null ) {
      throw new StrioRtException( MSG_ERR_STRIO_EXCEPTION );
    }
    for( int i = aRefs.length - 1; i >= 0; i-- ) {
      if( aRefs[i] == null ) {
        throw new StrioRtException( MSG_ERR_STRIO_EXCEPTION );
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
   * @throws StrioRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws StrioRtException {
    if( aRef == null ) {
      throw new StrioRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws StrioRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws StrioRtException {
    if( aRef == null ) {
      throw new StrioRtException( MSG_ERR_STRIO_EXCEPTION );
    }
    return aRef;
  }

}
