package org.toxsoft.tslib.coll;

import org.toxsoft.tslib.coll.basis.ITsClearable;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * An editable extension of the {@link IMap} collection that maps keys to values.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public interface IMapEdit<K, E>
    extends IMap<K, E>, ITsClearable {

  /**
   * Associates the specified value with the specified key in this map (puts key-value pair in this map).
   * <p>
   * If the map previously contained a mapping for the key, the old value is replaced by the specified value.
   *
   * @param aKey &lt;K&gt; - key with which the specified value is to be associated
   * @param aElem &lt;E&gt; - value to be associated with the specified key
   * @return &lt;E&gt; - the previous value associated with key, or <code>null</code> if there was no mapping
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  E put( K aKey, E aElem );

  /**
   * Removes the mapping for a key from this map if it is present.
   *
   * @param aKey &lt;K&gt; - key whose mapping is to be removed from the map
   * @return &lt;E&gt; - the previous value associated with key, or <code>null</code> if there was no mapping
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  E removeByKey( K aKey );

  // ------------------------------------------------------------------------------------
  // Convinience methods with default implementations
  //

  /**
   * Copies all of the mappings from the specified map to this map.
   * <p>
   * The effect of this call is equivalent to that of calling {@link #put(Object, Object)} on this map once for each
   * mapping from the specified map.
   *
   * @param aSrc {@link IMap}&lt;K, E&gt; - mappings to be stored in this map
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void putAll( IMap<K, ? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    IList<K> srcIds = aSrc.keys();
    IList<? extends E> srcValues = aSrc.values();
    for( int i = 0, n = srcIds.size(); i < n; i++ ) {
      put( srcIds.get( i ), srcValues.get( i ) );
    }
  }

  /**
   * Replaces all of the mappings from the specified map to this map.
   * <p>
   * The effect of this call is equivalent to that of calling {@link #clear()} and then {@link #putAll(IMap)}.
   *
   * @param aSrc {@link IMap}&lt;K, E&gt; - mappings to be stored in this map
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( IMap<K, ? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    clear();
    putAll( aSrc );
  }

}
