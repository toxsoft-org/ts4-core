package org.toxsoft.core.tslib.coll.basis;

import java.util.Collection;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mixin interface of collections allowing to add and remove elements.
 * <p>
 * All editable implementations must guarantee atomic behaviour of batch editing methods like
 * {@link #addAll(Collection)}. In other words, if any of adding elements can not be added to collection for any reason,
 * exception must be thrown and collection must remain intact.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface ITsCollectionEdit<E>
    extends ITsCollection<E>, ITsClearable {

  /**
   * Adds element to this collection.
   * <p>
   * Does not adds element if collection does not permit duplicates and already contains the specified element.
   *
   * @param aElem E - element to be added
   * @return int - index of added (or already existed) element
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  int add( E aElem );

  /**
   * Removes the first occurrence of the specified element from this list, if it is present.
   *
   * @param aElem &lt;E&gt; - element to be removed from this list, if present
   * @return int - index of just removed element or -1 if element was not found in this list
   * @throws TsNullArgumentRtException aElem = null
   */
  int remove( E aElem );

  /**
   * Removes the element at the specified position in this list.
   *
   * @param aIndex int - the index of the element to be removed, in range 0..{@link #size()}-1
   * @return &lt;E&gt; - the element previously at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   */
  E removeByIndex( int aIndex );

  // ------------------------------------------------------------------------------------
  // Additional methods with default implementations
  //

  /**
   * Removes the specified number of elements from the specified position in this list.
   *
   * @param aIndex int - the index of the first element to be removed, in range 0..{@link #size()}-1
   * @param aCount int - number of elements to remove in range 0..(size-aIndex)
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsIllegalArgumentRtException aCount < 0
   * @throws TsIllegalArgumentRtException aIndex+aCount is out of range
   */
  default void removeRangeByIndex( int aIndex, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aCount < 0 );
    TsIllegalArgumentRtException.checkFalse( aIndex >= size() );
    if( aCount == 0 ) {
      return;
    }
    TsIllegalArgumentRtException.checkFalse( aIndex + aCount >= size() );
    for( int i = 0; i < aCount; i++ ) {
      removeByIndex( aIndex );
    }
  }

  /**
   * Adds all of the elements in the specified array to this collection.
   * <p>
   * Result of thie method is same as if all items from the specified array were added to this collection using
   * {@link #add(Object)}.
   *
   * @param aArray E[] - array containing elements to be added to this collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsNullArgumentRtException any element of specified array is <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  default void addAll( E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      add( aArray[i] );
    }
  }

  /**
   * Adds all of the elements in the specified collection to this collection.
   * <p>
   * Result of thie method is same as if all items from the specified collection were added to this collection using
   * {@link #add(Object)}.
   *
   * @param aColl {@link ITsCollection}&lt;E&gt; - collection containing elements to be added to this collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void addAll( ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( aColl instanceof ITsFastIndexListTag ) {
      @SuppressWarnings( { "rawtypes", "unchecked" } )
      ITsFastIndexListTag<E> ll = (ITsFastIndexListTag)aColl;
      for( int i = 0, count = ll.size(); i < count; i++ ) {
        add( ll.get( i ) );
      }
    }
    else {
      for( E e : aColl ) {
        add( e );
      }
    }
  }

  /**
   * Adds all of the elements in the specified collection to this collection.
   * <p>
   * Result of thie method is same as if all items from the specified collection were added to this collection using
   * {@link #add(Object)}.
   *
   * @param aColl {@link Collection}&lt;E&gt; - collection containing elements to be added to this collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsNullArgumentRtException any element of specified collection is <code>null</code>
   */
  default void addAll( Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( E e : aColl ) { // for atomic error recovery check for nulls before really make changes in collection
      TsNullArgumentRtException.checkNull( e );
    }
    for( E e : aColl ) {
      add( e );
    }
  }

  /**
   * Replaces all items of this collection with all items from the specified collection.
   *
   * @param aColl {@link ITsCollection}&lt;E&gt; - specified collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    clear();
    addAll( aColl );
  }

  /**
   * Replaces all items of this collection with all items from the specified collection.
   *
   * @param aColl {@link Collection}&lt;E&gt; - specified collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( E e : aColl ) { // for atomic error recovery check for nulls before really make changes in collection
      TsNullArgumentRtException.checkNull( e );
    }
    clear();
    for( E e : aColl ) {
      add( e );
    }
  }

  /**
   * Replaces all items of this collection with all items from the specified array.
   *
   * @param aArray &lt;E&gt;[] - specified array
   * @throws TsNullArgumentRtException argument or any it's element = <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  default void setAll( E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    clear();
    for( int i = 0; i < aArray.length; i++ ) {
      add( aArray[i] );
    }
  }

}
