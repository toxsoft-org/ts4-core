package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Internal template for {@link IStringMap} sorted and unsorted implementations.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class AbstractStringMap<E>
    implements IStringMapEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IStringListBasicEdit     keyList;
  private final IListEdit<E>             elemList;
  private final ElemMapInternalIntList[] buckets;

  private final int bucketArrayLength;

  transient int changeCount = 0; // Counter of list editing operations used for concurrent access detection

  /**
   * Constructor for subclasses.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   * @param aKeysList {@link IStringListBasicEdit}&lt;K:gt; - keys list, may be sorted
   * @param aElemList {@link IListEdit}&lt;E&gt; - values list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractStringMap( int aBucketsCount, IStringListBasicEdit aKeysList, IListEdit<E> aElemList ) {
    int bucketsCount = calculateNextPrimeNumber( aBucketsCount );
    TsNullArgumentRtException.checkNulls( aKeysList, aElemList );
    keyList = aKeysList;
    elemList = aElemList;
    int bal = (int)Math.sqrt( bucketsCount );
    if( bal < 4 ) {
      bal = 4;
    }
    bucketArrayLength = bal;
    buckets = new ElemMapInternalIntList[bucketsCount];
    Arrays.fill( buckets, null );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Updates indexes in hash table after element removal.
   * <p>
   * After removing key from {@link #keyList} with index aRemovedIndex all index elements in hash table above
   * aRemovedIndex are decremented by 1.
   *
   * @param aRemovedIndex int - index of key removed from {@link #keyList}
   */
  private void adjustIndicesAfterRemove( int aRemovedIndex ) {
    for( int bIndex = 0; bIndex < buckets.length; bIndex++ ) {
      ElemMapInternalIntList elemIndexList = buckets[bIndex];
      if( elemIndexList != null ) {
        elemIndexList.decreaseAllAboveThreshold( aRemovedIndex );
      }
    }
  }

  // /**
  // * Updates indexes in hash table after element removal.
  // * <p>
  // * After removing key from {@link #keyList} with index aRemovedIndex all index elements in hash table above
  // * aRemovedIndex are decremented by 1.
  // *
  // * @param aRemovedIndex int - index of key removed from {@link #keyList}
  // */
  // private void adjustIndexesAfterRemove( int aRemovedIndex ) {
  // for( int bIndex = 0; bIndex < buckets.length; bIndex++ ) {
  // IIntListEdit elemIndexList = buckets[bIndex];
  // if( elemIndexList != null ) {
  // for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
  // int elemIndex = elemIndexList.getValue( i );
  // if( elemIndex > aRemovedIndex ) {
  // elemIndexList.set( i, elemIndex - 1 );
  // }
  // }
  // }
  // }
  // }

  /**
   * Updates indexes in hash table before element insertion.
   * <p>
   * After inserting key to {@link #keyList} at index aIndexToInsert all index elements in hash table above
   * aRemovedIndex are incremented by 1.
   *
   * @param aIndexToInsert int - index of key removed from {@link #keyList}
   */
  private void adjustIndicesBeforeInsert( int aIndexToInsert ) {
    for( int bIndex = 0; bIndex < buckets.length; bIndex++ ) {
      ElemMapInternalIntList elemIndexList = buckets[bIndex];
      if( elemIndexList != null ) {
        elemIndexList.increaseAllAboveThreshold( aIndexToInsert );
      }
    }
  }

  /**
   * Returns index of hash table cell by hash code.
   * <p>
   * Decendants may override this method to guarantee that correct cell index will be returned far any value of the
   * argument.
   *
   * @param aHashCode int - hash code of the key
   * @return int - cell index in range (0..buckets.length-1)
   */
  protected int bucketIndex( int aHashCode ) {
    if( aHashCode < 0 ) {
      if( aHashCode == Integer.MIN_VALUE ) {
        return 0;
      }
      return (-aHashCode) % buckets.length;
    }
    return aHashCode % buckets.length;
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return elemList.iterator();
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return keyList.size();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( E aElem ) {
    return elemList.hasElem( aElem );
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return elemList.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return elemList.toArray();
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    ElemMapInternalIntList elemIndexList = buckets[bucketIndex( aKey.hashCode() )];
    if( elemIndexList != null ) {
      for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
        int eIndex = elemIndexList.get( i );
        if( keyList.get( eIndex ).equals( aKey ) ) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public E findByKey( String aKey ) {
    if( aKey == null ) {
      throw new TsNullArgumentRtException();
    }
    ElemMapInternalIntList elemIndexList = buckets[bucketIndex( aKey.hashCode() )];
    if( elemIndexList != null ) {
      for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
        int eIndex = elemIndexList.get( i );
        if( keyList.get( eIndex ).equals( aKey ) ) {
          return elemList.get( eIndex );
        }
      }
    }
    return null;
  }

  @Override
  public IList<E> values() {
    return elemList;
  }

  // ------------------------------------------------------------------------------------
  // IStringMap
  //

  @Override
  public IStringList keys() {
    return keyList;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    if( !isEmpty() ) {
      keyList.clear();
      elemList.clear();
      for( int i = 0; i < buckets.length; i++ ) {
        if( buckets[i] != null ) {
          buckets[i].clear();
        }
      }
      ++changeCount;
    }
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
  //

  @Override
  public E put( String aKey, E aElem ) {
    if( aKey == null || aElem == null ) {
      throw new TsNullArgumentRtException();
    }
    checkArgsValidity( aKey, aElem );
    // find bucket and ensure it exists
    int bIndex = bucketIndex( aKey.hashCode() );
    ElemMapInternalIntList elemIndexList = buckets[bIndex];
    if( elemIndexList == null ) {
      elemIndexList = new ElemMapInternalIntList( bucketArrayLength );
      buckets[bIndex] = elemIndexList;
    }
    // if key is already in map, just set new value
    for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
      int eIndex = elemIndexList.get( i );
      if( keyList.get( eIndex ).equals( aKey ) ) {
        ++changeCount;
        return elemList.set( eIndex, aElem );
      }
    }
    // add key/value pair at coorect place either for sorted or unsorted maps
    int index = keyList.add( aKey );
    elemList.insert( index, aElem );
    if( index < keyList.size() - 1 ) {
      adjustIndicesBeforeInsert( index );
    }
    elemIndexList.add( index );
    ++changeCount;
    return null;
  }

  @Override
  public E removeByKey( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    ElemMapInternalIntList elemIndexList = buckets[bucketIndex( aKey.hashCode() )];
    if( elemIndexList != null ) {
      for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
        int eIndex = elemIndexList.get( i );
        if( keyList.get( eIndex ).equals( aKey ) ) {
          keyList.removeByIndex( eIndex );
          elemIndexList.remove( i );
          adjustIndicesAfterRemove( eIndex );
          ++changeCount;
          return elemList.removeByIndex( eIndex );
        }
      }
    }
    return null;
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
    if( !(obj instanceof IStringMap) ) {
      return false;
    }
    IStringMap<?> map = (IStringMap<?>)obj;
    int sz = map.size();
    if( sz != size() ) {
      return false;
    }
    for( int i = 0; i < sz; i++ ) {
      if( !map.keys().get( i ).equals( this.keys().get( i ) ) ) {
        return false;
      }
      if( !map.values().get( i ).equals( this.values().get( i ) ) ) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( int i = 0, n = size(); i < n; i++ ) {
      result = TsLibUtils.PRIME * result + keys().get( i ).hashCode();
      result = TsLibUtils.PRIME * result + values().get( i ).hashCode();
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may throw an exception if any argument of the method{@link #put(String, Object)} is not accepted.
   * <p>
   * Note: checking {@link #put(String, Object)} is the same as to check any key/values pair because this is the only
   * method that adds entires to this map.
   * <p>
   * Does nothong in the base class there is no need to call superclass method when overriding.
   *
   * @param aKey String - the key, never is <code>null</code>
   * @param aElem &lt;E&gt; - the value, never is <code>null</code>
   * @throws TsRuntimeException (or subclass) is any argument is not accepted by the subclass
   */
  protected void checkArgsValidity( String aKey, E aElem ) {
    // all non-null arguments are accepted
  }

}
