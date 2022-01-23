package org.toxsoft.tslib.coll.primtypes.impl;

import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.coll.primtypes.ILongMap;

/**
 * {@link ILongMap} implementation.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class LongMap<E>
    extends LongMapBase<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public LongMap() {
    super( new LongLinkedBundleList(), new ElemLinkedBundleList<>() );
  }

  /**
   * Constructs the map with internal list's specified capacity.
   *
   * @param aBundleCapacity int - number of elements in bundle
   */
  public LongMap( int aBundleCapacity ) {
    super( new LongLinkedBundleList( aBundleCapacity, true ), new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

}
