package org.toxsoft.core.txtproj.lib.workroom;

import static org.toxsoft.core.txtproj.lib.workroom.ITsResources.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Uncheckable exception when opening {@link ITsWorkroom}.
 *
 * @author hazard157
 */
public class TsWorkroomRtException
    extends TsRuntimeException {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor for wrapper exception.
   *
   * @param aCause Throwable - cause, may be <code>null</code>
   * @param aWrDir {@link File} - workroom directory
   */
  public TsWorkroomRtException( Throwable aCause, File aWrDir ) {
    super( aCause, formatMsg( aWrDir, FMT_ERR_WR_ERR_GENERAL ) );
  }

  /**
   * Constructor for wrapper exception.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aCause Throwable - cause, may be <code>null</code>
   * @param aWrDir {@link File} - workroom directory
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsWorkroomRtException( Throwable aCause, File aWrDir, String aMessageFormat, Object... aMsgArgs ) {
    super( aCause, formatMsg( aWrDir, aMessageFormat, aMsgArgs ) );
  }

  /**
   * Constructor with preset message.
   *
   * @param aWrDir {@link File} - workroom directory
   */
  public TsWorkroomRtException( File aWrDir ) {
    super( formatMsg( aWrDir, FMT_ERR_WR_ERR_GENERAL ) );
  }

  /**
   * Constructor.
   * <p>
   * Message string is created using {@link String#format(String, Object...)}.
   *
   * @param aWrDir {@link File} - workroom directory
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   */
  public TsWorkroomRtException( File aWrDir, String aMessageFormat, Object... aMsgArgs ) {
    super( formatMsg( aWrDir, aMessageFormat, aMsgArgs ) );
  }

  private static String formatMsg( File aWrDir, String aMessage, Object... aArgs ) {
    String strWsDir = Objects.toString( aWrDir );
    return String.format( "'%s': %s", strWsDir, String.format( aMessage, aArgs ) ); //$NON-NLS-1$
  }

}
