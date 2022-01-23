package org.toxsoft.tslib.coll.primtypes;

import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Editable list of integers.
 * <p>
 * Extends {@link ILongListBasicEdit} with editing methods applicable only to unsorted collections.
 *
 * @author hazard157
 */
public interface ILongListEdit
    extends ILongListBasicEdit, IListEdit<Long> {

  /**
   * Replaces the element at the specified position in this list with the specified element.
   *
   * @param aIndex int - index index of the element to replace (in range 0..{@link #size()}-1)
   * @param aValue long - element to be stored at the specified position
   * @return long - the element previously at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   */
  long set( int aIndex, long aValue );

  /**
   * Inserts the element at the specified position in this list.
   *
   * @param aIndex int - index of the element to replace (in range 0..{@link #size()}-1)
   * @param aValue long - element to be stored at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   */
  void insert( int aIndex, long aValue );

  // ------------------------------------------------------------------------------------
  // Convinience methods with default implementations
  //

  /**
   * Inserts all the element from specified collection at the specified position in this list.
   *
   * @param aIndex int - index of the first element (in range 0..{@link #size()}-1)
   * @param aLongList ILongList - specified collection
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void insertAll( int aIndex, ILongList aLongList ) {
    TsNullArgumentRtException.checkNull( aLongList );
    for( int i = 0, n = aLongList.size(); i < n; i++ ) {
      insert( aIndex, aLongList.getValue( aIndex ) );
    }
  }

  /**
   * Inserts all the element from specified array at the specified position in this list.
   *
   * @param aIndex int - index of the first element (in range 0..{@link #size()}-1)
   * @param aArray long[] - specified array
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void insertAll( int aIndex, long... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      insert( aIndex, aArray[i] );
    }
  }

  // ------------------------------------------------------------------------------------
  // Reimplement IListEdit<Long> methods
  //

  @Override
  default Long set( int aIndex, Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    long val = aElem.longValue();
    return Long.valueOf( set( aIndex, val ) );
  }

  @Override
  default void insert( int aIndex, Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    insert( aIndex, aElem.longValue() );
  }

}
