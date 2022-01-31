package org.toxsoft.core.tslib.utils.errors;

import java.util.Collection;

/**
 * Error handling utility constants and methods.
 *
 * @author hazard157
 */
public final class TsErrorUtils {

  /**
   * Checks specified array and throws exception if any condition is not met.
   *
   * @param <E> - optional type of specified array
   * @param aArrayArg E[] - array to be checked
   * @param aMinCount int - minimum number of elements in array
   * @return E[] - specified array
   * @throws TsNullArgumentRtException aArrayArg = null
   * @throws TsNullArgumentRtException array length is less than specified number of elements
   * @throws TsNullArgumentRtException at least one lelemnt of specified array is <code>null</code>
   */
  public static <E> E[] checkArrayArg( E[] aArrayArg, int aMinCount ) {
    if( aArrayArg == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aArrayArg.length < aMinCount ) {
      throw new TsIllegalArgumentRtException();
    }
    for( int i = 0, n = aArrayArg.length; i < n; i++ ) {
      if( aArrayArg[i] == null ) {
        throw new TsNullArgumentRtException();
      }
    }
    return aArrayArg;
  }

  /**
   * Checks specified array and throws exception if any condition is not met.
   *
   * @param <E> - optional type of specified array
   * @param aArrayArg E[] - array to be checked
   * @return E[] - specified array
   * @throws TsNullArgumentRtException aArrayArg = null
   * @throws TsNullArgumentRtException at least one lelemnt of specified array is <code>null</code>
   */
  public static <E> E[] checkArrayArg( E[] aArrayArg ) {
    if( aArrayArg == null ) {
      throw new TsNullArgumentRtException();
    }
    for( int i = 0, n = aArrayArg.length; i < n; i++ ) {
      if( aArrayArg[i] == null ) {
        throw new TsNullArgumentRtException();
      }
    }
    return aArrayArg;
  }

  /**
   * Checks that specified collection and any of its element is not <code>null</code>.
   *
   * @param <E> - optional type of specified collection
   * @param aColl Collection&lt;E&gt; - collection to be checked
   * @return Collection&lt;E&gt; - always returns argument aColl
   * @throws TsNullArgumentRtException aColl = null
   * @throws TsNullArgumentRtException at least one lelemnt of specified collection is <code>null</code>
   */
  public static <E> Collection<E> checkCollectionArg( Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( E e : aColl ) {
      if( e == null ) {
        throw new TsNullArgumentRtException();
      }
    }
    return aColl;
  }

  /**
   * Checks that argument is not <code>null</code> or blank string.
   * <p>
   * String is blank when {@link String#isBlank()} returns <code>true</code>.
   *
   * @param aString String - string to be checked
   * @return String - always returns argument
   * @throws TsNullArgumentRtException aString = null
   * @throws TsIllegalArgumentRtException aString is an blank string
   */
  public static String checkNonBlank( String aString ) {
    if( aString == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aString.isBlank() ) {
      throw new TsIllegalArgumentRtException();
    }
    return aString;
  }

  /**
   * Checks that argument is not <code>null</code> or empty string.
   * <p>
   * String is empty when {@link String#isEmpty()} returns <code>true</code>.
   *
   * @param aString String - string to be checked
   * @return String - always returns argument
   * @throws TsNullArgumentRtException aString = null
   * @throws TsIllegalArgumentRtException aString is an empty string
   */
  public static String checkNonEmpty( String aString ) {
    if( aString == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aString.isEmpty() ) {
      throw new TsIllegalArgumentRtException();
    }
    return aString;
  }

  /**
   * Prohibition of descendants creation.
   */
  private TsErrorUtils() {
    // nop
  }

}
