package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

/**
 * Method call is illegal - particular instance does not support called method.
 * <p>
 * In most cases this exception means that caller does not perforemed mandatory check on called object. Unlike
 * {@link TsIllegalStateRtException}, for the particular instance, call of method will never succeed.
 * <p>
 * This exception is rarely used. Most useful use case happens when the same interface is used for different kind of
 * objects. For example, <code>ITreeNode</code> is used both for nodes and leafs in tree. Call of method
 * <code>ITreeNode.addChild(...)</code> may throw this exception for leafs.
 *
 * @author hazard157
 */
public class TsUnsupportedFeatureRtException
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
  public TsUnsupportedFeatureRtException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
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
  public TsUnsupportedFeatureRtException( String aMessageFormat, Object... aMsgArgs ) {
    super( aMessageFormat, aMsgArgs );
  }

  /**
   * Constructor for wrapper exception with preset message.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   */
  public TsUnsupportedFeatureRtException( Throwable aCause ) {
    super( ERR_MSG_UNSUPPORTED_FEATURE, aCause );
  }

  /**
   * Constructor with preset message.
   */
  public TsUnsupportedFeatureRtException() {
    super( ERR_MSG_UNSUPPORTED_FEATURE );
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
   * @throws TsUnsupportedFeatureRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsUnsupportedFeatureRtException {
    if( !aCondition ) {
      throw new TsUnsupportedFeatureRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>false</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsUnsupportedFeatureRtException aCondition == <code>false</code>
   */
  public static void checkFalse( boolean aCondition )
      throws TsUnsupportedFeatureRtException {
    if( !aCondition ) {
      throw new TsUnsupportedFeatureRtException( ERR_MSG_UNSUPPORTED_FEATURE );
    }
  }

  /**
   * Throws an exception if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsUnsupportedFeatureRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition, String aMessageFormat, Object... aMsgArgs )
      throws TsUnsupportedFeatureRtException {
    if( aCondition ) {
      throw new TsUnsupportedFeatureRtException( aMessageFormat, aMsgArgs );
    }
  }

  /**
   * Throws an exception with preset message if condition is <code>true</code>.
   *
   * @param aCondition boolean - checked condition
   * @throws TsUnsupportedFeatureRtException aCondition == <code>true</code>
   */
  public static void checkTrue( boolean aCondition )
      throws TsUnsupportedFeatureRtException {
    if( aCondition ) {
      throw new TsUnsupportedFeatureRtException( ERR_MSG_UNSUPPORTED_FEATURE );
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
   * @throws TsUnsupportedFeatureRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsUnsupportedFeatureRtException {
    if( aRef == null ) {
      throw new TsUnsupportedFeatureRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsUnsupportedFeatureRtException aRef == <code>null</code>
   */
  public static <E> E checkNull( E aRef )
      throws TsUnsupportedFeatureRtException {
    if( aRef == null ) {
      throw new TsUnsupportedFeatureRtException( ERR_MSG_UNSUPPORTED_FEATURE );
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
   * @throws TsUnsupportedFeatureRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef, String aMessageFormat, Object... aMsgArgs )
      throws TsUnsupportedFeatureRtException {
    if( aRef != null ) {
      throw new TsUnsupportedFeatureRtException( aMessageFormat, aMsgArgs );
    }
    return aRef;
  }

  /**
   * Throws an exception with preset message if reference is <b>not</b> <code>null</code>.
   *
   * @param <E> - reference type
   * @param aRef &lt;E&gt; - checked reference
   * @return &lt;E&gt; - always returns aRef
   * @throws TsUnsupportedFeatureRtException aRef != <code>null</code>
   */
  public static <E> E checkNoNull( E aRef )
      throws TsUnsupportedFeatureRtException {
    if( aRef != null ) {
      throw new TsUnsupportedFeatureRtException( ERR_MSG_UNSUPPORTED_FEATURE );
    }
    return aRef;
  }

}
