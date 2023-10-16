package org.toxsoft.core.tslib.av.errors;

import static org.toxsoft.core.tslib.av.errors.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Error converting one type to other (atomic type incompatibility error).
 * <p>
 * Real data in atomic value has some {@link EAtomicType} that cannot be converted to other atomic type.For example it
 * is an error try to access {@link EAtomicType#INTEGER} as a {@link EAtomicType#FLOATING}.
 *
 * @author hazard157
 */
public class AvTypeCastRtException
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
  public AvTypeCastRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public AvTypeCastRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, may be <code>null</code>
   */
  public AvTypeCastRtException( Throwable aCause ) {
    super( LOG_STR_ERR_STD_TYPE_CAST, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public AvTypeCastRtException() {
    super( LOG_STR_ERR_STD_TYPE_CAST );
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
   * @throws AvTypeCastRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( !aCondition ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws AvTypeCastRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws AvTypeCastRtException {
    if( !aCondition ) {
      throw new AvTypeCastRtException( LOG_STR_ERR_STD_TYPE_CAST );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws AvTypeCastRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aCondition ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws AvTypeCastRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws AvTypeCastRtException {
    if( aCondition ) {
      throw new AvTypeCastRtException( LOG_STR_ERR_STD_TYPE_CAST );
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
   * @throws AvTypeCastRtException aRef == <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aRef != null ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws AvTypeCastRtException aRef == <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws AvTypeCastRtException {
    if( aRef != null ) {
      throw new AvTypeCastRtException( LOG_STR_ERR_STD_TYPE_CAST );
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
   * @throws AvTypeCastRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aRef == null ) {
      throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws AvTypeCastRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws AvTypeCastRtException {
    if( aRef == null ) {
      throw new AvTypeCastRtException( LOG_STR_ERR_STD_TYPE_CAST );
    }
    return aRef;
  }

  /**
   * Checks if left-value of the specified atomic type can be assigned the value.
   *
   * @param aLvalType {@link EAtomicType} - atomic type of the variable (left-value)
   * @param aRvalType {@link EAtomicType} - atomic type of the value to assign (right-value)
   * @return boolean - the check result, <code>true</code> if assignment can be done
   */
  public static boolean canAssign( EAtomicType aLvalType, EAtomicType aRvalType ) {
    if( aLvalType != EAtomicType.NONE && aRvalType != EAtomicType.NONE ) {
      if( aLvalType != aRvalType ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Throws an exception if left-value of the specified atomic type can <b>not</b> be assigned the value.
   *
   * @param aLvalType {@link EAtomicType} - atomic type of the variable (left-value)
   * @param aRvalType {@link EAtomicType} - atomic type of the value to assign (right-value)
   * @throws AvTypeCastRtException check {@link #canAssign(EAtomicType, EAtomicType)} failed
   */
  public static void checkCanAssign( EAtomicType aLvalType, EAtomicType aRvalType )
      throws AvTypeCastRtException {
    if( !canAssign( aLvalType, aRvalType ) ) {
      throw new AvTypeCastRtException( LOG_FMT_ERR_CANT_ASSIGN, aLvalType.id(), aRvalType.id() );
    }
  }

  /**
   * Throws an exception if left-value of the specified atomic type can <b>not</b> be assigned the value.
   *
   * @param aLvalType {@link EAtomicType} - atomic type of the variable (left-value)
   * @param aRvalType {@link EAtomicType} - atomic type of the value to assign (right-value)
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws AvTypeCastRtException check {@link #canAssign(EAtomicType, EAtomicType)} failed
   */
  public static void checkCanAssign( EAtomicType aLvalType, EAtomicType aRvalType, String aMessageFormat,
      Object... aMsgArgs )
      throws AvTypeCastRtException {
    if( aLvalType != EAtomicType.NONE && aRvalType != EAtomicType.NONE ) {
      if( aLvalType != aRvalType ) {
        throw new AvTypeCastRtException( aMessageFormat, aMsgArgs );
      }
    }
  }

}
