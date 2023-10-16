package org.toxsoft.core.tslib.av.errors;

import static org.toxsoft.core.tslib.av.errors.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Error accessing unassigned atomic value.
 * <p>
 * This exception is thrown when attempting to read a value using any <code>asXxx()</code> method other than
 * {@link IAtomicValue#asString()} from the {@link IAtomicValue#NULL} constant. Recall that {@link IAtomicValue#NULL} is
 * the only instance of the atomic data of the type {@link EAtomicType#NONE}.
 *
 * @author hazard157
 */
public class AvUnassignedValueRtException
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
  public AvUnassignedValueRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public AvUnassignedValueRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, may be <code>null</code>
   */
  public AvUnassignedValueRtException( Throwable aCause ) {
    super( LOG_STR_ERR_STD_UNASSIGNED_VALUE, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public AvUnassignedValueRtException() {
    super( LOG_STR_ERR_STD_UNASSIGNED_VALUE );
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
   * @throws AvUnassignedValueRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( !aCondition ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws AvUnassignedValueRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws AvUnassignedValueRtException {
    if( !aCondition ) {
      throw new AvUnassignedValueRtException( LOG_STR_ERR_STD_UNASSIGNED_VALUE );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws AvUnassignedValueRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( aCondition ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws AvUnassignedValueRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws AvUnassignedValueRtException {
    if( aCondition ) {
      throw new AvUnassignedValueRtException( LOG_STR_ERR_STD_UNASSIGNED_VALUE );
    }
  }

  /**
   * Throws an exception if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return &lt;E&gt; - always returns aRef
   * @throws AvUnassignedValueRtException aRef == <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( aRef != null ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws AvUnassignedValueRtException aRef == <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws AvUnassignedValueRtException {
    if( aRef != null ) {
      throw new AvUnassignedValueRtException( LOG_STR_ERR_STD_UNASSIGNED_VALUE );
    }
    return aRef;
  }

  /**
   * Throws an exception if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return &lt;E&gt; - always returns aRef
   * @throws AvUnassignedValueRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws AvUnassignedValueRtException {
    if( aRef == null ) {
      throw new AvUnassignedValueRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws AvUnassignedValueRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws AvUnassignedValueRtException {
    if( aRef == null ) {
      throw new AvUnassignedValueRtException( LOG_STR_ERR_STD_UNASSIGNED_VALUE );
    }
    return aRef;
  }

}
