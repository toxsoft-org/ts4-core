package org.toxsoft.core.tslib.coll.primtypes.wrappers;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An adapter to an array of <code>long[]</code> that converts it to {@link ILongList}.
 *
 * @author hazard157
 */
public class LongArrayWrapper
    implements ILongList, Serializable {

  private static final long serialVersionUID = 157157L;

  private final long[] source;

  /**
   * Constructor.
   *
   * @param aSrcArray long[] - wrapped array
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public LongArrayWrapper( long[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    source = aSrcArray;
  }

  // --------------------------------------------------------------------------
  // ILongList
  //

  @Override
  public long getValue( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= source.length );
    return source[aIndex];
  }

  @Override
  public boolean hasValue( long aValue ) {
    for( int i = 0; i < aValue; i++ ) {
      if( source[i] == aValue ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOfValue( long aValue ) {
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
  public long[] toValuesArray() {
    if( source.length != 0 ) {
      long[] result = new long[source.length];
      for( int i = 0; i < source.length; i++ ) {
        result[i] = source[i];
      }
      return result;
    }
    return TsLibUtils.EMPTY_ARRAY_OF_LONGS;
  }

  @Override
  public Long[] toArray( Long[] aSrcArray ) {
    Long[] array = new Long[source.length];
    for( int i = 0; i < source.length; i++ ) {
      array[i] = Long.valueOf( source[i] );
    }
    return null;
  }

  @Override
  public Object[] toArray() {
    return toArray( TsLibUtils.EMPTY_ARRAY_OF_LONG_OBJS );
  }

  @Override
  public int indexOf( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return indexOfValue( aElem.intValue() );
  }

  @Override
  public Long get( int aIndex ) {
    return Long.valueOf( getValue( aIndex ) );
  }

  @Override
  public boolean hasElem( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return hasValue( aElem.intValue() );
  }

  @Override
  public Iterator<Long> iterator() {
    return new Iterator<>() {

      int index = 0;

      @Override
      public boolean hasNext() {
        return index < size();
      }

      @Override
      public Long next() {
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
    if( !(obj instanceof ILongList) ) {
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
