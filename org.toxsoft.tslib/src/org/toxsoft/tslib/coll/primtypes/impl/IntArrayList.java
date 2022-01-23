package org.toxsoft.tslib.coll.primtypes.impl;

import static org.toxsoft.tslib.coll.impl.TsCollectionsUtils.*;
import static org.toxsoft.tslib.utils.TsLibUtils.*;

import java.io.Serializable;
import java.util.*;

import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.coll.primtypes.IIntList;
import org.toxsoft.tslib.coll.primtypes.IIntListEdit;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IIntListEdit} implementation as single array of <code>int</code>s.
 * <p>
 * During this list lifecycle length of internal array only increases the only way to decrease used memory is method
 * {@link #truncate()}.
 *
 * @author hazard157
 */
public class IntArrayList
    implements IIntListEdit, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Array of list elements.
   * <p>
   * Capacity is <code>items.length</code> and {@link #size} of elements started from index 0 are used.
   */
  private int[] items = EMPTY_ARRAY_OF_INTS;

  /**
   * Number of elements in this list.
   */
  private int size = 0;

  /**
   * Counter of list editing operations used for concurrent access detection.
   */
  protected int changeCount = 0;

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates empty list with capacity of {@link TsCollectionsUtils#DEFAULT_ARRAY_LIST_CAPACITY}.
   */
  public IntArrayList() {
    ensureCapacity( DEFAULT_ARRAY_LIST_CAPACITY );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems int[] - array of list elements
   * @throws TsNullArgumentRtException aElem = <code>null</code>
   */
  public IntArrayList( int... aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    size = aElems.length;
    if( size > 0 ) {
      items = new int[size];
      for( int i = 0; i < size; i++ ) {
        items[i] = aElems[i];
      }
    }
  }

  /**
   * Creates default list with initial content.
   *
   * @param aSource {@link Collection}&lt;Integer&gt; - collection of list elements
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException source collection or any element = null
   */
  public IntArrayList( Collection<Integer> aSource ) {
    this();
    addAll( aSource );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsCollection}&lt;Integer&gt; - source collection
   * @throws TsNullArgumentRtException aSource = null
   */
  public IntArrayList( ITsCollection<Integer> aSource ) {
    this();
    addAll( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Calculates recommended new capcaity of the array when adding new elements.
   *
   * @return int - new reommended length - 2 time larger than current array
   */
  private int recommendNewLength() {
    int recmmendedLength = items.length + items.length;
    if( recmmendedLength < MIN_ARRAY_LIST_CAPACITY ) {
      recmmendedLength = MIN_ARRAY_LIST_CAPACITY;
    }
    return recmmendedLength;
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
  public boolean hasElem( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return hasValue( aElem.intValue() );
  }

  @Override
  public Integer[] toArray( Integer[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( size == 0 ) {
      return aSrcArray;
    }
    Integer[] result;
    if( aSrcArray.length < size ) {
      result = new Integer[size];
    }
    else {
      result = aSrcArray;
    }
    for( int i = 0; i < size; i++ ) {
      result[i] = Integer.valueOf( items[i] );
    }
    return result;
  }

  @Override
  public Object[] toArray() {
    return toArray( EMPTY_ARRAY_OF_INT_OBJS );
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return indexOfValue( aElem.intValue() );
  }

  @Override
  public Integer get( int aIndex ) {
    return Integer.valueOf( getValue( aIndex ) );
  }

  // ------------------------------------------------------------------------------------
  // IIntList
  //

  @Override
  public boolean hasValue( int aValue ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] == aValue ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOfValue( int aValue ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] == aValue ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int getValue( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    return items[aIndex];
  }

  @Override
  public int[] toValuesArray() {
    if( size == 0 ) {
      return EMPTY_ARRAY_OF_INTS;
    }
    int[] result = new int[size];
    System.arraycopy( items, 0, result, 0, size );
    return result;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    size = 0;
    ++changeCount;
  }

  // ------------------------------------------------------------------------------------
  // IIntListBasicEdit
  //

  @Override
  public int add( int aValue ) {
    ensureCapacity( size + 1 );
    ++changeCount;
    items[size] = aValue;
    return size++;
  }

  @Override
  public int removeValue( int aValue ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] == aValue ) {
        removeValueByIndex( i );
        return i;
      }
    }
    return -1;
  }

  @Override
  public int removeValueByIndex( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    int result = items[aIndex];
    System.arraycopy( items, aIndex + 1, items, aIndex, size - aIndex - 1 );
    --size;
    ++changeCount;
    return result;
  }

  // ------------------------------------------------------------------------------------
  // IIntListEdit
  //

  @Override
  public int set( int aIndex, int aValue ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    int result = items[aIndex];
    items[aIndex] = aValue;
    ++changeCount;
    return result;
  }

  @Override
  public void insert( int aIndex, int aValue ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size );
    int[] src = items;
    int[] dest = items;
    if( size == items.length ) {
      dest = new int[recommendNewLength()];
      System.arraycopy( src, 0, dest, 0, aIndex );
    }
    System.arraycopy( src, aIndex, dest, aIndex + 1, size - aIndex );
    dest[aIndex] = aValue;
    items = dest;
    ++size;
    ++changeCount;
  }

  // --------------------------------------------------------------------------
  // Implementation specific additional API
  //

  /**
   * Increases (if required) intrenal array length to not less the specified capoacity.
   * <p>
   * Ths ize and contentof list remains the same.
   *
   * @param aCapacity int - requested minmal length of internal array
   * @throws TsIllegalArgumentRtException aCapacity < 0
   */
  public void ensureCapacity( int aCapacity ) {
    TsIllegalArgumentRtException.checkTrue( aCapacity < 0 );
    if( aCapacity > items.length ) {
      int newLength = recommendNewLength();
      if( newLength < aCapacity ) {
        newLength = aCapacity;
      }
      int[] newItems = new int[newLength];
      System.arraycopy( items, 0, newItems, 0, items.length );
      items = newItems;
    }
  }

  /**
   * Removes all elements from list and truncates internall array.
   * <p>
   * This is the only way to <b>decrease</b> ths size of internal array.
   */
  public void truncate() {
    size = 0;
    items = EMPTY_ARRAY_OF_INTS;
    ++changeCount;
  }

  /**
   * Returns reference to internal array.
   * <p>
   * Warning: changing array elements will change list content! This method is made public only for library developers.
   * The single prupose of method is optimization of some internal library routines.
   *
   * @return &lt;int&gt;[] - internall array of elements
   */
  public int[] getInternalArray() {
    return items;
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<Integer> iterator() {
    return new Iterator<>() {

      int               index               = 0;
      private final int expectedChangeCount = changeCount;

      private void checkForConcurrentModification() {
        if( expectedChangeCount != changeCount ) {
          throw new ConcurrentModificationException();
        }
      }

      @Override
      public boolean hasNext() {
        checkForConcurrentModification();
        return index < size();
      }

      @Override
      public Integer next() {
        if( hasNext() ) {
          return get( index++ );
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
    if( aObj instanceof IIntList ) {
      return TsCollectionsUtils.isListsEqual( this, (IIntList)aObj );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    for( int i = 0, n = size(); i < n; i++ ) {
      int value = items[i];
      result = PRIME * result + (value ^ (value >>> 32));
    }
    return result;
  }

}
