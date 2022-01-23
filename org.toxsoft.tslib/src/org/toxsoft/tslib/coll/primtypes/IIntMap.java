package org.toxsoft.tslib.coll.primtypes;

import java.io.ObjectStreamException;

import org.toxsoft.tslib.coll.IMap;
import org.toxsoft.tslib.coll.primtypes.impl.ImmutableIntMap;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * A collection that maps <code>int</code> keys to values.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public interface IIntMap<E>
    extends IMap<Integer, E> {

  /**
   * Alway empty immutable instance.
   */
  @SuppressWarnings( "rawtypes" )
  IIntMapEdit EMPTY = new InternalNullIntMap();

  /**
   * Detemines if map contains value with specified key.
   *
   * @param aKey int - key whose presence in this map is to be tested
   * @return <b>true</b> - да, элемент с таким ключом есть в карте;<br>
   *         <b>false</b> - нет такого ключа.
   */
  boolean hasKey( int aKey );

  /**
   * Returns the value to which the specified key is mapped.
   *
   * @param aKey int - the key whose associated value is to be returned
   * @return &lt;E&gt; - the value to which the specified key is mapped
   * @throws TsItemNotFoundRtException specified key not found in map
   */
  default E getByKey( int aKey ) {
    return TsItemNotFoundRtException.checkNull( findByKey( aKey ) );
  }

  /**
   * Finds the value to which the specified key is mapped.
   *
   * @param aKey int - the key whose associated value is to be returned
   * @return &lt;E&gt; - the value to which the specified key is mapped or <code>null</code> if none found
   */
  E findByKey( int aKey );

  /**
   * Returns ordered list of all keys.
   * <p>
   * Each element of {@link #keys()} list has assosiated value with the same index in {@link #values()} list.
   * <p>
   * This methods always returns the same reference. However elements may change their positions during map
   * modifications.
   *
   * @return {@link IIntList}&lt;K&gt; - ordered list of all keys
   */
  @Override
  IIntList keys();

  @Override
  default boolean hasKey( Integer aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return hasKey( aKey.intValue() );
  }

  @Override
  default E findByKey( Integer aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return findByKey( aKey.intValue() );
  }

}

/**
 * Internal class for {@link IIntMap#EMPTY} singleton implementation.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
class InternalNullIntMap<E>
    extends ImmutableIntMap<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IIntMap#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IIntMap#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IIntMap.EMPTY;
  }

  // Object methods are implemented in parent class

}
