package org.toxsoft.core.tslib.coll.impl;

import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMapEdit} implementation with {@link IMap#keys()} sorted in ascending order by external comparator.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
@SuppressWarnings( "rawtypes" )
public class SortedElemMapEx<K extends Comparable, E>
    extends AbstractElemMap<K, E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   *
   * @param aComparator {@link Comparable} - keys comparator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SortedElemMapEx( Comparator<K> aComparator ) {
    this( aComparator, TsCollectionsUtils.DEFAULT_BUCKETS_COUNT, TsCollectionsUtils.DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Constructor with invariants.
   *
   * @param aComparator {@link Comparable} - keys comparator
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   * @param aBundleCapacity int - number of elements in bundle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SortedElemMapEx( Comparator<K> aComparator, int aBucketsCount, int aBundleCapacity ) {
    super( aBucketsCount, //
        new SortedElemLinkedBundleListEx<>( aComparator, aBundleCapacity, true ), //
        new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

}
