package org.toxsoft.core.tslib.bricks.strid.coll;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.coll.helpers.ETsCollMove;
import org.toxsoft.core.tslib.coll.helpers.IListReorderer;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * The stridables list items reorderer tied to the editable list.
 *
 * @author hazard157
 * @param <E> - the type of elements the underlying list
 */
public interface IStridablesListReorderer<E extends IStridable>
    extends IListReorderer<E> {

  @Override
  IStridablesList<E> list();

  /**
   * Exchanges list items.
   *
   * @param aId1 String - first item ID
   * @param aId2 String - second item ID
   * @return <code>true</code> if items was exhanged
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException any ID not found in list
   */
  boolean swap( String aId1, String aId2 );

  /**
   * Moves the item with ID aOldId to the new position at aNewId.
   *
   * @param aOldId String - the old ID
   * @param aNewId String - the new ID
   * @return <code>true</code> if item was moved
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException any ID not found in list
   */
  boolean move( String aOldId, String aNewId );

  /**
   * Moves the item with ID aOldId to the specified direction.
   *
   * @param aOldId String - the item ID
   * @param aDir {@link ETsCollMove} - the move direction
   * @return <code>true</code> if item was moved
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException ID not found in list
   */
  boolean move( String aOldId, ETsCollMove aDir );

  // ------------------------------------------------------------------------------------
  // Convinience inline methods
  //

  @SuppressWarnings( "javadoc" )
  default boolean moveFirst( String aId ) {
    return move( aId, ETsCollMove.FIRST );
  }

  @SuppressWarnings( "javadoc" )
  default boolean movePrev( String aId ) {
    return move( aId, ETsCollMove.PREV );
  }

  @SuppressWarnings( "javadoc" )
  default boolean moveNext( String aId ) {
    return move( aId, ETsCollMove.NEXT );
  }

  @SuppressWarnings( "javadoc" )
  default boolean moveLast( String aId ) {
    return move( aId, ETsCollMove.LAST );
  }

}
