package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListReorderer;
import org.toxsoft.core.tslib.coll.helpers.ETsCollMove;
import org.toxsoft.core.tslib.coll.helpers.ListReorderer;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IStridablesListReorderer} implementation.
 *
 * @author hazard157
 * @param <E> - the type of elements the underlying list
 */
public class StridablesListReorderer<E extends IStridable>
    extends ListReorderer<E, IStridablesListEdit<E>>
    implements IStridablesListReorderer<E> {

  /**
   * Creates the reorderer, assosiated to the list.
   *
   * @param aSource &lt;{@link IStridablesListEdit}&gt; - the underlying list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StridablesListReorderer( IStridablesListEdit<E> aSource ) {
    super( aSource );
  }

  @Override
  public boolean swap( String aId1, String aId2 ) {
    int index1 = list().keys().indexOf( aId1 );
    int index2 = list().keys().indexOf( aId2 );
    return swap( index1, index2 );
  }

  @Override
  public boolean move( String aOldId, String aNewId ) {
    int oldIndex = list().keys().indexOf( aOldId );
    int newIndex = list().keys().indexOf( aNewId );
    return move( oldIndex, newIndex );
  }

  @Override
  public boolean move( String aOldId, ETsCollMove aDir ) {
    int oldIndex = list().keys().indexOf( aOldId );
    return move( oldIndex, aDir );
  }

}
