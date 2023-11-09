package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import java.util.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Immutable implementation of {@link IStridablesList} containing one element.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public class SingleStridableList<E extends IStridable>
    implements IStridablesList<E> {

  private final E           item;
  private final IStringList keys;
  private final IList<E>    values;

  /**
   * Constructor.
   *
   * @param aItem &lt;E&gt; - the single item of the list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SingleStridableList( E aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    item = aItem;
    keys = new SingleStringList( item.id() );
    values = new SingleItemList<>( item );
  }

  @Override
  public int indexOf( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    if( item.equals( aElem ) ) {
      return 0;
    }
    return -1;
  }

  @Override
  public E get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex != 0 );
    return item;
  }

  @Override
  public boolean hasElem( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return item.equals( aElem );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E[] toArray( E[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( aSrcArray.length == 0 ) {
      // Make a new array of a's runtime type, but my contents:
      Object a[] = Arrays.copyOf( aSrcArray, 1, aSrcArray.getClass() );
      a[0] = item;
      return (E[])a;
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

  @Override
  public int size() {
    return 1;
  }

  @Override
  public boolean hasKey( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return aKey.equals( item.id() );
  }

  @Override
  public E findByKey( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    if( aKey.equals( item.id() ) ) {
      return item;
    }
    return null;
  }

  @Override
  public IList<String> keys() {
    return keys;
  }

  @Override
  public IList<E> values() {
    return values;
  }

  @Override
  public IStringList ids() {
    return keys;
  }

}
