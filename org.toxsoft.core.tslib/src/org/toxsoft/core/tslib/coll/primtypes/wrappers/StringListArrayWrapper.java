package org.toxsoft.core.tslib.coll.primtypes.wrappers;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Wraps string array (String[]), to represent as a {@link IStringList}.
 *
 * @author hazard157
 */
public class StringListArrayWrapper
    implements IStringList, Serializable {

  private static final long serialVersionUID = 157157L;

  private final String[] srcArray;

  /**
   * Constructor.
   *
   * @param aSrcArray String[] - the array to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StringListArrayWrapper( String[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    for( String s : aSrcArray ) {
      TsNullArgumentRtException.checkNull( s );
    }
    srcArray = aSrcArray;
  }

  // --------------------------------------------------------------------------
  // IStringList
  //

  @Override
  public String get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= srcArray.length );
    return srcArray[aIndex];
  }

  @Override
  public boolean hasElem( String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    for( int i = 0; i < srcArray.length; i++ ) {
      if( srcArray[i].equals( aString ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOf( String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    for( int i = 0; i < srcArray.length; i++ ) {
      if( srcArray[i].equals( aString ) ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public boolean isEmpty() {
    return srcArray.length != 0;
  }

  @Override
  public int size() {
    return srcArray.length;
  }

  @Override
  public String[] toArray() {
    if( srcArray.length != 0 ) {
      String[] result = new String[srcArray.length];
      for( int i = 0; i < srcArray.length; i++ ) {
        result[i] = srcArray[i];
      }
      return result;
    }
    return TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
  }

  @Override
  public String[] toArray( String[] aSrcArray ) {
    if( srcArray.length == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
    }
    String[] result = new String[srcArray.length];
    System.arraycopy( srcArray, 0, result, 0, srcArray.length );
    return result;
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<String> iterator() {
    return new Iterator<>() {

      int index = 0;

      @Override
      public boolean hasNext() {
        return index < size();
      }

      @Override
      public String next() {
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
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof IStringList ) {
      return TsCollectionsUtils.isListsEqual( this, (IStringList)aObj );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( int i = 0; i < srcArray.length; i++ ) {
      result = TsLibUtils.PRIME * result + srcArray[i].hashCode();
    }
    return result;
  }

}
