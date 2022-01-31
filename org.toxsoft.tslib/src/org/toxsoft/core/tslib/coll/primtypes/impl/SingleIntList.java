package org.toxsoft.core.tslib.coll.primtypes.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.toxsoft.core.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Immutable implementation of {@link IIntList} containing one element.
 *
 * @author hazard157
 */
public class SingleIntList
    implements IIntList, ITsFastIndexListTag<Integer>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final int[] items = new int[1];

  /**
   * Constructor from element value.
   *
   * @param aElem int - value of the only element
   */
  public SingleIntList( int aElem ) {
    items[0] = aElem;
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<Integer> iterator() {
    return new Iterator<>() {

      int index = 0;

      @Override
      public boolean hasNext() {
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
    return 1;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public Integer[] toArray( Integer[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( aSrcArray.length > 0 ) {
      aSrcArray[0] = Integer.valueOf( items[0] );
      return aSrcArray;
    }
    Integer[] result = new Integer[1];
    result[0] = Integer.valueOf( items[0] );
    return result;
  }

  @Override
  public Object[] toArray() {
    Object[] result = new Object[1];
    result[0] = Integer.valueOf( items[0] );
    return result;
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
    return aValue == items[0];
  }

  @Override
  public int indexOfValue( int aValue ) {
    if( aValue == items[0] ) {
      return 0;
    }
    return -1;
  }

  @Override
  public int getValue( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex != 0 );
    return items[0];
  }

  @Override
  public int[] toValuesArray() {
    int[] result = new int[1];
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
