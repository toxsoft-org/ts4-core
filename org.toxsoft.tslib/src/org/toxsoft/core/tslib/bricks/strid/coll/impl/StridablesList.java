package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import static org.toxsoft.core.tslib.bricks.strid.coll.impl.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStridablesList} implementation.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public class StridablesList<E extends IStridable>
    extends AbstractStridablesList<E, IStringListEdit>
    implements IStridablesListEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public StridablesList() {
    super( new StringLinkedBundleList() );
  }

  /**
   * Constructor from collection.
   *
   * @param aColl ITsCollection&lt;E&gt; - source collection
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public StridablesList( ITsCollection<E> aColl ) {
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
  public StridablesList( E... aElems ) {
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
    if( index >= 0 ) {
      values.set( index, aElem );
      return index;
    }
    ids.add( aElem.id() );
    values.add( aElem );
    return ids.size() - 1;
  }

  @Override
  public E replace( String aId, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    int index = ids.indexOf( aId );
    if( index < 0 ) {
      // check that there is no item with ID = aElem.id()
      TsItemAlreadyExistsRtException.checkTrue( ids.hasElem( aElem.id() ), FMT_ERR_CANT_REPLACE_BY_ID, aId,
          aElem.id() );
      ids.add( aElem.id() );
      values.add( aElem );
      return null;
    }
    E oldItem = values.get( index );
    ids.set( index, aElem.id() );
    values.set( index, aElem );
    return oldItem;
  }

  // --------------------------------------------------------------------------
  // IStridablesListEdit
  //

  @Override
  public E set( int aIndex, E aElem ) {
    if( values.get( aIndex ) == aElem ) {
      return aElem;
    }
    TsNullArgumentRtException.checkNull( aElem );
    ids.set( aIndex, aElem.id() );
    return values.set( aIndex, aElem );
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > ids.size() );
    int index = ids.indexOf( aElem.id() );
    if( index < 0 ) {
      index = aIndex;
    }
    ids.insert( aIndex, aElem.id() );
    values.insert( aIndex, aElem );
  }

}
