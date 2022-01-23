package org.toxsoft.tslib.coll.helpers;

import java.util.Comparator;

import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.basis.ITsSortedCollectionTag;
import org.toxsoft.tslib.utils.errors.*;

/**
 * The list items reorderer tied to the editable list.
 *
 * @author hazard157
 * @param <E> - the type of elements the underlying list
 */
public interface IListReorderer<E> {

  /**
   * Returns the underlying list.
   *
   * @return {@link IList} - the list is being reordered
   */
  IList<E> list();

  /**
   * Exchanges list items.
   *
   * @param aIndex1 int - first item index
   * @param aIndex2 int - second item index
   * @return <code>true</code> if items was exhanged
   * @throws TsIllegalArgumentRtException any index is out of range
   */
  boolean swap( int aIndex1, int aIndex2 );

  /**
   * Moves the item with index aOldIndex to the new position at aNewIndex.
   *
   * @param aOldIndex int - the old index
   * @param aNewIndex int - the new index
   * @return <code>true</code> if item was moved
   * @throws TsIllegalArgumentRtException any index is out of range
   */
  boolean move( int aOldIndex, int aNewIndex );

  /**
   * Moves the item with index aOldIndex to the specified direction.
   *
   * @param aOldIndex int - the item index
   * @param aDir {@link ETsCollMove} - the move direction
   * @return <code>true</code> if item was moved
   * @throws TsIllegalArgumentRtException index is out of range
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean move( int aOldIndex, ETsCollMove aDir );

  /**
   * Exchanges list items.
   *
   * @param aElem1 &lt;E&gt; - the first item
   * @param aElem2 &lt;E&gt; - the second item
   * @return <code>true</code> if items was exhanged
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such item in list
   */
  boolean swap( E aElem1, E aElem2 );

  /**
   * Moves the item to the new position at aNewIndex..
   *
   * @param aElem &lt;E&gt; - the item to be moved
   * @param aNewIndex int - the new index
   * @return <code>true</code> if item was moved
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such item in list
   */
  boolean move( E aElem, int aNewIndex );

  /**
   * Moves the item the specified direction.
   *
   * @param aElem &lt;E&gt; - the item to be moved
   * @param aDir {@link ETsCollMove} - the move direction
   * @return <code>true</code> if item was moved
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such item in list
   */
  boolean move( E aElem, ETsCollMove aDir );

  /**
   * Sorts the list.
   * <p>
   * Does nothing if underlying list implements {@link ITsSortedCollectionTag}.
   *
   * @param aComparator {@link Comparator} - comparator for items comparison
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void sort( Comparator<E> aComparator );

  // ------------------------------------------------------------------------------------
  // Convinience inline methods
  //

  @SuppressWarnings( "javadoc" )
  default boolean moveFirst( int aIndex ) {
    return move( aIndex, ETsCollMove.FIRST );
  }

  @SuppressWarnings( "javadoc" )
  default boolean movePrev( int aIndex ) {
    return move( aIndex, ETsCollMove.PREV );
  }

  @SuppressWarnings( "javadoc" )
  default boolean moveNext( int aIndex ) {
    return move( aIndex, ETsCollMove.NEXT );
  }

  @SuppressWarnings( "javadoc" )
  default boolean moveLast( int aIndex ) {
    return move( aIndex, ETsCollMove.LAST );
  }

  @SuppressWarnings( "javadoc" )
  default boolean moveFirst( E aElem ) {
    return move( aElem, ETsCollMove.FIRST );
  }

  @SuppressWarnings( "javadoc" )
  default boolean movePrev( E aElem ) {
    return move( aElem, ETsCollMove.PREV );
  }

  @SuppressWarnings( "javadoc" )
  default boolean moveNext( E aElem ) {
    return move( aElem, ETsCollMove.NEXT );
  }

  @SuppressWarnings( "javadoc" )
  default boolean moveLast( E aElem ) {
    return move( aElem, ETsCollMove.LAST );
  }

}
