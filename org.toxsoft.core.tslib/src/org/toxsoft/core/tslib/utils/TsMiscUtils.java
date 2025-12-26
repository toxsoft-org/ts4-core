package org.toxsoft.core.tslib.utils;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;
import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Miscellaneous utility methods.
 *
 * @author hazard157
 */
public class TsMiscUtils {

  // ------------------------------------------------------------------------------------
  // OSGi/Eclipse/E4
  //

  /**
   * Makes Eclipse OSGi/E4 class contribution IRI string.
   *
   * @param aPluginId String - ID of the plugin containing contributed class
   * @param aClass {@link Class} - the contributed class
   * @return String - the contribution URI string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException plugin ID is a blank string
   */
  public static String makeContributionUri( String aPluginId, Class<?> aClass ) {
    TsErrorUtils.checkNonBlank( aPluginId );
    TsNullArgumentRtException.checkNull( aClass );
    return "bundleclass://" + aPluginId + '/' + aClass.getName(); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // Bits manipulation
  //

  /**
   * Checks if any bit is set in a word.
   *
   * @param aFlags int - the word to check
   * @param aBitsMask long - the bits mask
   * @return boolean - <code>true</code> if any of the masked bits are set
   */
  public static boolean isBitAny( long aFlags, long aBitsMask ) {
    return (aFlags & aBitsMask) != 0;
  }

  /**
   * Checks if all bit is set in a word.
   *
   * @param aFlags long - the word to check
   * @param aBitsMask long - the bits mask
   * @return boolean - <code>true</code> if all of the masked bits are set
   */
  public static boolean isBitsAll( long aFlags, long aBitsMask ) {
    return (aFlags & aBitsMask) == aBitsMask;
  }

  /**
   * Checks if any bit is set in a word.
   *
   * @param aFlags int - the word to check
   * @param aBitsMask int - the bits mask
   * @return boolean - <code>true</code> if any of the masked bits are set
   */
  public static boolean isBitAny( int aFlags, int aBitsMask ) {
    return (aFlags & aBitsMask) != 0;
  }

  /**
   * Checks if all bit is set in a word.
   *
   * @param aFlags int - the word to check
   * @param aBitsMask int - the bits mask
   * @return boolean - <code>true</code> if all of the masked bits are set
   */
  public static boolean isBitsAll( int aFlags, int aBitsMask ) {
    return (aFlags & aBitsMask) == aBitsMask;
  }

  // ------------------------------------------------------------------------------------
  // Running programs
  //

  /**
   * Runs external program and does not waits for it.
   *
   * @param aProgramName String - program name
   * @param aArgs String[] - command line arguments
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException empty name
   * @throws TsIoRtException error running program
   */
  public static void runProgram( String aProgramName, String... aArgs ) {
    TsNullArgumentRtException.checkNull( aProgramName );
    TsErrorUtils.checkArrayArg( aArgs );
    TsIllegalArgumentRtException.checkTrue( aProgramName.length() == 0 );
    try {
      String[] cmdarr = new String[1 + aArgs.length];
      cmdarr[0] = aProgramName;
      for( int i = 0; i < aArgs.length; i++ ) {
        cmdarr[i + 1] = aArgs[i];
      }
      ProcessBuilder processBuilder = new ProcessBuilder( cmdarr );
      @SuppressWarnings( "unused" )
      Process p = processBuilder.start();
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex, aProgramName );
    }
  }

  /**
   * Runs the specified program and waits until it finishes.
   *
   * @param aTimeoutSecs int - waiting timeout seconds
   * @param aProgramName String - program name
   * @param aArgs String[] - command line arguments
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException empty name
   * @throws TsIoRtException error running program
   */
  public static void runAndWait( int aTimeoutSecs, String aProgramName, String... aArgs ) {
    TsNullArgumentRtException.checkNull( aProgramName );
    TsErrorUtils.checkArrayArg( aArgs );
    TsIllegalArgumentRtException.checkTrue( aProgramName.length() == 0 );
    try {
      String[] cmdarr = new String[1 + aArgs.length];
      cmdarr[0] = aProgramName;
      for( int i = 0; i < aArgs.length; i++ ) {
        cmdarr[i + 1] = aArgs[i];
      }
      ProcessBuilder processBuilder = new ProcessBuilder( cmdarr );
      Process p = processBuilder.start();
      p.waitFor( aTimeoutSecs, TimeUnit.SECONDS );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex, aProgramName );
    }
  }

  // TODO TRANSLATE

  /**
   * Запускает внешенюю программу и возвращает его консольный выход.
   *
   * @param aProgramName String - имя программы (полный путь или имя для поиска в пути PATH)
   * @param aArgs String[] - аргументы комндной строки
   * @return {@link Pair}&lt;IStringList,IStringList&gt; - пара выходов консоль/ошибки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException имя программы пустая строка
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  public static Pair<IStringList, IStringList> runTool( String aProgramName, String... aArgs ) {
    TsNullArgumentRtException.checkNull( aProgramName );
    TsErrorUtils.checkArrayArg( aArgs );
    TsIllegalArgumentRtException.checkTrue( aProgramName.length() == 0 );
    try {
      String[] cmdarr = new String[1 + aArgs.length];
      cmdarr[0] = aProgramName;
      for( int i = 0; i < aArgs.length; i++ ) {
        cmdarr[i + 1] = aArgs[i];
      }
      // запуск программы
      Process proc = Runtime.getRuntime().exec( cmdarr );
      proc.waitFor();
      String s = null;
      // стандартный выход
      BufferedReader stdInput = new BufferedReader( new InputStreamReader( proc.getInputStream() ) );
      IStringListEdit slOut = new StringLinkedBundleList();
      while( (s = stdInput.readLine()) != null ) {
        slOut.add( s );
      }
      // выход ошибок
      BufferedReader stdError = new BufferedReader( new InputStreamReader( proc.getErrorStream() ) );
      IStringListEdit slErr = new StringLinkedBundleList();
      while( (s = stdError.readLine()) != null ) {
        slErr.add( s );
      }
      return new Pair<>( slOut, slErr );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex, aProgramName );
    }
  }

  /**
   * Returns the value within the specified range.
   * <p>
   * If the argument value is between <code>aMin</code> and <code>aMax</code>, it is returned. If value is outside the
   * boundary, the bounday value ius returned.
   *
   * @param aValue int - the value
   * @param aMin int - minimal allowed values
   * @param aMax int - maximal allowed values
   * @return int - the value in range aMin..aMax
   * @throws TsIllegalArgumentRtException maximum value is less than minimal
   */
  public static int inRange( int aValue, int aMin, int aMax ) {
    TsIllegalArgumentRtException.checkTrue( aMax < aMin );
    if( aValue < aMin ) {
      return aMin;
    }
    if( aValue > aMax ) {
      return aMax;
    }
    return aValue;
  }

  /**
   * Checks if the specified value is in specified range.
   *
   * @param aValue int - the value
   * @param aMin int - minimal allowed values
   * @param aMax int - maximal allowed values
   * @return boolean - true if <code>aValue</code> is in range <code>aMin..aMax</code>
   * @throws TsIllegalArgumentRtException maximum value is less than minimal
   */
  public static boolean isInRange( int aValue, int aMin, int aMax ) {
    TsIllegalArgumentRtException.checkTrue( aMax < aMin );
    if( aValue < aMin || aValue > aMax ) {
      return false;
    }
    return true;
  }

  /**
   * Возвращает однострочное представление аргумента в виде содержимого строки-в-кавычках.
   * <p>
   * Внимание: возвращаемая строка не содержит начальную и конечные символы кавычек.
   * <p>
   * Quoted string has very simple representation - most characters except {@link IStrioHardConstants#CHAR_ESCAPE} and
   * {@link IStrioHardConstants#CHAR_QUOTE} are stored "as is". This two characters are preceded (escaped) by the
   * {@link IStrioHardConstants#CHAR_ESCAPE} character. New line and line feed are replaced by "\n" and "\r" strings. So
   * argument string is always represented by the single-line text placed between quotes
   * ({@link IStrioHardConstants#CHAR_QUOTE} characters). No UTF complexity is considered...
   *
   * @param aString String - the source text
   * @return String - single line of text as described above
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String toQuotedLine( String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    int argLen = aString.length();
    if( argLen == 0 ) {
      return EMPTY_STRING;
    }
    StringBuilder sb = new StringBuilder( argLen + 8 );
    for( int i = 0; i < aString.length(); i++ ) {
      char ch = aString.charAt( i );
      if( ch == CHAR_QUOTE || ch == CHAR_ESCAPE ) {
        sb.append( CHAR_ESCAPE );
      }
      if( ch == '\n' ) {
        sb.append( '\\' );
        sb.append( 'n' );
      }
      else {
        if( ch == '\r' ) {
          sb.append( '\\' );
          sb.append( 'r' );
        }
        else {
          sb.append( ch );
        }
      }
    }
    return sb.toString();
  }

  /**
   * Восстанавливает обычную строку из однострочной строки-в-кавычках.
   * <p>
   * Внимение: аргмент <b>не</b> должен содержать начальную и конечные символы кавычек.
   *
   * @param aLine String - содержимое строка-в-кавычках, полученное методом {@link #toQuotedLine(String)}
   * @return String - восстановленная строка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргемнт наруфает формат строки-в-кавычках
   */
  public static String fromQuotedLine( String aLine ) {
    TsNullArgumentRtException.checkNull( aLine );
    int argLen = aLine.length();
    if( argLen == 0 ) {
      return EMPTY_STRING;
    }
    StringBuilder sb = new StringBuilder( argLen + 8 );
    for( int i = 0; i < argLen; i++ ) {
      char ch = aLine.charAt( i );
      if( ch == CHAR_ESCAPE ) {
        TsIllegalArgumentRtException.checkTrue( i == argLen - 1 );
        ch = aLine.charAt( i++ );
        switch( ch ) {
          case CHAR_ESCAPE, CHAR_QUOTE -> sb.append( ch );
          case 'n' -> sb.append( '\n' );
          case 'r' -> sb.append( '\r' );
          default -> {
            sb.append( CHAR_ESCAPE );
            sb.append( ch );
          }
        }

      }
      else {
        sb.append( ch );
      }
    }
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // Miscellaneous
  //

  /**
   * Compares two comparable objects even if any of them is <code>null</code>ll.
   * <p>
   * <code>null</code> reference is considered "less" than any non-<code>null</code> reference.
   *
   * @param <T> - comparable objects type
   * @param aA &lt;T&gt; - an object
   * @param aB &lt;T&gt; - an object to be compared with <code>aA</code>
   * @return int - the comparison result
   */
  public static <T extends Comparable<T>> int compare( T aA, T aB ) {
    if( aA == aB ) {
      return 0;
    }
    if( aA == null || aB == null ) {
      return aA == null ? -1 : 1;
    }
    return aA.compareTo( aB );
  }

  /**
   * Returns hexadecimal string representation of the argument.
   * <p>
   * first byte in array will make be two first <b>char</b>s of the resulting string, last byte - last 2
   * <code>char</code>s.
   * <p>
   * If argument is an empty array returns an empty string. Returned {@link String#length()} is always twice the length
   * of the array.
   *
   * @param aArray byte[] - array of bytes
   * @return String - hexadecimal representation of the array
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String bytesToHexStr( byte[] aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    if( aArray.length == 0 ) {
      return TsLibUtils.EMPTY_STRING;
    }
    StringBuilder sb = new StringBuilder( aArray.length * 2 );
    for( byte b : aArray ) {
      sb.append( String.format( "%02x", Byte.valueOf( b ) ) ); //$NON-NLS-1$
    }
    return sb.toString();
  }

  /**
   * Calculates the similarity (a number within 0.0 and 1.0) between two strings.
   *
   * @param aStr1 String - first string
   * @param aStr2 String - first string
   * @return double - string similarity measure (in range 0.0 ... 1.0)
   */
  public static double stringSimilarity( String aStr1, String aStr2 ) {
    String longer = aStr1, shorter = aStr2;
    if( aStr1.length() < aStr2.length() ) { // longer should always have greater length
      longer = aStr2;
      shorter = aStr1;
    }
    int longerLength = longer.length();
    if( longerLength == 0 ) { // both strings are zero length
      return 1.0;
    }
    longer = longer.toLowerCase();
    shorter = shorter.toLowerCase();
    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    int[] costs = new int[shorter.length() + 1];
    for( int i = 0; i <= longer.length(); i++ ) {
      int lastValue = i;
      for( int j = 0; j <= shorter.length(); j++ ) {
        if( i == 0 ) {
          costs[j] = j;
        }
        else {
          if( j > 0 ) {
            int newValue = costs[j - 1];
            if( longer.charAt( i - 1 ) != shorter.charAt( j - 1 ) ) {
              newValue = Math.min( Math.min( newValue, lastValue ), costs[j] ) + 1;
            }
            costs[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if( i > 0 ) {
        costs[shorter.length()] = lastValue;
      }
    }
    //
    int editDistance = costs[shorter.length()];
    return (longerLength - editDistance) / (double)longerLength;
  }

  /**
   * Prohibition of subclass creation.
   */
  private TsMiscUtils() {
    // nop
  }

}
