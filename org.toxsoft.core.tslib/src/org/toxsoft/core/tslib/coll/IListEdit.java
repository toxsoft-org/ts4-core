package org.toxsoft.core.tslib.coll;

import java.util.*;

import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Editable list of elements.
 * <p>
 * Extends {@link IListBasicEdit} with editing methods applicable only to unsorted collections.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface IListEdit<E>
    extends IListBasicEdit<E> {

  /**
   * Replaces the element at the specified position in this list with the specified element.
   *
   * @param aIndex int - index index of the element to replace (in range 0..{@link #size()}-1)
   * @param aElem &lt;E&gt; - element to be stored at the specified position
   * @return &lt;E&gt; - the element previously at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException aElem = null
   * @throws TsItemAlreadyExistsRtException the same element is already in list (when duplicates are disabled)
   */
  E set( int aIndex, E aElem );

  /**
   * Inserts the element at the specified position in this list.
   *
   * @param aIndex int - index of the element to replace (in range 0..{@link #size()}-1)
   * @param aElem &lt;E&gt; - element to be stored at the specified position
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException aElem = null
   * @throws TsItemAlreadyExistsRtException the same element is already in list (when duplicates are disabled)
   */
  void insert( int aIndex, E aElem );

  // ------------------------------------------------------------------------------------
  // Convinience methods with default implementations
  //

  /**
   * Inserts all the element from specified array at the specified position in this list.
   *
   * @param aIndex int - index of the first element (in range 0..{@link #size()}-1)
   * @param aArray &lt;E&gt;[] - specified array
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException aArray = <code>null</code>
   * @throws TsNullArgumentRtException any element of array is <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  default void insertAll( int aIndex, E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    for( int i = aArray.length - 1; i >= 0; i-- ) {
      insert( aIndex, aArray[i] );
    }
  }

  /**
   * Inserts all the element from specified collection at the specified position in this list.
   *
   * @param aIndex int - index of the first element (in range 0..{@link #size()}-1)
   * @param aColl {@link ITsCollection}&lt;E&gt; - specified collection
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException aColl = <code>null</code>
   */
  default void insertAll( int aIndex, ITsCollection<E> aColl ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size() );
    TsNullArgumentRtException.checkNull( aColl );
    if( aColl.isEmpty() ) {
      return;
    }
    if( aColl instanceof ITsFastIndexListTag ) {
      @SuppressWarnings( { "unchecked", "rawtypes" } )
      ITsFastIndexListTag<E> coll = (ITsFastIndexListTag)aColl;
      for( int i = coll.size() - 1; i >= 0; i-- ) {
        insert( aIndex, coll.get( i ) );
      }
    }
    else {
      IListEdit<E> tmpList = new ElemArrayList<>( aColl.size() );
      for( E e : aColl ) {
        tmpList.insert( 0, e );
      }
      insertAll( aIndex, tmpList );
    }
  }

  /**
   * Inserts all the element from specified collection at the specified position in this list.
   *
   * @param aIndex int - index of the first element (in range 0..{@link #size()}-1)
   * @param aColl Collection&lt;E&gt; - specified collection
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException aColl = <code>null</code>
   * @throws TsNullArgumentRtException any element of array is <code>null</code>
   */
  default void insertAll( int aIndex, Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( E s : aColl ) {
      TsNullArgumentRtException.checkNull( s );
    }
    IListEdit<E> list = new ElemArrayList<>( aColl );
    insertAll( aIndex, list );
  }

}
