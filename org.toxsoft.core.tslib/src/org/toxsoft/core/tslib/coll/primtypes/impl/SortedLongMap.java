package org.toxsoft.core.tslib.coll.primtypes.impl;

import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.coll.primtypes.ILongMap;

/**
 * {@link ILongMap} implementation with <code>int</code> keys sorted in ascending order.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class SortedLongMap<E>
    extends LongMapBase<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public SortedLongMap() {
    super( new SortedLongLinkedBundleList(), new ElemLinkedBundleList<>() );
  }

  /**
   * Constructs the map with internal list's specified capacity.
   *
   * @param aBundleCapacity int - number of elements in bundle
   */
  public SortedLongMap( int aBundleCapacity ) {
    super( new SortedLongLinkedBundleList( aBundleCapacity, true ),
        new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

}
