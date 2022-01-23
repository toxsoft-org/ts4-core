package org.toxsoft.tslib.utils.errors;

import org.toxsoft.tslib.bricks.time.ITimestampable;

/**
 * Base class for all unchecked exceptions.
 * <p>
 * This class is designed only to be extended by other classes.
 *
 * @author hazard157
 */
public class TsRuntimeException
    extends RuntimeException
    implements ITimestampable {

  private static final long serialVersionUID = 157157L;

  /**
   * Creation time {@link ITimestampable#timestamp()}.
   */
  private final long timeStamp;

  /**
   * Constructor for wrapper exception.
   * <p>
   * Message string is created useing {@link String#format(String, Object...)}.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  protected TsRuntimeException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ), aCause );
    timeStamp = System.currentTimeMillis();
  }

  /**
   * Constructor for wrapper exception.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   * @param aMessage String - message string
   */
  protected TsRuntimeException( Throwable aCause, String aMessage ) {
    super( aMessage, aCause );
    timeStamp = System.currentTimeMillis();
  }

  /**
   * Constructor.
   * <p>
   * Message string is created useing {@link String#format(String, Object...)}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  protected TsRuntimeException( String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ) );
    timeStamp = System.currentTimeMillis();
  }

  /**
   * Constructor.
   *
   * @param aMessage String - message string
   */
  protected TsRuntimeException( String aMessage ) {
    super( aMessage );
    timeStamp = System.currentTimeMillis();
  }

  // --------------------------------------------------------------------------
  // ITsExceptionBase
  //

  @Override
  public long timestamp() {
    return timeStamp;
  }

}
