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
 * Implementation of the {@link IStringMap}.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class StringMap<E>
    implements IStringMapEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IStringListEdit keyList  = new StringLinkedBundleList();
  private final IListEdit<E>    elemList = new ElemLinkedBundleList<>();
  private final IIntListEdit[]  buckets;

  transient int changeCount = 0; // Counter of list editing operations used for concurrent access detection

  /**
   * Construcor.
   *
   * @param aBucketsCount int - number of cells in hash-table (rounded to nearest prime number)
   */
  public StringMap( int aBucketsCount ) {
    int bucketsCount = calculateNextPrimeNumber( aBucketsCount );
    buckets = new IIntListEdit[bucketsCount];
    Arrays.fill( buckets, null );
  }

  /**
   * Constructor.
   */
  public StringMap() {
    this( DEFAULT_BUCKETS_COUNT );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link IStringMap}&lt;E&gt; - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StringMap( IStringMap<E> aSource ) {
    this( DEFAULT_BUCKETS_COUNT );
    setAll( aSource );
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
      IIntListEdit elemIndexList = buckets[bIndex];
      if( elemIndexList != null ) {
        for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
          int elemIndex = elemIndexList.getValue( i );
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
  public boolean hasKey( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    IIntList elemIndexList = buckets[bucketIndex( aKey.hashCode() )];
    if( elemIndexList != null ) {
      for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
        int eIndex = elemIndexList.getValue( i );
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
    IIntList elemIndexList = buckets[bucketIndex( aKey.hashCode() )];
    if( elemIndexList != null ) {
      for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
        int eIndex = elemIndexList.getValue( i );
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
    int bIndex = bucketIndex( aKey.hashCode() );
    IIntListEdit elemIndexList = buckets[bIndex];
    if( elemIndexList == null ) {
      elemIndexList = new IntLinkedBundleList();
      buckets[bIndex] = elemIndexList;
    }
    for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
      int eIndex = elemIndexList.getValue( i );
      if( keyList.get( eIndex ).equals( aKey ) ) {
        ++changeCount;
        return elemList.set( eIndex, aElem );
      }
    }
    keyList.add( aKey );
    elemList.add( aElem );
    elemIndexList.add( keyList.size() - 1 );
    ++changeCount;
    return null;
  }

  @Override
  public E removeByKey( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    IIntListEdit elemIndexList = buckets[bucketIndex( aKey.hashCode() )];
    if( elemIndexList != null ) {
      for( int i = 0, n = elemIndexList.size(); i < n; i++ ) {
        int eIndex = elemIndexList.getValue( i );
        if( keyList.get( eIndex ).equals( aKey ) ) {
          keyList.removeByIndex( eIndex );
          elemIndexList.removeByIndex( i );
          adjustIndexesAfterRemove( eIndex );
          ++changeCount;
          return elemList.removeByIndex( eIndex );
        }
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
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

}
