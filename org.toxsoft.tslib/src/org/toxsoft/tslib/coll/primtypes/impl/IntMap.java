package org.toxsoft.tslib.coll.primtypes.impl;

import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.coll.primtypes.IIntMap;

/**
 * {@link IIntMap} implementation.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class IntMap<E>
    extends IntMapBase<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public IntMap() {
    super( new IntLinkedBundleList(), new ElemLinkedBundleList<>() );
  }

  /**
   * Constructs the map with internal list's specified capacity.
   *
   * @param aBundleCapacity int - number of elements in bundle
   */
  public IntMap( int aBundleCapacity ) {
    super( new IntLinkedBundleList( aBundleCapacity, true ), new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

}
