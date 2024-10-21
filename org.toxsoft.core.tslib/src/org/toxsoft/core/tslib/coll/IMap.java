package org.toxsoft.core.tslib.coll;

import static org.toxsoft.core.tslib.coll.ITsCollSharedResources.*;

import java.io.*;

import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A collection that maps keys to values.
 * <p>
 * This is basic interface of all associative collections that maps keys to values.
 * <p>
 * All <code>tslib</code> maps has both {@link #keys()} and {@link #values()} lists exposed. Even more - both lists
 * returns references to internal lists. Elements of lists are ordered either in the order of appearance of new keys or
 * in sorted order. {@link IMap} guarantees that element in {@link #keys()} list has corresponding value in
 * {@link #values()} list with the same index as key in {@link #keys()} list.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public interface IMap<K, E>
    extends ITsCollection<E> {

  /**
   * Singleton of always empty immutable map.
   */
  @SuppressWarnings( "rawtypes" )
  IMapEdit EMPTY = new InternalNullMap();

  /**
   * Detemines if map contains value with specified key.
   *
   * @param aKey &lt;K&gt; - key whose presence in this map is to be tested
   * @return <b>true</b> - да, элемент с таким ключом есть в карте;<br>
   *         <b>false</b> - нет такого ключа.
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  boolean hasKey( K aKey );

  /**
   * Returns the value to which the specified key is mapped.
   *
   * @param aKey K - the key whose associated value is to be returned
   * @return &lt;E&gt; - the value to which the specified key is mapped
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemNotFoundRtException specified key not found in map
   */
  default E getByKey( K aKey ) {
    E e = findByKey( aKey );
    if( e == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_ITEM_NOT_FOUND_BY_KEY, this.getClass().getSimpleName(), aKey );
    }
    return e;
  }

  /**
   * Finds the value to which the specified key is mapped.
   *
   * @param aKey K - the key whose associated value is to be returned
   * @return &lt;E&gt; - the value to which the specified key is mapped or <code>null</code> if none found
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  E findByKey( K aKey );

  /**
   * Returns ordered list of all keys.
   * <p>
   * Each element of {@link #keys()} list has assosiated value with the same index in {@link #values()} list.
   * <p>
   * This methods always returns the same reference. However elements may change their positions during map
   * modifications.
   *
   * @return {@link IList}&lt;K&gt; - ordered list of all keys
   */
  IList<K> keys();

  /**
   * Returns ordered list of all values.
   * <p>
   * Each element of {@link #keys()} list has assosiated value with the same index in {@link #values()} list.
   * <p>
   * This methods always returns the same reference. However elements may change their positions during map
   * modifications.
   *
   * @return {@link IList}&lt;K&gt; - ordered list of all values
   */
  IList<E> values();

  /**
   * Copies this map content to the destionation map.
   * <p>
   * Elements from this map will be added to destination by method {@link IMapEdit#putAll(IMap)}.
   * <p>
   * If argument is <code>null</code>, then new instance of {@link ElemMap} will be created and returned.
   *
   * @param aDest {@link IMapEdit} - destination map or <code>null</code> for new map
   * @return {@link IMapEdit} - returns the argument or new instance of {@link ElemMap}
   */
  default IMapEdit<K, E> copyTo( IMapEdit<K, E> aDest ) {
    IMapEdit<K, E> dest = aDest;
    if( dest == null ) {
      int estOrder = TsCollectionsUtils.estimateOrder( size() );
      int bucksCount = TsCollectionsUtils.getMapBucketsCount( estOrder );
      int bindleCapacity = TsCollectionsUtils.getListInitialCapacity( estOrder );
      dest = new ElemMap<>( bucksCount, bindleCapacity );
    }
    dest.putAll( this );
    return dest;
  }

}

/**
 * Internal class for {@link IMap#EMPTY} singleton implementation.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
class InternalNullMap<K, E>
    extends ImmutableMap<K, E> {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IMap#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IMap#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IMap.EMPTY;
  }

  // Object methods are implemented in parent class

}
