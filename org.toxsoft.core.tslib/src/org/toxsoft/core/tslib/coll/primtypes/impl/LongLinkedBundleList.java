package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.coll.basis.ITsSortedCollectionTag;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Balanced implementation of sorted list {@link ILongListBasicEdit} as linked list of arrays (bundles).
 *
 * @author hazard157
 */
public final class LongLinkedBundleList
    extends AbstractLongBundledList
    implements ILongListEdit, ITsSortedCollectionTag {

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
  public LongLinkedBundleList( int aBundleCapacity, boolean aAllowDuplicates ) {
    super( aBundleCapacity, aAllowDuplicates );
  }

  /**
   * Creates empty list with defaults: duplicates allowed and bundle size of {@link #DEFAULT_BUNDLE_CAPACITY}.
   */
  public LongLinkedBundleList() {
    this( DEFAULT_BUNDLE_CAPACITY, true );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems long[] - array of list elements
   * @throws TsNullArgumentRtException aElem = <code>null</code>
   */
  public LongLinkedBundleList( long... aElems ) {
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
  public LongLinkedBundleList( ILongList aSrc ) {
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
    Bundle b = lastBundle();
    if( b.isFull() ) {
      b.next = createBundle();
      b = b.next;
    }
    b.elems[b.count++] = aValue;
    ++changeCount;
    return size++;
  }

  @Override
  public int removeValue( long aValue ) {
    int index = 0;
    Bundle prevB = firstBundle;
    // iterate over all bundles
    for( Bundle b = firstBundle; b != null; b = b.next ) {
      // now iterate over elements in bundle
      for( int i = 0; i < b.count; i++ ) {
        if( b.elems[i] == aValue ) { // found value to be removed
          b.remove( index );
          if( b.isEmpty() && b.next != null ) { // bundle is empty - remove if it is not the last (or the only) bundle
            if( b == firstBundle ) {
              firstBundle = b.next;
            }
            else {
              prevB.next = b.next;
            }
          }
          --size;
          ++changeCount;
          return index;
        }
        ++index;
      }
      prevB = b;
    }
    return -1;
  }

  @Override
  public long removeValueByIndex( int aIndex ) {
    TsNullArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    Bundle prevB = b;
    int index = aIndex;
    while( true ) {
      if( index < b.count ) { // found bundle where is element to be removed
        long result = b.remove( index );
        if( b.isEmpty() && b.next != null ) { // bundle is empty - remove if it is not the last (or the only) bundle
          if( b == firstBundle ) {
            firstBundle = b.next;
          }
          else {
            prevB.next = b.next;
          }
        }
        --size;
        ++changeCount;
        return result;
      }
      index -= b.count;
      prevB = b;
      b = b.next;
    }
  }

  @Override
  public long set( int aIndex, long aValue ) {
    TsNullArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    int index = aIndex;
    while( true ) {
      if( index < b.count ) {
        long prevValue = b.elems[index];
        b.elems[index] = aValue;
        ++changeCount;
        return prevValue;
      }
      index -= b.count;
      b = b.next;
    }
  }

  @Override
  public void insert( int aIndex, long aValue ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size );
    Bundle b = firstBundle;
    int index = aIndex;
    while( true ) {
      if( index <= b.count ) { // found bundle where element must be inserted
        ++size;
        ++changeCount;
        if( b.isFull() ) { // bundle is full, need to move one element to the next bundle
          Bundle nextBundle = b.next;
          if( nextBundle == null ) { // last bundle - add new bundle
            nextBundle = createBundle();
            b.next = nextBundle;
          }
          else {
            if( nextBundle.isFull() ) { // next bundle is also full - insert new bundle between
              nextBundle = createBundle();
              nextBundle.next = b.next;
              b.next = nextBundle;
            }
          }
          if( index == b.count ) {
            nextBundle.insert( 0, aValue );
            return;
          }
          nextBundle.insert( 0, b.elems[b.elems.length - 1] );
        } // bundle was full - one element was moved to the next bundle
        b.insert( index, aValue );
        return;
      }
      index -= b.count;
      b = b.next;
    }
  }

}
