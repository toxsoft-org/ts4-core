package org.toxsoft.core.tslib.coll.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Sorted list of strings.
 *
 * @author hazard157
 */
public class SortedStringLinkedBundleList
    implements IStringListBasicEdit, ITsFastIndexListTag<String>, ITsSortedCollectionTag, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Bundle, element of single-side linked list (chain) of bundles.
   * <p>
   * Attention: this internal class relies on callers and for sake of optimization does not checks arguments.
   *
   * @author hazard157
   */
  private static final class Bundle
      implements Serializable {

    private static final long serialVersionUID = 157157L;

    /**
     * Array of elements in bundle, also part of elements of implemented {@link IStringList}.
     */
    final String elems[];

    /**
     * Number of elements in bundle (number of used cells of array {@link #elems}).
     */
    int count = 0;

    /**
     * Reference to next bundle in chain.
     * <p>
     * For last bundle in chain {@link #next} = <b>null</b>.
     */
    Bundle next = null;

    /**
     * Constructor, needed for serialization.
     * <p>
     * OPTIMIZE totally rewrite serialization - serialize as array, not linked bundles
     */
    @SuppressWarnings( "unused" )
    Bundle() {
      this( DEFAULT_BUNDLE_CAPACITY );
    }

    /**
     * Creates bundle of specified capacity (internal array length).
     *
     * @param aCapacity int - specified capacity (internal array length)
     */
    Bundle( int aCapacity ) {
      elems = new String[aCapacity];
    }

    /**
     * Return last element in array (not last cell of array).
     *
     * @return String - simply returns {@link #elems elems}[ {@link #count count}-1 ]
     */
    String lastValue() {
      return elems[count - 1];
    }

    /**
     * Returns element of bundle by index.
     *
     * @param aIndex int - index in range 0..{@link #count}-1
     * @return String - simply returns {@link #elems elems[index-1]}
     */
    String elem( int aIndex ) {
      return elems[aIndex];
    }

    /**
     * Determines if bundle is empty (does not contains any elemens).
     *
     * @return <b>true</b> - bundle is empty, {@link #count} = 0;<br>
     *         <b>false</b> - there is at least one element in bundle, {@link #count} > 0.
     */
    boolean isEmpty() {
      return count == 0;
    }

    /**
     * Determines if bundle is full (all array cells are occupied by elements). Определяет, весь ли массив-хранилище
     * занято.
     *
     * @return <b>true</b> - array is full, no space for elements, {@link #count} == {@link #elems}.length;<br>
     *         <b>false</b> - there is space for at least oneelement, {@link #count} < {@link #elems} .length.
     */
    boolean isFull() {
      return count == elems.length;
    }

    /**
     * Inserts element aValue at index aIndex, shifting right part of array.
     *
     * @param aIndex int - index for element insertion in range 0..{@link #count}-1
     * @param aValue String - element to be inserted in bundle
     */
    public void insert( int aIndex, String aValue ) {
      int ceiling = count;
      if( count == elems.length ) {
        --ceiling;
      }
      for( int i = ceiling; i > aIndex; i-- ) {
        elems[i] = elems[i - 1];
      }
      elems[aIndex] = aValue;
      if( count < elems.length ) {
        ++count;
      }
    }

  }

  /**
   * <code>true</code> if duplicate items are allowed in list.
   */
  private final boolean allowDuplicates;

  /**
   * Capacity of newly created bundles.
   */
  private final int bundleCapacity;

  /**
   * Reference to first bundle in chain.
   * <p>
   * As the list eis being edited, this reference may change.
   */
  Bundle firstBundle;

  /**
   * Number of elements in this list.
   * <p>
   * This is sum of numbers of elemenets {@link Bundle#count} in all bundles.
   */
  private int size;

  /**
   * Counter of list editing operations used for concurrent access detection.
   */
  protected int changeCount = 0;

  // --------------------------------------------------------------------------
  // Creation
  //

  /**
   * Creates list with all invariant properties.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  public SortedStringLinkedBundleList( int aBundleCapacity, boolean aAllowDuplicates ) {
    TsIllegalArgumentRtException
        .checkTrue( aBundleCapacity < MIN_BUNDLE_CAPACITY || aBundleCapacity > MAX_BUNDLE_CAPACITY );
    bundleCapacity = aBundleCapacity;
    firstBundle = new Bundle( bundleCapacity );
    size = 0;
    allowDuplicates = aAllowDuplicates;
  }

  /**
   * Creates empty list with defaults: duplicates allowed and bundle size of {@link #DEFAULT_BUNDLE_CAPACITY}.
   */
  public SortedStringLinkedBundleList() {
    this( DEFAULT_BUNDLE_CAPACITY, true );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems String[] - array of list elements
   * @throws TsNullArgumentRtException aElem = <code>null</code>
   */
  public SortedStringLinkedBundleList( String... aElems ) {
    this();
    TsErrorUtils.checkArrayArg( aElems );
    for( int i = 0; i < aElems.length; i++ ) {
      add( aElems[i] );
    }
  }

  /**
   * Copy constructor.
   *
   * @param aSrc {@link IStringList} - source list
   * @throws TsNullArgumentRtException aSrc = null
   */
  public SortedStringLinkedBundleList( IStringList aSrc ) {
    this();
    addAll( aSrc );
  }

  // ------------------------------------------------------------------------------------
  // IList<E>
  //

  @Override
  public boolean isEmpty() {
    return firstBundle.isEmpty();
  }

  @Override
  public String get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    int index = aIndex;
    do { // iterate through chain of bundles
      if( index < b.count ) {
        return b.elem( index );
      }
      index -= b.count; // bundle passed - take into account the number of elements in bundle
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  @Override
  public boolean hasElem( String aValue ) {
    return indexOf( aValue ) >= 0;
  }

  @Override
  public int indexOf( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      return -1;
    }
    int index = 0;
    do { // iterate through chain of bundles
      // determine if specified element is in this bundle
      if( aValue.compareTo( b.lastValue() ) <= 0 ) { // aValue <= b.lastValue()
        // simply iterate inside bundle
        for( int i = 0; i < b.count; i++ ) {
          String s = b.elem( i );
          if( s.hashCode() == aValue.hashCode() ) { // optimize: don't call equals() for non-equal strings
            if( s.equals( aValue ) ) { // b.elems[i] == aValue
              return i + index;
            }
          }
          if( aValue.compareTo( s ) < 0 ) { // aValue < b.elems[i]
            return -1;
          }
        }
        return -1;
      }
      index += b.count;
      b = b.next;
    } while( b != null );
    return -1;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public String[] toArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
    }
    String[] result = new String[size];
    for( int i = 0; i < size; i++ ) {
      result[i] = get( i );
    }
    return result;
  }

  @Override
  public String[] toArray( String[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    String[] destArray = aSrcArray;
    if( destArray.length < size ) {
      destArray = new String[size];
    }
    int i;
    for( i = 0; i < size; i++ ) {
      destArray[i] = get( i );
    }
    for( ; i < destArray.length; i++ ) {
      destArray[i] = null;
    }
    return destArray;
  }

  @Override
  public Iterator<String> iterator() {
    return new Iterator<>() {

      private final int expectedChnageCount = changeCount;

      Bundle currBundle = firstBundle;
      int    currIndex  = 0;

      private void checkForConcurrentModification() {
        if( expectedChnageCount != changeCount ) {
          throw new ConcurrentModificationException();
        }
      }

      @Override
      public boolean hasNext() {
        checkForConcurrentModification();
        return currBundle.next != null || currIndex < currBundle.count;
      }

      @Override
      public String next() {
        checkForConcurrentModification();
        if( hasNext() ) {
          String result = currBundle.elem( currIndex++ );
          if( currIndex >= currBundle.count && currBundle.next != null ) {
            currBundle = currBundle.next;
            currIndex = 0;
          }
          return result;
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

  // ------------------------------------------------------------------------------------
  // IStringListBasicEdit
  //

  @Override
  public int add( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    Bundle b = firstBundle;
    if( b.isEmpty() ) { // adding to empty list/bundle
      size = 1;
      b.count = 1;
      b.elems[0] = aValue;
      return 0;
    }
    Bundle bPrev = null;
    int index = 0;
    do { // iterate throught chain of bundles until found bundle for element insertion
      if( aValue.compareTo( b.lastValue() ) <= 0 ) { // aValue <= b.lastValue()
        int i;
        // bundle is found, searching for index i for insertion
        for( i = b.count - 1; i >= 0; i-- ) {
          if( aValue.compareTo( b.elem( i ) ) >= 0 ) { // aValue >= b.elems[i]
            if( aValue.equals( b.elem( i ) ) ) { // aValue == b.elems[i]
              if( !allowDuplicates ) {
                return i + index;
              }
            }
            break;
          }
        }
        ++size;
        // inserting aVlaue at index i+1
        if( b.isFull() ) {
          Bundle bNext = b.next;
          if( bNext == null || bNext.isFull() ) { // new bundle is needed
            bNext = new Bundle( bundleCapacity );
            bNext.next = b.next;
            b.next = bNext;
          }
          if( i == b.count - 1 ) {
            bNext.insert( 0, aValue );
          }
          else {
            bNext.insert( 0, b.lastValue() );
            b.insert( i + 1, aValue );
          }
        }
        else {
          b.insert( i + 1, aValue );
        }
        ++changeCount;
        return i + 1 + index;
      }
      index += b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    // here we found, that element is to be inserted at the and of the last bundle which may be full
    if( bPrev.isFull() ) {
      Bundle bNew = new Bundle( bundleCapacity );
      bNew.count = 1;
      bNew.elems[0] = aValue;
      bPrev.next = bNew;
    }
    else {
      bPrev.elems[bPrev.count] = aValue;
      ++bPrev.count;
    }
    ++changeCount;
    return size++;
  }

  @Override
  public void addAll( String... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      add( aArray[i] );
    }
  }

  @Override
  public void addAll( ITsCollection<String> aElemList ) {
    TsNullArgumentRtException.checkNull( aElemList );

    if( aElemList instanceof ITsFastIndexListTag ) {
      ITsFastIndexListTag<String> src = (ITsFastIndexListTag<String>)aElemList;
      for( int i = 0, n = src.size(); i < n; i++ ) {
        add( src.get( i ) );
      }
    }
    else {
      for( String e : aElemList ) {
        add( e );
      }
    }
  }

  @Override
  public void addAll( Collection<String> aStrColl ) {
    TsNullArgumentRtException.checkNull( aStrColl );
    for( String s : aStrColl ) {
      TsNullArgumentRtException.checkNull( s );
      add( s );
    }
  }

  @Override
  public void setAll( String... aStrings ) {
    TsNullArgumentRtException.checkNull( aStrings );
    clear();
    addAll( aStrings );
  }

  @Override
  public void setAll( ITsCollection<String> aStringList ) {
    TsNullArgumentRtException.checkNull( aStringList );
    clear();
    addAll( aStringList );
  }

  @Override
  public void setAll( Collection<String> aStrColl ) {
    TsNullArgumentRtException.checkNull( aStrColl );
    clear();
    addAll( aStrColl );
  }

  @Override
  public void clear() {
    if( size != 0 ) {
      firstBundle.next = null;
      firstBundle.count = 0;
      Arrays.fill( firstBundle.elems, null );
      size = 0;
      ++changeCount;
    }
  }

  @Override
  public String removeByIndex( int aIndex ) {
    TsIllegalStateRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    Bundle bPrev = null;
    int index = aIndex;
    do {
      // seraching bundle containing element with index aIndex
      if( index < b.count ) {
        String result = b.elem( index );
        --b.count;
        --size;
        if( b.count == 0 ) { // связка опустела
          if( bPrev == null ) {
            if( b.next != null ) {
              firstBundle = b.next;
            }
          }
          else {
            bPrev.next = b.next;
          }
        }
        else { // shifting down upper part of the array
          for( int j = index; j < b.count; j++ ) {
            b.elems[j] = b.elems[j + 1];
          }
          b.elems[b.count] = null; // clear unused reference
        }
        ++changeCount;
        return result;
      }
      index -= b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aCount < 0 );
    TsIllegalArgumentRtException.checkFalse( aIndex >= size() );
    if( aCount == 0 ) {
      return;
    }
    TsIllegalArgumentRtException.checkFalse( aIndex + aCount >= size() );
    // OPTIMIZE needs to be optimized
    for( int i = 0; i < aCount; i++ ) {
      removeByIndex( aIndex );
    }
  }

  @Override
  public int remove( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      return -1;
    }
    Bundle bPrev = null;
    int indexOfBundle0 = 0;
    do { // iterate throught chainof bundles
      // determine if specified element is in this bundle
      if( aValue.compareTo( b.lastValue() ) <= 0 ) { // aValue <= b.lastValue()
        // simply iterate inside bundle
        for( int i = 0; i < b.count; i++ ) {
          if( b.elem( i ).equals( aValue ) ) { // b.elems[i] == aValue
            --b.count;
            --size;
            if( b.count == 0 ) { // bundle becames empty
              if( bPrev == null ) {
                if( b.next != null ) {
                  firstBundle = b.next;
                }
              }
              else {
                bPrev.next = b.next;
              }
            }
            else { // shift down upper part of the array
              for( int j = i; j < b.count; j++ ) {
                b.elems[j] = b.elems[j + 1];
              }
              b.elems[b.count] = null; // clear unused reference
            }
            ++changeCount;
            return i + indexOfBundle0;
          }
          if( aValue.compareTo( b.elem( i ) ) < 0 ) { // Value < b.elems[i]
            return -1;
          }
        }
        return -1;
      }
      indexOfBundle0 += b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    return -1;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof IStringList ) {
      return TsCollectionsUtils.isListsEqual( this, (IStringList)aObj );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( int i = 0, n = size(); i < n; i++ ) {
      result = TsLibUtils.PRIME * result + get( i ).hashCode();
    }
    return result;
  }

  // FIXME serialize as array

}
