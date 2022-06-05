package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Null argumnet exception.
 * <p>
 * This excpetion is like {@link java.lang.NullPointerException} but used only for methods arguments checking.
 * <p>
 * This is method's precondition violation exception. Happens when method gets <code>null</code> argument not allowed by
 * API contract.
 *
 * @author hazard157
 */
public class TsNullArgumentRtException
    extends TsIllegalArgumentRtException {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   * <p>
   * Message string is created useing {@link String#format(String, Object...)}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsNullArgumentRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor.
   *
   * @param aMessage String - message string
   */
  public TsNullArgumentRtException( String aMessage ) {
    super( aMessage );
  }

  /**
   * Constructor with preset message.
   */
  public TsNullArgumentRtException() {
    super( MSG_ERR_NULL_ARGUMENT );
  }

  // ------------------------------------------------------------------------------------
  // Check & throw methods
  //

  /**
   * Throws exception if any reference is <code>null</code>.
   *
   * @param aRef1 Object - checked reference
   * @throws TsNullArgumentRtException aRefs == {@link NullPointerException}
   * @throws TsNullArgumentRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object aRef1 )
      throws TsNullArgumentRtException {
    if( aRef1 == null ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
  }

  /**
   * Throws exception if any reference is <code>null</code>.
   *
   * @param aRef1 Object - checked reference
   * @param aRef2 Object - checked reference
   * @throws TsNullArgumentRtException aRefs == {@link NullPointerException}
   * @throws TsNullArgumentRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object aRef1, Object aRef2 )
      throws TsNullArgumentRtException {
    if( aRef1 == null || aRef2 == null ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
  }

  /**
   * Throws exception if any reference is <code>null</code>.
   *
   * @param aRef1 Object - checked reference
   * @param aRef2 Object - checked reference
   * @param aRef3 Object - checked reference
   * @throws TsNullArgumentRtException aRefs == {@link NullPointerException}
   * @throws TsNullArgumentRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object aRef1, Object aRef2, Object aRef3 )
      throws TsNullArgumentRtException {
    if( aRef1 == null || aRef2 == null || aRef3 == null ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
  }

  /**
   * Throws exception if any reference is <code>null</code>.
   *
   * @param aRef1 Object - checked reference
   * @param aRef2 Object - checked reference
   * @param aRef3 Object - checked reference
   * @param aRef4 Object - checked reference
   * @throws TsNullArgumentRtException aRefs == {@link NullPointerException}
   * @throws TsNullArgumentRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object aRef1, Object aRef2, Object aRef3, Object aRef4 )
      throws TsNullArgumentRtException {
    if( aRef1 == null || aRef2 == null || aRef3 == null || aRef4 == null ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
  }

  /**
   * Throws exception if any reference is <code>null</code>.
   *
   * @param aRef1 Object - checked reference
   * @param aRef2 Object - checked reference
   * @param aRef3 Object - checked reference
   * @param aRef4 Object - checked reference
   * @param aRef5 Object - checked reference
   * @param aRefs Object[] - checked references
   * @throws TsNullArgumentRtException aRefs == {@link NullPointerException}
   * @throws TsNullArgumentRtException any array element is <code>null</code>
   */
  public static void checkNulls( Object aRef1, Object aRef2, Object aRef3, Object aRef4, Object aRef5, Object... aRefs )
      throws TsNullArgumentRtException {
    if( aRef1 == null || aRef2 == null || aRef3 == null || aRef4 == null || aRef5 == null ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
    for( int i = aRefs.length - 1; i >= 0; i-- ) {
      if( aRefs[i] == null ) {
        throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
      }
    }
  }

  /**
   * Throws exception if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return &lt;E&gt; - always returns aRef
   * @throws TsNullArgumentRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsNullArgumentRtException {
    if( aRef == null ) {
      throw new TsNullArgumentRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsNullArgumentRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsNullArgumentRtException {
    if( aRef == null ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
    return aRef;
  }

  /**
   * Throws exception if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsNullArgumentRtException {
    if( !aCondition ) {
      throw new TsNullArgumentRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsNullArgumentRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsNullArgumentRtException {
    if( !aCondition ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
  }

  /**
   * Throws exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsNullArgumentRtException {
    if( aCondition ) {
      throw new TsNullArgumentRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsNullArgumentRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsNullArgumentRtException {
    if( aCondition ) {
      throw new TsNullArgumentRtException( MSG_ERR_NULL_ARGUMENT );
    }
  }

}
