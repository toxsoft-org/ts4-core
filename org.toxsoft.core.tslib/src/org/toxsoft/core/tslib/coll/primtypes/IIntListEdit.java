package org.toxsoft.core.tslib.coll.primtypes;

import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Editable list of integers.
 * <p>
 * Extends {@link IIntListBasicEdit} with editing methods applicable only to unsorted collections.
 *
 * @author hazard157
 */
public interface IIntListEdit
    extends IIntListBasicEdit, IListEdit<Integer> {

  /**
   * Replaces the element at the specified position in this list with the specified element.
   *
   * @param aIndex int - index index of the element to replace (in range 0..{@link #size()}-1)
   * @param aValue int - element to be stored at the specified position
   * @return int - the element previously at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   */
  int set( int aIndex, int aValue );

  /**
   * Inserts the element at the specified position in this list.
   *
   * @param aIndex int - index of the element to replace (in range 0..{@link #size()}-1)
   * @param aValue int - element to be stored at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   */
  void insert( int aIndex, int aValue );

  // ------------------------------------------------------------------------------------
  // Convinience methods with default implementations
  //

  /**
   * Inserts all the element from specified collection at the specified position in this list.
   *
   * @param aIndex int - index of the first element (in range 0..{@link #size()}-1)
   * @param aIntList IIntList - specified collection
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void insertAll( int aIndex, IIntList aIntList ) {
    TsNullArgumentRtException.checkNull( aIntList );
    for( int i = 0, n = aIntList.size(); i < n; i++ ) {
      insert( aIndex, aIntList.getValue( aIndex ) );
    }
  }

  /**
   * Inserts all the element from specified array at the specified position in this list.
   *
   * @param aIndex int - index of the first element (in range 0..{@link #size()}-1)
   * @param aArray int[] - specified array
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void insertAll( int aIndex, int... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      insert( aIndex, aArray[i] );
    }
  }

  // ------------------------------------------------------------------------------------
  // Reimplement IListEdit<Integer> methods
  //

  @Override
  default Integer set( int aIndex, Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    int val = aElem.intValue();
    return Integer.valueOf( set( aIndex, val ) );
  }

  @Override
  default void insert( int aIndex, Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    insert( aIndex, aElem.intValue() );
  }

}
