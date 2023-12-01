package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base of balanced {@link ILongList} implementation as linked list of arrays (bundles).
 *
 * @author hazard157
 */
abstract class AbstractLongBundledList
    implements ILongListBasicEdit, ITsFastIndexListTag<Long>, Serializable {

  // FIXME serialize as array

  private static final long serialVersionUID = 158158L;

  /**
   * Bundleed array of longs, chained as linked list.
   *
   * @author hazard157
   */
  protected static final class Bundle {

    final long elems[];
    int        count = 0;
    Bundle     next  = null;

    Bundle( int aCapacity ) {
      elems = new long[aCapacity];
    }

    long lastValue() {
      return elems[count - 1];
    }

    boolean isEmpty() {
      return count == 0;
    }

    boolean isFull() {
      return count == elems.length;
    }

    long remove( int aIndex ) {
      long result = elems[aIndex];
      for( int i = aIndex + 1; i < count; i++ ) {
        elems[i - 1] = elems[i];
      }
      --count;
      return result;
    }

    void insert( int aIndex, long aValue ) {
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

  private transient int      bundleCapacity;
  private transient boolean  allowDuplicates;
  protected transient Bundle firstBundle;
  protected transient int    size;

  transient int changeCount = 0; // Counter of list editing operations used for concurrent access detection

  /**
   * Constructor for descendants.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  protected AbstractLongBundledList( int aBundleCapacity, boolean aAllowDuplicates ) {
    init( aBundleCapacity, aAllowDuplicates );
  }

  protected void init( int aBundleCapacity, boolean aAllowDuplicates ) {
    bundleCapacity = BUNDLE_CAPACITY_RANGE.inRange( aBundleCapacity );
    firstBundle = createBundle();
    size = 0;
    allowDuplicates = aAllowDuplicates;
  }

  // ------------------------------------------------------------------------------------
  // API for descendants
  //

  /**
   * Returns <code>true</code> if duplicate elements are allowed in list.
   *
   * @return boolean - dulicates allowed sign<br>
   *         <b>true</b> - duplicate elements are allowed in list;<br>
   *         <b>false</b> - list will not contain duplicate elements.
   */
  protected final boolean isDuplicatesAllowed() {
    return allowDuplicates;
  }

  /**
   * Returns configured maximal number of elements in bundle.
   *
   * @return int - number of elements in bundle
   */
  protected final int getBundleCapacity() {
    return bundleCapacity;
  }

  /**
   * Returns last bundle in bundles linked list.
   *
   * @return {@link Bundle} - last bundle in bundles linked list
   */
  protected Bundle lastBundle() {
    Bundle b = firstBundle;
    while( b.next != null ) {
      b = b.next;
    }
    return b;
  }

  /**
   * Creates new {@link Bundle} with configured capacity.
   *
   * @return {@link Bundle} - new empty bundle
   */
  protected final Bundle createBundle() {
    return new Bundle( bundleCapacity );
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<Long> iterator() {
    return new Iterator<>() {

      private final int expectedChangeCount = changeCount;

      int index = 0;

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
    int index = 0;
    for( Bundle b = firstBundle; b != null; b = b.next ) {
      for( int i = 0; i < b.count; i++ ) {
        result[index++] = Long.valueOf( b.elems[i] );
      }
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

  // nop

  // ------------------------------------------------------------------------------------
  // ILongList
  //

  @Override
  public boolean hasValue( long aValue ) {
    return indexOfValue( aValue ) >= 0;
  }

  @Override
  public abstract int indexOfValue( long aValue );

  @Override
  public long getValue( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    int index = aIndex;
    do {
      if( index < b.count ) {
        return b.elems[index];
      }
      index -= b.count;
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  @Override
  public long[] toValuesArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_LONGS;
    }
    long[] result = new long[size];
    int index = 0;
    for( Bundle b = firstBundle; b != null; b = b.next ) {
      for( int i = 0; i < b.count; i++ ) {
        result[index++] = b.elems[i];
      }
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    firstBundle.next = null;
    firstBundle.count = 0;
    size = 0;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollectionEdit
  //

  // nop

  // ------------------------------------------------------------------------------------
  // IListBasicEdit
  //

  // nop

  // ------------------------------------------------------------------------------------
  // ILongListBasicEdit
  //

  @Override
  public abstract int add( long aValue );

  @Override
  public abstract int removeValue( long aValue );

  @Override
  public abstract long removeValueByIndex( int aIndex );

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof ILongList) ) {
      return false;
    }
    return TsCollectionsUtils.isLongListsEqual( this, (ILongList)obj );
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( int i = 0, n = size(); i < n; i++ ) {
      long value = getValue( i );
      result = TsLibUtils.PRIME * result + (int)(value ^ (value >>> 32));
    }
    return result;
  }

}
