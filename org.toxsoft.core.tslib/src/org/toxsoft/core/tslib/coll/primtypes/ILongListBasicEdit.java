package org.toxsoft.core.tslib.coll.primtypes;

import org.toxsoft.core.tslib.coll.IListBasicEdit;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Editable extension of {@link ILongList}.
 * <p>
 * This interface adds basic editing methods to {@link ILongList}. Editing methods is applicable both the sorted
 * collections and unsorted lists.
 *
 * @author hazard157
 */
public interface ILongListBasicEdit
    extends ILongList, IListBasicEdit<Long> {

  /**
   * Adds element to collection.
   *
   * @param aValue long - element to be added
   * @return int - index of added (or already existed) element
   */
  int add( long aValue );

  /**
   * Removes the first occurrence of the specified element from this list, if it is present.
   *
   * @param aValue long - element to be removed from this list, if present
   * @return int - index of just removed element or -1 if element was not found in this list
   */
  int removeValue( long aValue );

  /**
   * Removes the element at the specified position in this list.
   *
   * @param aIndex int - the index of the element to be removed, in range 0..{@link #size()}-1
   * @return long - the element previously at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   */
  long removeValueByIndex( int aIndex );

  // ------------------------------------------------------------------------------------
  // Convinience methods with default implementations
  //

  /**
   * Adds all of the elements in the specified array to this collection.
   * <p>
   * Result of thie method is same as if all items from the specified array were added to this collection using
   * {@link #add(long)}.
   *
   * @param aArray long[] - array containing elements to be added to this collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void addAll( long... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      add( aArray[i] );
    }
  }

  /**
   * Adds all of the elements in the specified collection to this collection.
   * <p>
   * Result of thie method is same as if all items from the specified array were added to this collection using
   * {@link #add(long)}.
   *
   * @param aLongList ILongList - collection containing elements to be added to this collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void addAll( ILongList aLongList ) {
    TsNullArgumentRtException.checkNull( aLongList );
    for( int i = 0, n = aLongList.size(); i < n; i++ ) {
      add( aLongList.getValue( i ) );
    }
  }

  /**
   * Replaces all items of this collection with all items from the specified array.
   *
   * @param aArray long[] - specified array
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( long... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    clear();
    addAll( aArray );
  }

  /**
   * Replaces all items of this collection with all items from the specified collection.
   *
   * @param aLongList ILongList - specified collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( ILongList aLongList ) {
    TsNullArgumentRtException.checkNull( aLongList );
    clear();
    addAll( aLongList );
  }

  // ------------------------------------------------------------------------------------
  // Reimplement IListBasicEdit<Long> methods
  //

  @Override
  default int add( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return add( aElem.longValue() );
  }

  @Override
  default int remove( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return removeValue( aElem.longValue() );
  }

  @Override
  default Long removeByIndex( int aIndex ) {
    return Long.valueOf( removeValueByIndex( aIndex ) );
  }

}
