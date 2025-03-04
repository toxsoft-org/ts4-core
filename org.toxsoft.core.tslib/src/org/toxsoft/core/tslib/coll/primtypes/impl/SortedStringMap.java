package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation of the {@link IStringMap} with the keys sorted in {@link String} natural order.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class SortedStringMap<E>
    extends AbstractStringMap<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   * @param aBundleCapacity int - number of elements in bundle
   */
  public SortedStringMap( int aBucketsCount, int aBundleCapacity ) {
    super( aBucketsCount, //
        new SortedStringLinkedBundleList( aBundleCapacity, true ), //
        new ElemLinkedBundleList<>( aBundleCapacity, true ) //
    );
  }

  /**
   * Constructor.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   */
  public SortedStringMap( int aBucketsCount ) {
    this( aBucketsCount, DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Constructor.
   */
  public SortedStringMap() {
    this( DEFAULT_BUCKETS_COUNT, DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link IStringMap}&lt;E&gt; - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SortedStringMap( IStringMap<E> aSource ) {
    this( DEFAULT_BUCKETS_COUNT, DEFAULT_BUNDLE_CAPACITY );
    setAll( aSource );
  }

}
