package org.toxsoft.core.tslib.coll.primtypes.wrappers;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An adapter to an array of <code>int[]</code> that converts it to {@link IIntList}.
 *
 * @author hazard157
 */
public class IntArrayWrapper
    implements IIntList, Serializable {

  private static final long serialVersionUID = 157157L;

  private final int[] source;

  /**
   * Constructor.
   *
   * @param aSrcArray int[] - wrapped array
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IntArrayWrapper( int[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    source = aSrcArray;
  }

  // --------------------------------------------------------------------------
  // IIntList
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
  // Object
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
