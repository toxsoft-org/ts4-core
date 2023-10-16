package org.toxsoft.core.tslib.utils.errors;

import static org.toxsoft.core.tslib.utils.errors.ITsResources.*;

import java.util.*;

import org.toxsoft.core.tslib.coll.*;

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
   * Checks that argument <code>aList</code> if of type {@link IList} and at list first item of type
   * <code>aItemClass</code>.
   *
   * @param aList Object - probable list
   * @param aItemClass {@link Class} - expected class of items
   * @return {@link IList} - argument {@link IList} as raw {@link IList}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException the type expectations are not met
   */
  @SuppressWarnings( "rawtypes" )
  public static IList checkListOfTypes( Object aList, Class<?> aItemClass ) {
    TsNullArgumentRtException.checkNulls( aList, aItemClass );
    if( aList instanceof IList ll ) {
      int count = ll.size();
      if( count > 0 ) {
        if( !aItemClass.isInstance( ll.first() ) ) {
          throw new ClassCastException( String.format( LOG_FMT_ERR_LIST_HAS_INV_ITEM, aItemClass.getName() ) );
        }
      }
      return ll;
    }
    throw new ClassCastException( String.format( LOG_FMT_ERR_ARG_IS_NOT_LIST, aList.toString() ) );
  }

  /**
   * Checks if index can be applied to a collection with the specified maximum index value.
   * <p>
   * throw error (in the order of checks):
   * <ul>
   * <li>if <code>aIndex</code> has negative value - in collections/arrays index must start from 0 and up;</li>
   * <li>if <code>aMaxIndex</code> has negative value - collection is empty and no index can by applied to it;</li>
   * <li>if <code>aIndex</code> is greater than <code>aMaxIndex</code>.</li>
   * </ul>
   * Note: <code>aMaxIndex</code> is maximum allowed value of index, not the size of a collection. Usually index maximal
   * value is one less than the collection size. However sometime (eg {@link IListEdit#insert(int, Object)}) index
   * maximum value is equal to the collection size.
   *
   * @param aMaxIndex int - maximal allowed value of the index, or negative value
   * @param aIndex int - the index to check
   */
  public static void checkCollIndex( int aMaxIndex, int aIndex ) {
    if( aIndex < 0 ) {
      throw new TsIllegalArgumentRtException( LOG_STR_ERR_INV_INDEX_NEGATIVE );
    }
    if( aMaxIndex < 0 ) {
      throw new TsIllegalArgumentRtException( LOG_STR_ERR_INV_INDEX_ON_EMPTY_COLL );
    }
    if( aIndex > aMaxIndex ) {
      throw new TsIllegalArgumentRtException( LOG_FMT_ERR_INV_INDEX_OUT_OF_RANGE, Integer.valueOf( aMaxIndex ) );
    }
  }

  /**
   * Prohibition of descendants creation.
   */
  private TsErrorUtils() {
    // nop
  }

}
