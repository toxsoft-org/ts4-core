package org.toxsoft.core.tslib.coll.primtypes;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Editable extension of {@link IIntList}.
 * <p>
 * This interface adds basic editing methods to {@link IIntList}. Editing methods is applicable both the sorted
 * collections and unsorted lists.
 *
 * @author hazard157
 */
public interface IIntListBasicEdit
    extends IIntList, IListBasicEdit<Integer> {

  /**
   * Adds element to collection.
   *
   * @param aValue int - element to be added
   * @return int - index of added (or already existed) element
   */
  int add( int aValue );

  /**
   * Removes the first occurrence of the specified element from this list, if it is present.
   *
   * @param aValue int - element to be removed from this list, if present
   * @return int - index of just removed element or -1 if element was not found in this list
   */
  int removeValue( int aValue );

  /**
   * Removes the element at the specified position in this list.
   *
   * @param aIndex int - the index of the element to be removed, in range 0..{@link #size()}-1
   * @return int - the element previously at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   */
  int removeValueByIndex( int aIndex );

  // ------------------------------------------------------------------------------------
  // Convenience methods with default implementations
  //

  /**
   * Adds all of the elements in the specified array to this collection.
   * <p>
   * Result of this method is same as if all items from the specified array were added to this collection using
   * {@link #add(int)}.
   *
   * @param aArray int[] - array containing elements to be added to this collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void addAll( int... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      add( aArray[i] );
    }
  }

  /**
   * Adds all of the elements in the specified collection to this collection.
   * <p>
   * Result of thie method is same as if all items from the specified array were added to this collection using
   * {@link #add(int)}.
   *
   * @param aIntList IIntList - collection containing elements to be added to this collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void addAll( IIntList aIntList ) {
    TsNullArgumentRtException.checkNull( aIntList );
    for( int i = 0, n = aIntList.size(); i < n; i++ ) {
      add( aIntList.getValue( i ) );
    }
  }

  /**
   * Replaces all items of this collection with all items from the specified array.
   *
   * @param aArray int[] - specified array
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( int... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    clear();
    addAll( aArray );
  }

  /**
   * Replaces all items of this collection with all items from the specified collection.
   *
   * @param aIntList IIntList - specified collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( IIntList aIntList ) {
    TsNullArgumentRtException.checkNull( aIntList );
    clear();
    addAll( aIntList );
  }

  // ------------------------------------------------------------------------------------
  // Reimplement IListBasicEdit<Integer> methods
  //

  @Override
  default int add( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return add( aElem.intValue() );
  }

  @Override
  default int remove( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return removeValue( aElem.intValue() );
  }

  @Override
  default Integer removeByIndex( int aIndex ) {
    return Integer.valueOf( removeValueByIndex( aIndex ) );
  }

}
