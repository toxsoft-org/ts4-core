package org.toxsoft.core.tslib.coll.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Sorted {@link IListBasicEdit} implementation of comparable elements.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class SortedElemLinkedBundleList<E extends Comparable<? super E>>
    implements IListBasicEdit<E>, ITsFastIndexListTag<E>, ITsSortedCollectionTag, Serializable {

  // FIXME serialize as array

  private static final long serialVersionUID = 157157L;

  /**
   * Bundle, element of single-side linked list (chain) of bundles.
   * <p>
   * Attention: this internal class relies on callers and for sake of optimization does not checks arguments.
   *
   * @author hazard157
   */
  private final class Bundle
      implements Serializable {

    private static final long serialVersionUID = 157157L;

    /**
     * Array of elements in bundle, also part of elements of implemented {@link IList}.
     */
    final Object elems[];

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
      elems = new Object[aCapacity];
    }

    /**
     * Return last element in array (not last cell of array).
     *
     * @return &lt;E&gt; - simply returns {@link #elems elems}[ {@link #count count}-1 ]
     */
    @SuppressWarnings( "unchecked" )
    E lastValue() {
      return (E)elems[count - 1];
    }

    /**
     * Returns element of bundle by index.
     *
     * @param aIndex int - index in range 0..{@link #count}-1
     * @return &lt;E&gt; - simply returns {@link #elems elems[index-1]}
     */
    @SuppressWarnings( "unchecked" )
    E elem( int aIndex ) {
      return (E)elems[aIndex];
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
     * @param aValue Object - element to be inserted in bundle
     */
    public void insert( int aIndex, Object aValue ) {
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

  // ------------------------------------------------------------------------------------
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
  public SortedElemLinkedBundleList( int aBundleCapacity, boolean aAllowDuplicates ) {
    bundleCapacity = BUNDLE_CAPACITY_RANGE.inRange( aBundleCapacity );
    firstBundle = new Bundle( bundleCapacity );
    size = 0;
    allowDuplicates = aAllowDuplicates;
  }

  /**
   * Creates empty list with defaults: duplicates allowed and bundle size of {@link #DEFAULT_BUNDLE_CAPACITY}.
   */
  public SortedElemLinkedBundleList() {
    this( DEFAULT_BUNDLE_CAPACITY, true );
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {

      Bundle            currBundle          = firstBundle;
      int               currIndex           = 0;
      private final int expectedChnageCount = changeCount;

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
      public E next() {
        checkForConcurrentModification();
        if( hasNext() ) {
          E result = currBundle.elem( currIndex++ );
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
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return size;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( E aElem ) {
    return indexOf( aElem ) >= 0;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E[] toArray( E[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    E[] result = aSrcArray;
    if( aSrcArray.length < size ) {
      result = (E[])Array.newInstance( aSrcArray.getClass().getComponentType(), size );
    }
    for( int i = 0; i < size; i++ ) {
      result[i] = get( i );
    }
    for( int i = size; i < aSrcArray.length; i++ ) {
      result[i] = null;
    }
    return result;
  }

  @Override
  public Object[] toArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
    }
    Object[] result = new Object[size];
    for( int i = 0; i < size; i++ ) {
      result[i] = get( i );
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      return -1;
    }
    int index = 0;
    do {
      if( aElem.compareTo( b.lastValue() ) <= 0 ) { // aValue <= b.lastValue()
        for( int i = 0; i < b.count; i++ ) {
          if( b.elem( i ).equals( aElem ) ) { // b.elems[i] == aValue
            return i + index;
          }
          if( aElem.compareTo( b.elem( i ) ) < 0 ) { // aValue < b.elems[i]
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
  public E get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    int index = aIndex;
    do {
      if( index < b.count ) {
        return b.elem( index );
      }
      index -= b.count;
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

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

  // ------------------------------------------------------------------------------------
  // ITsCollectionEdit
  //

  @Override
  public int add( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      size = 1;
      b.count = 1;
      b.elems[0] = aElem;
      return 0;
    }
    Bundle bPrev = null;
    int index = 0;
    do {
      if( aElem.compareTo( b.lastValue() ) <= 0 ) { // aValue <= b.lastValue()
        int i;
        for( i = b.count - 1; i >= 0; i-- ) {
          if( aElem.compareTo( b.elem( i ) ) >= 0 ) { // aValue >= b.elems[i]
            if( aElem.equals( b.elem( i ) ) ) { // aValue == b.elems[i]
              if( !allowDuplicates ) {
                return i + index;
              }
            }
            break;
          }
        }
        ++size;
        // insert aElem to the place i+1
        if( b.isFull() ) {
          Bundle bNext = b.next;
          if( bNext == null || bNext.isFull() ) { // need to add bundle
            bNext = new Bundle( bundleCapacity );
            bNext.next = b.next;
            b.next = bNext;
          }
          if( i == b.count - 1 ) {
            bNext.insert( 0, aElem );
          }
          else {
            bNext.insert( 0, b.lastValue() );
            b.insert( i + 1, aElem );
          }
        }
        else {
          b.insert( i + 1, aElem );
        }
        ++changeCount;
        return i + 1 + index;
      }
      index += b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    if( bPrev.isFull() ) {
      Bundle bNew = new Bundle( bundleCapacity );
      bNew.count = 1;
      bNew.elems[0] = aElem;
      bPrev.next = bNew;
    }
    else {
      bPrev.elems[bPrev.count] = aElem;
      ++bPrev.count;
    }
    ++changeCount;
    return size++;
  }

  @Override
  public int remove( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      return -1;
    }
    Bundle bPrev = null;
    int indexBase = 0;
    do {
      if( aElem.compareTo( b.lastValue() ) <= 0 ) { // aValue <= b.lastValue()
        for( int i = 0; i < b.count; i++ ) {
          if( b.elem( i ).equals( aElem ) ) { // b.elems[i] == aValue
            --b.count;
            --size;
            if( b.count == 0 ) { // bundle becames empty, remove it
              if( bPrev == null ) {
                if( b.next != null ) {
                  firstBundle = b.next;
                }
              }
              else {
                bPrev.next = b.next;
              }
            }
            else { // shift upper part of the array down
              for( int j = i; j < b.count; j++ ) {
                b.elems[j] = b.elems[j + 1];
              }
              b.elems[b.count] = null; // clear unused reference
            }
            ++changeCount;
            return indexBase + i;
          }
        }
        return -1;
      }
      indexBase += b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    return -1;
  }

  @Override
  public E removeByIndex( int aIndex ) {
    TsIllegalStateRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    Bundle bPrev = null;
    int index = aIndex;
    do {
      if( index < b.count ) {
        E result = b.elem( index );
        --b.count;
        --size;
        if( b.count == 0 ) { // bundle becames empty, remove it
          if( bPrev == null ) {
            if( b.next != null ) {
              firstBundle = b.next;
            }
          }
          else {
            bPrev.next = b.next;
          }
        }
        else { // shift upper part of the array down
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

  // ------------------------------------------------------------------------------------
  // IListBasicEdit
  //

  // nop

}
