package org.toxsoft.core.tslib.coll.impl;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple integers list to be used used as indexes in map's buckets.
 * <p>
 * 2019-05-31 The list was introduced to optimize the {@link IMapEdit#removeByKey(Object)} method.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class ElemMapInternalIntList
    implements Serializable {

  private static final long serialVersionUID = 157157L;

  int items[];
  int size;

  public ElemMapInternalIntList( int aInitialSize ) {
    TsIllegalArgumentRtException.checkTrue( aInitialSize <= 0 );
    items = new int[aInitialSize];
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public int size() {
    return size;
  }

  public int get( int aIndex ) {
    return items[aIndex];
  }

  public void set( int aIndex, int aValue ) {
    items[aIndex] = aValue;
  }

  public void remove( int aIndex ) {
    System.arraycopy( items, aIndex + 1, items, aIndex, size - aIndex - 1 );
    --size;
  }

  public void clear() {
    size = 0;
  }

  public int add( int aValue ) {
    // reallocate bigger array
    if( size == items.length ) {
      int[] newItems = new int[2 * items.length];
      System.arraycopy( items, 0, newItems, 0, items.length );
      items = newItems;
    }
    items[size] = aValue;
    return size++;
  }

  /**
   * For all elements, decreases the value by 1 if value is greater than aThreshold.
   *
   * @param aThreshold int - threshold value of elements to decrease
   */
  public void decreaseAllAboveThreshold( int aThreshold ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] > aThreshold ) {
        --items[i];
      }
    }
  }

  /**
   * For all elements, increases the value by 1 if value is greater than aThreshold.
   *
   * @param aThreshold int - threshold value of elements to increase
   */
  public void increaseAllAboveThreshold( int aThreshold ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] >= aThreshold ) {
        ++items[i];
      }
    }
  }

}
