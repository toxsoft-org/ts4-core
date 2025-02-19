package org.toxsoft.core.tslib.coll.primtypes;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A collection that maps <code>long</code> keys to values.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public interface ILongMap<E>
    extends IMap<Long, E> {

  /**
   * Always empty immutable instance.
   */
  @SuppressWarnings( "rawtypes" )
  ILongMapEdit EMPTY = new InternalNullLongMap();

  /**
   * Determines if map contains value with specified key.
   *
   * @param aKey long - key whose presence in this map is to be tested
   * @return <b>true</b> - да, элемент с таким ключом есть в карте;<br>
   *         <b>false</b> - нет такого ключа.
   */
  boolean hasKey( long aKey );

  /**
   * Returns the value to which the specified key is mapped.
   *
   * @param aKey long - the key whose associated value is to be returned
   * @return &lt;E&gt; - the value to which the specified key is mapped
   * @throws TsItemNotFoundRtException specified key not found in map
   */
  default E getByKey( long aKey ) {
    return TsItemNotFoundRtException.checkNull( findByKey( aKey ) );
  }

  /**
   * Finds the value to which the specified key is mapped.
   *
   * @param aKey long - the key whose associated value is to be returned
   * @return &lt;E&gt; - the value to which the specified key is mapped or <code>null</code> if none found
   */
  E findByKey( long aKey );

  /**
   * Returns ordered list of all keys.
   * <p>
   * Each element of {@link #keys()} list has assosiated value with the same index in {@link #values()} list.
   * <p>
   * This methods always returns the same reference. However elements may change their positions during map
   * modifications.
   *
   * @return {@link ILongList}&lt;K&gt; - ordered list of all keys
   */
  @Override
  ILongList keys();

  @Override
  default boolean hasKey( Long aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return hasKey( aKey.longValue() );
  }

  @Override
  default E findByKey( Long aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return findByKey( aKey.longValue() );
  }

}

/**
 * Internal class for {@link ILongMap#EMPTY} singleton implementation.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
class InternalNullLongMap<E>
    extends ImmutableLongMap<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ILongMap#EMPTY} will be read correctly.
   *
   * @return Object - always {@link ILongMap#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ILongMap.EMPTY;
  }

  // Object methods are implemented in parent class

}
