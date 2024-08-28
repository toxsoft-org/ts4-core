package org.toxsoft.core.tslib.utils;

import static org.toxsoft.core.tslib.utils.ITsResources.*;

import java.util.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods mainly to be used for testing and debugging purposes.
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
   * @throws TsNullArgumentRtException aMessageFormat = <code>null</code>
   */
  public static void p( String aMessageFormat, Object... aMsgArgs ) {
    TsNullArgumentRtException.checkNull( aMessageFormat );
    String msg = String.format( aMessageFormat, aMsgArgs );
    System.out.print( msg );
  }

  /**
   * Formatted output with line feed on end.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException aMessageFormat = <code>null</code>
   */
  public static void pl( String aMessageFormat, Object... aMsgArgs ) {
    TsNullArgumentRtException.checkNull( aMessageFormat );
    String msg = String.format( aMessageFormat, aMsgArgs );
    System.out.println( msg );
  }

  /**
   * Formatted error message output with line feed on end.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @throws TsNullArgumentRtException aMessageFormat = <code>null</code>
   */
  public static void errl( String aMessageFormat, Object... aMsgArgs ) {
    TsNullArgumentRtException.checkNull( aMessageFormat );
    String msg = String.format( MSG_ERROR_MSG_PREFIX + aMessageFormat + '\n', aMsgArgs );
    System.out.println( msg );
  }

  /**
   * Outputs the string as"as is".
   *
   * @param aStr String - the output string
   */
  public static void outStr( String aStr ) {
    System.out.println( aStr );
  }

  /**
   * Simple outputs line feed.
   */
  public static void nl() {
    System.out.println( TsLibUtils.EMPTY_STRING );
  }

  /**
   * To read input string, outputs message and waits for ENTER press.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return String - entered string or empty string (including on I/O error)
   * @throws TsNullArgumentRtException aMessageFormat = <code>null</code>
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
   * To read input string, outputs predefined message and waits for ENTER press.
   *
   * @return String - entered string
   */
  public static String waitEnter() {
    p( MSG_ENTER_TO_CONTINUE );
    return scanner.nextLine();
  }

  /**
   * Outputs message, end with " (y/n): " and returns <code>true</code> if user presses y or Y.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return boolean - <code>true</code> if user answered y (yes)
   * @throws TsNullArgumentRtException aMessageFormat = <code>null</code>
   */
  @SuppressWarnings( "nls" )
  public static boolean askConfirm( String aMessageFormat, Object aMsgArgs ) {
    String s = waitEnter( aMessageFormat + " (y/n): ", aMsgArgs );
    return s.trim().equalsIgnoreCase( "y" );
  }

  /**
   * Outputs message, end with " (y/n): " and returns <code>true</code> if user presses y or Y.
   * <p>
   * Difference between {@link #askConfirm(String, Object)} and {@link #askConfirm2(String, Object)} is in
   * interpretation of an empty input (when directly ENTER was pressed). First method returns <code>false</code>, while
   * second returns <code>true</code>.
   *
   * @param aMessageFormat String - message format string
   * @param aMsgArgs Object[] - optional arguments for message string
   * @return boolean - <code>true</code> if user answered y (yes)
   * @throws TsNullArgumentRtException aMessageFormat = <code>null</code>
   */
  @SuppressWarnings( "nls" )
  public static boolean askConfirm2( String aMessageFormat, Object aMsgArgs ) {
    String s = waitEnter( aMessageFormat + " (y/n): ", aMsgArgs );
    if( s.isEmpty() ) {
      return true;
    }
    return s.trim().equalsIgnoreCase( "y" );
  }

}
