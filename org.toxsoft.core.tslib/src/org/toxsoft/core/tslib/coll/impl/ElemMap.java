package org.toxsoft.core.tslib.coll.impl;

import org.toxsoft.core.tslib.coll.IMap;

/**
 * {@link IMap} implementation.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public class ElemMap<K, E>
    extends AbstractElemMap<K, E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public ElemMap() {
    this( TsCollectionsUtils.DEFAULT_BUCKETS_COUNT, TsCollectionsUtils.DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Constructor with invariants.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   * @param aBundleCapacity int - number of elements in bundle
   */
  public ElemMap( int aBucketsCount, int aBundleCapacity ) {
    super( aBucketsCount, //
        new ElemLinkedBundleList<>( aBundleCapacity, true ), //
        new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

}
