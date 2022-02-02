package org.toxsoft.core.tslib.coll.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.Serializable;
import java.util.Iterator;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Internal template for {@link IMap} sorted and unsorted implementations.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
class AbstractElemMap<K, E>
    implements IMapEdit<K, E>, Serializable {

  private static final long serialVersionUID = 158158L;

  private final IListBasicEdit<K>        keyList;
  private final IListEdit<E>             elemList;
  private final ElemMapInternalIntList[] buckets;

  private final int bucketArrayLength;

  transient int changeCount = 0; // Counter of list editing operations used for concurrent access detection

  /**
   * Construcor for subclasses.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   * @param aKeysList {@link IListBasicEdit}&lt;K:gt; - keys list, may be sorted
   * @param aElemList {@link IListEdit}&lt;E&gt; - values list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractElemMap( int aBucketsCount, IListBasicEdit<K> aKeysList, IListEdit<E> aElemList ) {
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
    for( int i = 0; i < buckets.length; i++ ) {
      buckets[i] = null;
    }
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
  private void adjustIndexesAfterRemove( int aRemovedIndex ) {
    for( int bIndex = 0; bIndex < buckets.length; bIndex++ ) {
      ElemMapInternalIntList elemIndexList = buckets[bIndex];
      if( elemIndexList != null ) {
        for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
          int elemIndex = elemIndexList.get( i );
          if( elemIndex > aRemovedIndex ) {
            elemIndexList.set( i, elemIndex - 1 );
          }
        }
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
  public boolean hasKey( K aKey ) {
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
  public E findByKey( K aKey ) {
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
  public IList<K> keys() {
    return keyList;
  }

  @Override
  public IList<E> values() {
    return elemList;
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
  public E put( K aKey, E aElem ) {
    if( aKey == null || aElem == null ) {
      throw new TsNullArgumentRtException();
    }
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
    // add key/value pair at coorect place eithr for sorted or unsorted maps
    int index = keyList.add( aKey );
    elemList.insert( index, aElem );
    elemIndexList.add( index );
    ++changeCount;
    return null;
  }

  @Override
  public E removeByKey( K aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    ElemMapInternalIntList elemIndexList = buckets[bucketIndex( aKey.hashCode() )];
    if( elemIndexList != null ) {
      for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
        int eIndex = elemIndexList.get( i );
        if( keyList.get( eIndex ).equals( aKey ) ) {
          keyList.removeByIndex( eIndex );
          elemIndexList.remove( i );
          adjustIndexesAfterRemove( eIndex );
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

  @SuppressWarnings( "rawtypes" )
  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IList) ) {
      return false;
    }
    return TsCollectionsUtils.isMapsEqual( this, (IMap)obj );
  }

  @Override
  public int hashCode() {
    return TsCollectionsUtils.calcMapHashCode( this );
  }

}
