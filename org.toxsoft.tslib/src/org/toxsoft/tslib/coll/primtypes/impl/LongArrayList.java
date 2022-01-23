package org.toxsoft.tslib.coll.primtypes.impl;

import static org.toxsoft.tslib.coll.impl.TsCollectionsUtils.*;
import static org.toxsoft.tslib.utils.TsLibUtils.*;

import java.io.Serializable;
import java.util.*;

import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.coll.primtypes.ILongList;
import org.toxsoft.tslib.coll.primtypes.ILongListEdit;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link ILongListEdit} implementation as single array of <code>long</code>s.
 * <p>
 * During this list lifecycle length of internal array only increases the only way to decrease used memory is method
 * {@link #truncate()}.
 *
 * @author hazard157
 */
public class LongArrayList
    implements ILongListEdit, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Array of list elements.
   * <p>
   * Capacity is <code>items.length</code> and {@link #size} of elements started from index 0 are used.
   */
  private long[] items = EMPTY_ARRAY_OF_LONGS;

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
  public LongArrayList() {
    ensureCapacity( DEFAULT_ARRAY_LIST_CAPACITY );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems long[] - array of list elements
   * @throws TsNullArgumentRtException aElem = <code>null</code>
   */
  public LongArrayList( long... aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    size = aElems.length;
    if( size > 0 ) {
      items = new long[size];
      for( int i = 0; i < size; i++ ) {
        items[i] = aElems[i];
      }
    }
  }

  /**
   * Creates default list with initial content.
   *
   * @param aSource {@link Collection}&lt;Long&gt; - collection of list elements
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException source collection or any element = null
   */
  public LongArrayList( Collection<Long> aSource ) {
    this();
    addAll( aSource );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsCollection}&lt;Long&gt; - source collection
   * @throws TsNullArgumentRtException aSource = null
   */
  public LongArrayList( ITsCollection<Long> aSource ) {
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
  public boolean hasElem( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return hasValue( aElem.longValue() );
  }

  @Override
  public Long[] toArray( Long[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( size == 0 ) {
      return aSrcArray;
    }
    Long[] result;
    if( aSrcArray.length < size ) {
      result = new Long[size];
    }
    else {
      result = aSrcArray;
    }
    for( int i = 0; i < size; i++ ) {
      result[i] = Long.valueOf( items[i] );
    }
    return result;
  }

  @Override
  public Object[] toArray() {
    return toArray( TsLibUtils.EMPTY_ARRAY_OF_LONG_OBJS );
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return indexOfValue( aElem.longValue() );
  }

  @Override
  public Long get( int aIndex ) {
    return Long.valueOf( getValue( aIndex ) );
  }

  // ------------------------------------------------------------------------------------
  // ILongList
  //

  @Override
  public boolean hasValue( long aValue ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] == aValue ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOfValue( long aValue ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] == aValue ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public long getValue( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    return items[aIndex];
  }

  @Override
  public long[] toValuesArray() {
    if( size == 0 ) {
      return EMPTY_ARRAY_OF_LONGS;
    }
    long[] result = new long[size];
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
  // ILongListBasicEdit
  //

  @Override
  public int add( long aValue ) {
    ensureCapacity( size + 1 );
    ++changeCount;
    items[size] = aValue;
    return size++;
  }

  @Override
  public int removeValue( long aValue ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] == aValue ) {
        removeValueByIndex( i );
        return i;
      }
    }
    return -1;
  }

  @Override
  public long removeValueByIndex( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    long result = items[aIndex];
    System.arraycopy( items, aIndex + 1, items, aIndex, size - aIndex - 1 );
    --size;
    ++changeCount;
    return result;
  }

  // ------------------------------------------------------------------------------------
  // ILongListEdit
  //

  @Override
  public long set( int aIndex, long aValue ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    long result = items[aIndex];
    items[aIndex] = aValue;
    ++changeCount;
    return result;
  }

  @Override
  public void insert( int aIndex, long aValue ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size );
    long[] src = items;
    long[] dest = items;
    if( size == items.length ) {
      dest = new long[recommendNewLength()];
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
      long[] newItems = new long[newLength];
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
    items = TsLibUtils.EMPTY_ARRAY_OF_LONGS;
    ++changeCount;
  }

  /**
   * Returns reference to internal array.
   * <p>
   * Warning: changing array elements will change list content! This method is made public only for library developers.
   * The single prupose of method is optimization of some internal library routines.
   *
   * @return &lt;long&gt;[] - internall array of elements
   */
  public long[] getInternalArray() {
    return items;
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<Long> iterator() {
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
      public Long next() {
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
    if( aObj instanceof ILongList ) {
      return TsCollectionsUtils.isListsEqual( this, (ILongList)aObj );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    for( int i = 0, n = size(); i < n; i++ ) {
      long value = items[i];
      result = PRIME * result + (int)(value ^ (value >>> 32));
    }
    return result;
  }

}
