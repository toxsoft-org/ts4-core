package org.toxsoft.tslib.utils.errors;

import static org.toxsoft.tslib.utils.errors.ITsResources.*;

/**
 * Call of "null" object methods.
 * <p>
 * Null objects are implementation of design pattern "<i>special case</i>" as described in Martin Fowler's "Refactoring"
 * (Introduce null object). Depending on designation of object (such as "unknown", "empty" or just "null" instaed of
 * <code>null</code>) call of some methods may be prohibited. This is the exception to be thrown from such methods.
 * <p>
 * Thsi exception reveals eroor in progam code and needs immediate correction.
 *
 * @author hazard157
 */
public class TsNullObjectErrorRtException
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
  public TsNullObjectErrorRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public TsNullObjectErrorRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for error accessing method of the specified Null object class.
   *
   * @param aNullObjClass {@link Class} - the class of the special-case object
   */
  public TsNullObjectErrorRtException( Class<?> aNullObjClass ) {
    super( String.format( FMT_MSG_NULL_OBJECT_ERROR, aNullObjClass.getName() ) );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsNullObjectErrorRtException( Throwable aCause ) {
    super( MSG_ERR_NULL_OBJECT_ERROR, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsNullObjectErrorRtException() {
    super( MSG_ERR_NULL_OBJECT_ERROR );
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
   * @throws TsNullObjectErrorRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsNullObjectErrorRtException {
    if( !aCondition ) {
      throw new TsNullObjectErrorRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsNullObjectErrorRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsNullObjectErrorRtException {
    if( !aCondition ) {
      throw new TsNullObjectErrorRtException( MSG_ERR_NULL_OBJECT_ERROR );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullObjectErrorRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsNullObjectErrorRtException {
    if( aCondition ) {
      throw new TsNullObjectErrorRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsNullObjectErrorRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsNullObjectErrorRtException {
    if( aCondition ) {
      throw new TsNullObjectErrorRtException( MSG_ERR_NULL_OBJECT_ERROR );
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
   * @throws TsNullObjectErrorRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsNullObjectErrorRtException {
    if( aRef == null ) {
      throw new TsNullObjectErrorRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsNullObjectErrorRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsNullObjectErrorRtException {
    if( aRef == null ) {
      throw new TsNullObjectErrorRtException( MSG_ERR_NULL_OBJECT_ERROR );
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
   * @throws TsNullObjectErrorRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsNullObjectErrorRtException {
    if( aRef != null ) {
      throw new TsNullObjectErrorRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsNullObjectErrorRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws TsNullObjectErrorRtException {
    if( aRef != null ) {
      throw new TsNullObjectErrorRtException( MSG_ERR_NULL_OBJECT_ERROR );
    }
    return aRef;
  }

}
