package org.toxsoft.tslib.coll.primtypes.impl;

import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.coll.primtypes.IIntMap;

/**
 * {@link IIntMap} implementation with <code>int</code> keys sorted in ascending order.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class SortedIntMap<E>
    extends IntMapBase<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public SortedIntMap() {
    super( new SortedIntLinkedBundleList(), new ElemLinkedBundleList<>() );
  }

  /**
   * Constructs the map with internal list's specified capacity.
   *
   * @param aBundleCapacity int - number of elements in bundle
   */
  public SortedIntMap( int aBundleCapacity ) {
    super( new SortedIntLinkedBundleList( aBundleCapacity, true ),
        new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

}
