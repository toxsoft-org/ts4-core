package org.toxsoft.core.tslib.utils.diff;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility methods to compare and calculate differences between containers/collections.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "unused", "nls" } )
public class DiffUtils {

  /**
   * Compares two lists and returns difference.
   * <p>
   * <code>null</code> argument of left/right collection is considered as an empty collection.
   * <p>
   * Returned map always contains entries for all {@link EDiffNature} constants. However, by definition of the
   * comparison algorithm, the list of elements with the keys {@link EDiffNature#NONE} and {@link EDiffNature#DIFF} are
   * always empty.
   *
   * @param <E> - element type of the collection
   * @param aLeft {@link IList} - the left list of comparison, may be <code>null</code>
   * @param aRight {@link IList} - the right list of comparison, may be <code>null</code>
   * @return {@link IMapEdit}&lt;{@link EDiffNature},{@link IListEdit}&gt; - an editable result of comparison
   */
  public static <E> IMapEdit<EDiffNature, IListEdit<E>> compareLists( IList<E> aLeft, IList<E> aRight ) {
    IList<E> left = aLeft != null ? aLeft : IList.EMPTY;
    IList<E> right = aRight != null ? aRight : IList.EMPTY;
    // prepare for big collection
    int order = TsCollectionsUtils.estimateOrder( left.size() + right.size() );
    int initialCapacity = TsCollectionsUtils.getListInitialCapacity( order );
    int bucketsCount = TsCollectionsUtils.getMapBucketsCount( order );
    // initialize resulting map
    IMapEdit<EDiffNature, IListEdit<E>> map = new ElemMap<>( bucketsCount, initialCapacity );
    for( EDiffNature dn : EDiffNature.asList() ) {
      IListEdit<E> ll = new ElemLinkedBundleList<>( initialCapacity, true );
      map.put( dn, ll );
    }
    // calculate differences for each element of the both lists
    IList<E> allElems = TsCollectionsUtils.union( left, right );
    for( E e : allElems ) {
      E elemLeft = left.hasElem( e ) ? e : null;
      E elemRight = right.hasElem( e ) ? e : null;
      EDiffNature dn = EDiffNature.diff( elemLeft, elemRight );
      map.getByKey( dn ).add( e );
    }
    return map;
  }

  /**
   * Compares two lists and returns difference.
   * <p>
   * <code>null</code> argument of left/right collection is considered as an empty collection.
   *
   * @param <E> - element type of the collection
   * @param aLeft {@link IStridablesList} - the left list of comparison, may be <code>null</code>
   * @param aRight {@link IStridablesList} - the right list of comparison, may be <code>null</code>
   * @return {@link IMapEdit}&lt;{@link EDiffNature},{@link IStridablesListEdit}&gt; - an editable result of comparison
   */
  public static <E extends IStridable> IMapEdit<EDiffNature, IStridablesListEdit<E>> compareStridablesLists(
      IStridablesList<E> aLeft, IStridablesList<E> aRight ) {
    IStridablesList<E> left = aLeft != null ? aLeft : IStridablesList.EMPTY;
    IStridablesList<E> right = aRight != null ? aRight : IStridablesList.EMPTY;
    // prepare for big collection
    int order = TsCollectionsUtils.estimateOrder( left.size() + right.size() );
    int initialCapacity = TsCollectionsUtils.getListInitialCapacity( order );
    int bucketsCount = TsCollectionsUtils.getMapBucketsCount( order );
    // initialize resulting map
    IMapEdit<EDiffNature, IStridablesListEdit<E>> map = new ElemMap<>( bucketsCount, initialCapacity );
    for( EDiffNature dn : EDiffNature.asList() ) {
      IStridablesListEdit<E> ll = new StridablesList<>( initialCapacity );
      map.put( dn, ll );
    }
    // calculate differences for each key of the both lists
    IList<E> allElems = TsCollectionsUtils.union( left, right );
    for( E e : allElems ) {
      E elemLeft = left.hasElem( e ) ? e : null;
      E elemRight = right.hasElem( e ) ? e : null;
      EDiffNature dn = EDiffNature.diff( elemLeft, elemRight );
      map.getByKey( dn ).add( e );
    }
    return map;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public static <K, V> IMap<EDiffNature, IList<K>> compareMaps( IMap<K, V> aLeft, IMap<K, V> aRight ) {
    IMap<K, V> left = aLeft != null ? aLeft : IMap.EMPTY;
    IMap<K, V> right = aRight != null ? aRight : IMap.EMPTY;
    // prepare for big collection
    int order = TsCollectionsUtils.estimateOrder( left.size() + right.size() );
    int initialCapacity = TsCollectionsUtils.getListInitialCapacity( order );
    int bucketsCount = TsCollectionsUtils.getMapBucketsCount( order );
    // initialize resulting map
    IMapEdit<EDiffNature, IListEdit<K>> map = new ElemMap<>();
    for( EDiffNature dn : EDiffNature.asList() ) {
      IListEdit<K> ll = new ElemLinkedBundleList<>( initialCapacity, true );
      map.put( dn, ll );
    }
    // calculate differences for each key of the both lists
    IList<K> allKeys = TsCollectionsUtils.union( left.keys(), right.keys() );
    for( K k : allKeys ) {
      V elemLeft = left.hasKey( k ) ? left.getByKey( k ) : null;
      V elemRight = right.hasKey( k ) ? right.getByKey( k ) : null;
      EDiffNature dn = EDiffNature.diff( elemLeft, elemRight );
      map.getByKey( dn ).add( k );
    }
    return (IMap)map;
  }

  public static <V> IMap<EDiffNature, IIntList> compareIntMaps( IIntMap<V> aLeft, IIntMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, ILongList> compareLongMaps( ILongMap<V> aLeft, ILongMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, IStringList> compareStringMaps( IStringMap<V> aLeft, IStringMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <K, V> IMap<EDiffNature, IList<K>> compareMapsEx( IList<K> aExKeys, IMap<K, V> aLeft,
      IMap<K, V> aRight ) {
    // TODO реализовать DiffUtils.compareMapsEx()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMapsEx()" );
  }

  public static <V> IMap<EDiffNature, IIntList> compareIntMapsEx( IIntList aExKeys, IIntMap<V> aLeft,
      IIntMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, ILongList> compareLongMapsEx( ILongList aExKeys, ILongMap<V> aLeft,
      ILongMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  public static <V> IMap<EDiffNature, IStringList> compareStringMapsEx( IStringList aExKeys, IStringMap<V> aLeft,
      IStringMap<V> aRight ) {
    // TODO реализовать DiffUtils.compareMaps()
    throw new TsUnderDevelopmentRtException( "DiffUtils.compareMaps()" );
  }

  /**
   * No subclasses.
   */
  private DiffUtils() {
    // nop
  }

}
