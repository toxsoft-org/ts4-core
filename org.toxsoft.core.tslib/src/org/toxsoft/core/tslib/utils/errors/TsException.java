package org.toxsoft.core.tslib.utils.errors;

import org.toxsoft.core.tslib.bricks.time.ITimestampable;

/**
 * Base class for all checked exceptions.
 *
 * @author hazard157
 */
public class TsException
    extends Exception
    implements ITimestampable {

  private static final long serialVersionUID = 157157L;

  /**
   * Creation time {@link ITimestampable#timestamp()}.
   */
  private final long timeStamp;

  /**
   * Constructor for wrapper exception.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aCause Throwable - cause, mey be <code>null</code>
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsException( Throwable aCause, String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ), aCause );
    timeStamp = System.currentTimeMillis();
  }

  /**
   * Constructor.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsException( String aMessageFormat, Object... aMsgArgs ) {
    super( String.format( aMessageFormat, aMsgArgs ) );
    timeStamp = System.currentTimeMillis();
  }

  // --------------------------------------------------------------------------
  // ITimestampable
  //

  @Override
  public long timestamp() {
    return timeStamp;
  }

}
