package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Sorted by ids implementation of {@link IStridablesList}.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public class SortedStridablesList<E extends IStridable>
    extends AbstractStridablesList<E, IStringListBasicEdit>
    implements ITsSortedCollectionTag {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  public SortedStridablesList( int aBundleCapacity ) {
    super( new SortedStringLinkedBundleList( aBundleCapacity, true ),
        new ElemLinkedBundleList<>( aBundleCapacity, true ) );
  }

  /**
   * Constructor.
   */
  public SortedStridablesList() {
    this( DEFAULT_BUNDLE_CAPACITY );
  }

  /**
   * Constructor from collection.
   *
   * @param aColl ITsCollection&lt;E&gt; - source collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public SortedStridablesList( ITsCollection<E> aColl ) {
    this();
    TsNullArgumentRtException.checkNull( aColl );
    addAll( aColl );
  }

  /**
   * Constructor from array.
   *
   * @param aElems &lt;E&gt;[] - source array
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SafeVarargs
  public SortedStridablesList( E... aElems ) {
    this();
    TsErrorUtils.checkArrayArg( aElems );
    addAll( aElems );
  }

  // --------------------------------------------------------------------------
  // IStridablesListBasicEdit
  //

  @Override
  public int put( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    int index = ids.indexOf( aElem.id() );
    if( index < 0 ) {
      index = ids.add( aElem.id() );
      values.insert( index, aElem );
    }
    else {
      values.set( index, aElem );
    }
    return index;
  }

  @Override
  public E replace( String aId, E aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    int index = ids.indexOf( aId );
    if( index < 0 ) {
      TsItemAlreadyExistsRtException.checkTrue( ids.hasElem( aItem.id() ) );
      ids.add( aItem.id() );
      values.add( aItem );
      return null;
    }
    E oldItem = values.removeByIndex( index );
    ids.removeByIndex( index );
    index = ids.add( aItem.id() );
    values.insert( index, aItem );
    return oldItem;
  }

}
