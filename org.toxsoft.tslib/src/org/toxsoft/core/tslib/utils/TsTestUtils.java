package org.toxsoft.core.tslib.utils;

import static org.toxsoft.core.tslib.utils.ITsResources.*;

import java.util.Scanner;

import org.toxsoft.core.tslib.utils.errors.TsErrorUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Helpers methods mainly to be used for testing and debugging purposes.
 *
 * @author hazard157
 */
public class TsTestUtils {

  private static final Scanner scanner = new Scanner( System.in );

  /**
   * Formatted output.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void p( String aMessageFormat, Object... aMsgArgs ) {
    TsNullArgumentRtException.checkNull( aMessageFormat );
    TsErrorUtils.checkArrayArg( aMsgArgs );
    String msg = String.format( aMessageFormat, aMsgArgs );
    System.out.print( msg );
  }

  /**
   * Formatted output with line feed on end.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void pl( String aMessageFormat, Object... aMsgArgs ) {
    TsNullArgumentRtException.checkNull( aMessageFormat );
    TsErrorUtils.checkArrayArg( aMsgArgs );
    String msg = String.format( aMessageFormat, aMsgArgs );
    System.out.println( msg );
  }

  /**
   * Formatted error message output with line feed on end.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void errl( String aMessageFormat, Object... aMsgArgs ) {
    TsNullArgumentRtException.checkNull( aMessageFormat );
    TsErrorUtils.checkArrayArg( aMsgArgs );
    String msg = String.format( MSG_ERROR_MSG_PREFIX + aMessageFormat + '\n', aMsgArgs );
    System.out.println( msg );
  }

  /**
   * Simple outputs line feed.
   */
  public static void nl() {
    System.out.println( TsLibUtils.EMPTY_STRING );
  }

  /**
   * Outputs message and waits for ENTER press.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return String - entered string or empty string (including on I/O error)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String waitEnter( String aMessageFormat, Object... aMsgArgs ) {
    p( aMessageFormat, aMsgArgs );
    try {
      return scanner.nextLine();
    }
    catch( @SuppressWarnings( "unused" ) Exception e ) {
      return TsLibUtils.EMPTY_STRING;
    }
  }

  /**
   * Outputs predefined message and waits for ENTER press.
   */
  public static void waitEnter() {
    p( MSG_ENTER_TO_CONTINUE );
    scanner.nextLine();
  }

  /**
   * Outputs message, end with " (y/n): " and returns <code>true</code> if user presses y or Y.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return boolean - <code>true</code> if user answered y (yes)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( "nls" )
  public static boolean askConfirm( String aMessageFormat, Object aMsgArgs ) {
    String s = waitEnter( aMessageFormat + " (y/n): ", aMsgArgs );
    return s.trim().equalsIgnoreCase( "y" );
  }

}
