package org.toxsoft.core.tslib.coll.impl;

import java.io.Serializable;
import java.util.*;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Immutable implementation of {@link IList} containing one element.
 *
 * @author hazard157
 * @param <T> - the type of elements in this collection
 */
public class SingleItemList<T>
    implements IList<T>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final T item;

  /**
   * Constructor from element value.
   *
   * @param aElem &lt;T&gt; - value of the only element
   */
  public SingleItemList( T aElem ) {
    item = TsNullArgumentRtException.checkNull( aElem );
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<T> iterator() {
    return new Iterator<>() {

      int index = 0;

      @Override
      public boolean hasNext() {
        return index < size();
      }

      @Override
      public T next() {
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

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return 1;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @SuppressWarnings( "unchecked" )
  @Override
  public T[] toArray( T[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( aSrcArray.length == 0 ) {
      // Make a new array of a's runtime type, but my contents:
      Object a[] = Arrays.copyOf( aSrcArray, 1, aSrcArray.getClass() );
      a[0] = item;
      return (T[])a;
    }
    aSrcArray[0] = item;
    for( int i = 1; i < aSrcArray.length; i++ ) {
      aSrcArray[i] = null;
    }
    return aSrcArray;
  }

  @Override
  public Object[] toArray() {
    Object a[] = new Object[1];
    a[0] = item;
    return a;
  }

  // ------------------------------------------------------------------------------------
  // IStringList
  //

  @Override
  public int indexOf( T aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    if( aElem.equals( item ) ) {
      return 0;
    }
    return -1;
  }

  @Override
  public T get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex != 0 );
    return item;
  }

  @Override
  public boolean hasElem( T aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return item.equals( aElem );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IStringList) ) {
      return false;
    }
    return TsCollectionsUtils.isListsEqual( this, (IStringList)obj );
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + item.hashCode();
    return result;
  }

}
