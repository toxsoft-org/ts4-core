package org.toxsoft.core.tslib.coll.wrappers;

import java.io.Serializable;
import java.util.*;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Адаптер к массиву эдементов (E[]), превращающий его в {@link IList}.
 *
 * @author hazard157
 * @version $id$
 * @param <E> - тип элементов списка
 */
public class ElemArrayWrapper<E>
    implements IList<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final E[] source;

  /**
   * Создает неизменяемый список элементов как оболочку над массивом.
   *
   * @param aSrcArray E[] - массив элементов
   * @throws TsNullArgumentRtException - aSrcArray = null
   */
  public ElemArrayWrapper( E[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    for( E s : aSrcArray ) {
      TsNullArgumentRtException.checkNull( s );
    }
    source = aSrcArray;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IList<E>
  //

  @Override
  public E get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= source.length );
    return source[aIndex];
  }

  @Override
  public boolean hasElem( E aString ) {
    TsNullArgumentRtException.checkNull( aString );
    for( int i = 0; i < source.length; i++ ) {
      if( source[i].equals( aString ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOf( E aString ) {
    TsNullArgumentRtException.checkNull( aString );
    for( int i = 0; i < source.length; i++ ) {
      if( source[i].equals( aString ) ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public boolean isEmpty() {
    return source.length == 0;
  }

  @Override
  public int size() {
    return source.length;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E[] toArray( E[] aSrcArray ) {
    if( aSrcArray.length < source.length ) {
      // Make a new array of a's runtime type, but my contents:
      return (E[])Arrays.copyOf( source, source.length, aSrcArray.getClass() );
    }
    System.arraycopy( source, 0, aSrcArray, 0, source.length );
    for( int i = source.length; i < aSrcArray.length; i++ ) {
      aSrcArray[i] = null;
    }
    return aSrcArray;
  }

  @Override
  public Object[] toArray() {
    if( source.length == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
    }
    return Arrays.copyOf( source, source.length );
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса Iterable
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

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IList) ) {
      return false;
    }
    return source.equals( obj );
  }

  @Override
  public int hashCode() {
    return source.hashCode();
  }

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

}
