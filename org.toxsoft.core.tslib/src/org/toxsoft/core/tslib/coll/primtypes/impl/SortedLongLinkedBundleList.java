package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.coll.basis.ITsSortedCollectionTag;
import org.toxsoft.core.tslib.coll.primtypes.ILongList;
import org.toxsoft.core.tslib.coll.primtypes.ILongListBasicEdit;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Balanced implementation of sorted list {@link ILongListBasicEdit} as linked list of arrays (bundles).
 *
 * @author hazard157
 */
public final class SortedLongLinkedBundleList
    extends AbstractLongBundledList
    implements ITsSortedCollectionTag {

  // FIXME serialize as array

  private static final long serialVersionUID = 158158L;

  // --------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates list with all invariant properties.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  public SortedLongLinkedBundleList( int aBundleCapacity, boolean aAllowDuplicates ) {
    super( aBundleCapacity, aAllowDuplicates );
  }

  /**
   * Creates empty list with defaults: duplicates allowed and bundle size of {@link #DEFAULT_BUNDLE_CAPACITY}.
   */
  public SortedLongLinkedBundleList() {
    this( DEFAULT_BUNDLE_CAPACITY, true );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems long[] - array of list elements
   * @throws TsNullArgumentRtException aElem = <code>null</code>
   */
  public SortedLongLinkedBundleList( long... aElems ) {
    this();
    for( int i = 0; i < aElems.length; i++ ) {
      add( aElems[i] );
    }
  }

  /**
   * Copy constructor.
   *
   * @param aSrc {@link ILongList} - source list
   * @throws TsNullArgumentRtException aSrc = null
   */
  public SortedLongLinkedBundleList( ILongList aSrc ) {
    this();
    for( int i = 0, n = aSrc.size(); i < n; i++ ) {
      add( aSrc.getValue( i ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Serialization
  // List is written in following form:
  // - int bundleCapacity
  // - boolean allowDuplicates
  // - int size
  // - long[] elements
  //

  private void writeObject( ObjectOutputStream aOut )
      throws IOException {
    aOut.defaultWriteObject();
    aOut.writeInt( getBundleCapacity() );
    aOut.writeBoolean( isDuplicatesAllowed() );
    aOut.writeInt( size );
    for( Bundle b = firstBundle; b != null; b = b.next ) {
      for( int i = 0; i < b.count; i++ ) {
        aOut.writeLong( b.elems[i] );
      }
    }
  }

  private void readObject( ObjectInputStream aIn )
      throws IOException {
    int rBundleCapacity = aIn.readInt();
    boolean rallowDups = aIn.readBoolean();
    init( rBundleCapacity, rallowDups );
    int rSize = aIn.readInt();
    for( int i = 0; i < rSize; i++ ) {
      long val = aIn.readLong();
      add( val );
    }
  }

  // ------------------------------------------------------------------------------------
  // ILongListBasicEdit
  //

  @Override
  public int add( long aValue ) {
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      size = 1;
      b.count = 1;
      b.elems[0] = aValue;
      ++changeCount;
      return 0;
    }
    Bundle bPrev = null;
    int index = 0;
    do {
      if( aValue <= b.lastValue() ) {
        int i;
        for( i = b.count - 1; i >= 0; i-- ) {
          if( aValue >= b.elems[i] ) {
            if( aValue == b.elems[i] ) {
              if( !isDuplicatesAllowed() ) {
                return i + index;
              }
            }
            break;
          }
        }
        ++size;
        // insert aValue into i+1
        if( b.isFull() ) {
          Bundle bNext = b.next;
          if( bNext == null || bNext.isFull() ) { // нужно добавить связку
            bNext = createBundle();
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
    if( bPrev.isFull() ) {
      Bundle bNew = createBundle();
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
  public int removeValue( long aValue ) {
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      return -1;
    }
    Bundle bPrev = null;
    int indexOfBundle0 = 0;
    do {
      if( aValue <= b.lastValue() ) {
        for( int i = 0; i < b.count; i++ ) {
          if( b.elems[i] == aValue ) {
            --b.count;
            --size;
            if( b.count == 0 ) { // bundle became empty
              if( bPrev == null ) {
                if( b.next != null ) {
                  firstBundle = b.next;
                }
              }
              else {
                bPrev.next = b.next;
              }
            }
            else { // shifing down upper part of the array
              for( int j = i; j < b.count; j++ ) {
                b.elems[j] = b.elems[j + 1];
              }
            }
            ++changeCount;
            return i + indexOfBundle0;
          }
          if( aValue < b.elems[i] ) {
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

  @Override
  public long removeValueByIndex( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    Bundle bPrev = null;
    int index = aIndex;
    do {
      if( index < b.count ) {
        long result = b.elems[index];
        --b.count;
        --size;
        if( b.count == 0 ) { // bundle is empty
          if( bPrev == null ) {
            if( b.next != null ) {
              firstBundle = b.next;
            }
          }
          else {
            bPrev.next = b.next;
          }
        }
        else { // shift down upper part of array
          for( int j = index; j < b.count; j++ ) {
            b.elems[j] = b.elems[j + 1];
          }
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

}
