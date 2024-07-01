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
 * Base of balanced {@link IIntList} implementation as linked list of arrays (bundles).
 *
 * @author hazard157
 */
abstract class AbstractIntBundledList
    implements IIntListBasicEdit, ITsFastIndexListTag<Integer>, Serializable {

  // FIXME serialize as array

  private static final long serialVersionUID = 158158L;

  /**
   * Bundled array of ints, chained as linked list.
   *
   * @author hazard157
   */
  protected static final class Bundle {

    final int elems[];
    int       count = 0;
    Bundle    next  = null;

    Bundle( int aCapacity ) {
      elems = new int[aCapacity];
    }

    int lastValue() {
      return elems[count - 1];
    }

    boolean isEmpty() {
      return count == 0;
    }

    boolean isFull() {
      return count == elems.length;
    }

    int remove( int aIndex ) {
      int result = elems[aIndex];
      for( int i = aIndex + 1; i < count; i++ ) {
        elems[i - 1] = elems[i];
      }
      --count;
      return result;
    }

    void insert( int aIndex, int aValue ) {
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
  protected AbstractIntBundledList( int aBundleCapacity, boolean aAllowDuplicates ) {
    init( aBundleCapacity, aAllowDuplicates );
  }

  protected void init( int aBundleCapacity, boolean aAllowDuplicates ) {
    bundleCapacity = BUNDLE_CAPACITY_RANGE.inRange( aBundleCapacity );
    bundleCapacity = aBundleCapacity;
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
  public Iterator<Integer> iterator() {
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
    int index = 0;
    for( Bundle b = firstBundle; b != null; b = b.next ) {
      for( int i = 0; i < b.count; i++ ) {
        result[index++] = Integer.valueOf( b.elems[i] );
      }
    }
    return result;
  }

  @Override
  public Object[] toArray() {
    return toArray( TsLibUtils.EMPTY_ARRAY_OF_INT_OBJS );
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  // nop

  // ------------------------------------------------------------------------------------
  // IIntList
  //

  @Override
  public boolean hasValue( int aValue ) {
    return indexOfValue( aValue ) >= 0;
  }

  @Override
  public abstract int indexOfValue( int aValue );

  @Override
  public int getValue( int aIndex ) {
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
  public int[] toValuesArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_INTS;
    }
    int[] result = new int[size];
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
  // IIntListBasicEdit
  //

  @Override
  public abstract int add( int aValue );

  @Override
  public abstract int removeValue( int aValue );

  @Override
  public abstract int removeValueByIndex( int aIndex );

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
    if( !(obj instanceof IIntList) ) {
      return false;
    }
    return TsCollectionsUtils.isIntListsEqual( this, (IIntList)obj );
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( int i = 0, n = size(); i < n; i++ ) {
      int value = getValue( i );
      result = TsLibUtils.PRIME * result + (value ^ (value >>> 32));
    }
    return result;
  }

}
