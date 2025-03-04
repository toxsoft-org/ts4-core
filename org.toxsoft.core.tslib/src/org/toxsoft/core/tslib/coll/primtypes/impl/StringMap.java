package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation of the {@link IStringMap}.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class StringMap<E>
    extends AbstractStringMap<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   * @param aBundleCapacity int - number of elements in bundle
   */
  public StringMap( int aBucketsCount, int aBundleCapacity ) {
    super( aBucketsCount, //
        new StringLinkedBundleList( aBundleCapacity, true ), //
        new ElemLinkedBundleList<>( aBundleCapacity, true ) //
    );
  }

  /**
   * Constructor.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   */
  public StringMap( int aBucketsCount ) {
    this( aBucketsCount, DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Constructor.
   */
  public StringMap() {
    this( DEFAULT_BUCKETS_COUNT, DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link IStringMap}&lt;E&gt; - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StringMap( IStringMap<E> aSource ) {
    this( DEFAULT_BUCKETS_COUNT, DEFAULT_BUNDLE_CAPACITY );
    setAll( aSource );
  }

}
