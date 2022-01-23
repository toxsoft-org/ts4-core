package org.toxsoft.tslib.coll.primtypes.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.toxsoft.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Immutable implementation of {@link IStringList} containing one element.
 *
 * @author hazard157
 */
public class SingleStringList
    implements IStringList, ITsFastIndexListTag<String>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final String item;

  /**
   * Constructor from element value.
   *
   * @param aElem String - value of the only element
   */
  public SingleStringList( String aElem ) {
    if( aElem == null ) {
      throw new TsNullArgumentRtException();
    }
    item = aElem;
  }

  // ------------------------------------------------------------------------------------
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
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return 1;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public String[] toArray( String[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( aSrcArray.length > 0 ) {
      aSrcArray[0] = String.valueOf( item );
      return aSrcArray;
    }
    String[] result = new String[1];
    result[0] = String.valueOf( item );
    return result;
  }

  @Override
  public String[] toArray() {
    String[] result = new String[1];
    result[0] = String.valueOf( item );
    return result;
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  // nop

  // ------------------------------------------------------------------------------------
  // IStringList
  //

  @Override
  public int indexOf( String aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    if( aElem.equals( item ) ) {
      return 0;
    }
    return -1;
  }

  @Override
  public String get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex != 0 );
    return item;
  }

  @Override
  public boolean hasElem( String aElem ) {
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
