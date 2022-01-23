package org.toxsoft.tslib.coll.impl;

import org.toxsoft.tslib.coll.IMap;
import org.toxsoft.tslib.coll.IMapEdit;

/**
 * {@link IMapEdit} implementation with {@link IMap#keys()} sorted in ascending order.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public class SortedElemMap<K extends Comparable<K>, E>
    extends AbstractElemMap<K, E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public SortedElemMap() {
    this( TsCollectionsUtils.DEFAULT_BUCKETS_COUNT, TsCollectionsUtils.DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Constructor with invariants.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   * @param aBundleCapacity int - number of elements in bundle
   */
  public SortedElemMap( int aBucketsCount, int aBundleCapacity ) {
    super( aBucketsCount, //
        new SortedElemLinkedBundleList<>( aBundleCapacity, true ), //
        new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

}
