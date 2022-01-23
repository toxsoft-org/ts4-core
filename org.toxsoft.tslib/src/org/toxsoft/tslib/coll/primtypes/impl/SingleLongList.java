package org.toxsoft.tslib.coll.primtypes.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.toxsoft.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.coll.primtypes.ILongList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Immutable implementation of {@link ILongList} containing one element.
 *
 * @author hazard157
 */
public class SingleLongList
    implements ILongList, ITsFastIndexListTag<Long>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final long[] items = new long[1];

  /**
   * Constructor from element value.
   *
   * @param aElem long - value of the only element
   */
  public SingleLongList( long aElem ) {
    items[0] = aElem;
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<Long> iterator() {
    return new Iterator<>() {

      int index = 0;

      @Override
      public boolean hasNext() {
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
    return 1;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public Long[] toArray( Long[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( aSrcArray.length > 0 ) {
      aSrcArray[0] = Long.valueOf( items[0] );
      return aSrcArray;
    }
    Long[] result = new Long[1];
    result[0] = Long.valueOf( items[0] );
    return result;
  }

  @Override
  public Object[] toArray() {
    Object[] result = new Object[1];
    result[0] = Long.valueOf( items[0] );
    return result;
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
    return aValue == items[0];
  }

  @Override
  public int indexOfValue( long aValue ) {
    if( aValue == items[0] ) {
      return 0;
    }
    return -1;
  }

  @Override
  public long getValue( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex != 0 );
    return items[0];
  }

  @Override
  public long[] toValuesArray() {
    long[] result = new long[1];
    result[0] = items[0];
    return result;
  }

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
    long value = items[0];
    result = TsLibUtils.PRIME * result + (int)(value ^ (value >>> 32));
    return result;
  }

}
