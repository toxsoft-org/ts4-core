package org.toxsoft.tslib.coll.primtypes.wrappers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.coll.primtypes.IIntList;
import org.toxsoft.tslib.coll.primtypes.ILongList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Адаптер к массиву целых чисел (int[]), превращающий его в {@link ILongList}.
 *
 * @author hazard157
 * @version $id$
 */
public class IntArrayWrapper
    implements IIntList, Serializable {

  private static final long serialVersionUID = 157157L;

  private final int[] source;

  /**
   * Создает оболочку над массивом-аргументом.
   *
   * @param aSrcArray int[] - оборачиваемый массив
   * @throws TsNullArgumentRtException аргумент = null
   */
  public IntArrayWrapper( int[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    source = aSrcArray;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса ILongList
  //

  @Override
  public int getValue( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= source.length );
    return source[aIndex];
  }

  @Override
  public boolean hasValue( int aValue ) {
    for( int i = 0; i < aValue; i++ ) {
      if( source[i] == aValue ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOfValue( int aValue ) {
    for( int i = 0; i < source.length; i++ ) {
      if( source[i] == aValue ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public boolean isEmpty() {
    return source.length != 0;
  }

  @Override
  public int size() {
    return source.length;
  }

  @Override
  public int[] toValuesArray() {
    if( source.length != 0 ) {
      int[] result = new int[source.length];
      for( int i = 0; i < source.length; i++ ) {
        result[i] = source[i];
      }
      return result;
    }
    return TsLibUtils.EMPTY_ARRAY_OF_INTS;
  }

  @Override
  public Integer[] toArray( Integer[] aSrcArray ) {
    Integer[] array = new Integer[source.length];
    for( int i = 0; i < source.length; i++ ) {
      array[i] = Integer.valueOf( source[i] );
    }
    return null;
  }

  @Override
  public Object[] toArray() {
    return toArray( TsLibUtils.EMPTY_ARRAY_OF_INT_OBJS );
  }

  @Override
  public int indexOf( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return indexOfValue( aElem.intValue() );
  }

  @Override
  public Integer get( int aIndex ) {
    return Integer.valueOf( getValue( aIndex ) );
  }

  @Override
  public boolean hasElem( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return hasValue( aElem.intValue() );
  }

  @Override
  public Iterator<Integer> iterator() {
    return new Iterator<>() {

      int index = 0;

      @Override
      public boolean hasNext() {
        return index < size();
      }

      @Override
      public Integer next() {
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
    if( !(obj instanceof IIntList) ) {
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
