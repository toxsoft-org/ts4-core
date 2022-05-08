package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import static org.toxsoft.core.tslib.bricks.strid.coll.impl.ITsResources.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parent class for {@link IStridablesList} implementations.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 * @param <L> - type of identifiers list {@link #ids}
 */
public abstract class AbstractStridablesList<E extends IStridable, L extends IStringListBasicEdit>
    implements IStridablesListBasicEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * List of identifiers (keys).
   */
  protected final L ids;

  /**
   * List of elements.
   */
  protected final IListEdit<E> values;

  protected AbstractStridablesList( L aIdList, IListEdit<E> aValList ) {
    ids = aIdList;
    values = aValList;
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {

      int index = 0;

      @Override
      public boolean hasNext() {
        return index < size();
      }

      @Override
      public E next() {
        if( hasNext() ) {
          return get( index++ );
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

  // --------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return ids.size();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( E aElem ) {
    return values.hasElem( aElem );
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return values.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return values.toArray();
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( E aElem ) {
    return values.indexOf( aElem );
  }

  @Override
  public E get( int aIndex ) {
    return values.get( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( String aId ) {
    return ids.hasElem( aId );
  }

  @Override
  public E getByKey( String aId ) {
    E e = findByKey( aId );
    if( e == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_ITEM_NOT_FOUND_BY_ID, aId );
    }
    return e;
  }

  @Override
  public E findByKey( String aId ) {
    int index = ids.indexOf( aId );
    if( index < 0 ) {
      return null;
    }
    return values.get( index );
  }

  @Override
  public IStringList keys() {
    return ids;
  }

  @Override
  public IList<E> values() {
    return values;
  }

  // ------------------------------------------------------------------------------------
  // IStridablesList
  //

  @Override
  public IStringList ids() {
    return ids;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    ids.clear();
    values.clear();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollectionEdit
  //

  @Override
  public int add( E aElem ) {
    return put( aElem );
  }

  @Override
  public int remove( E aElem ) {
    int index = values.indexOf( aElem );
    if( index >= 0 ) {
      removeByIndex( index );
    }
    return index;
  }

  @Override
  public E removeByIndex( int aIndex ) {
    ids.removeByIndex( aIndex );
    return values.removeByIndex( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
  //

  @Override
  public E put( String aKey, E aElem ) {
    return replace( aKey, aElem );
  }

  @Override
  public E removeByKey( String aKey ) {
    return removeById( aKey );
  }

  // ------------------------------------------------------------------------------------
  // IStridablesListBasicEdit
  //

  @Override
  public abstract int put( E aElem );

  @Override
  public abstract E replace( String aId, E aElem );

  @Override
  public E removeById( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    int index = ids.indexOf( aId );
    E e = null;
    if( index >= 0 ) {
      ids.removeByIndex( index );
      e = values.removeByIndex( index );
    }
    return e;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IStridablesList) ) {
      return false;
    }
    IStridablesList<?> list = (IStridablesList<?>)obj;
    int sz = list.size();
    if( sz != size() ) {
      return false;
    }
    for( int i = 0; i < sz; i++ ) {
      if( !list.get( i ).equals( values.get( i ) ) ) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( int i = 0, n = values.size(); i < n; i++ ) {
      result = TsLibUtils.PRIME * result + values.get( i ).hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

}
