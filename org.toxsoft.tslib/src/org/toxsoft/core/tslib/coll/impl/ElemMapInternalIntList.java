package org.toxsoft.core.tslib.coll.impl;

import java.io.Serializable;

import org.toxsoft.core.tslib.coll.IMapEdit;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;

/**
 * Простой список целых индексов - содержимое крзин.
 * <p>
 * 2019-05-31 Список введен с целью оптимизации метода {@link IMapEdit#removeByKey(Object)}.
 *
 * @author hazard157
 */
class ElemMapInternalIntList
    implements Serializable {

  private static final long serialVersionUID = 157157L;

  int items[];
  int size;

  public ElemMapInternalIntList( int aInitialSize ) {
    TsIllegalArgumentRtException.checkTrue( aInitialSize <= 0 );
    items = new int[aInitialSize];
  }

  int size() {
    return size;
  }

  int get( int aIndex ) {
    return items[aIndex];
  }

  void set( int aIndex, int aValue ) {
    items[aIndex] = aValue;
  }

  void remove( int aIndex ) {
    System.arraycopy( items, aIndex + 1, items, aIndex, size - aIndex - 1 );
    --size;
  }

  void clear() {
    size = 0;
  }

  int add( int aValue ) {
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
   * Для всех элементов уменьшает значение на 1, если оно больше aThreshold.
   *
   * @param aThreshold int - порговое значение элементов к уменьшению
   */
  void decreaseAllAboveThreshold( int aThreshold ) {
    for( int i = 0; i < size; i++ ) {
      if( items[i] > aThreshold ) {
        --items[i];
      }
    }
  }

}
