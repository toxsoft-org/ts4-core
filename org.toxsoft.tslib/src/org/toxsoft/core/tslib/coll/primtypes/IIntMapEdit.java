package org.toxsoft.core.tslib.coll.primtypes;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * An editable collection that maps <code>int</code> keys to values.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public interface IIntMapEdit<E>
    extends IIntMap<E>, IMapEdit<Integer, E> {

  /**
   * Associates the specified value with the specified key in this map (puts key-value pair in this map).
   * <p>
   * If the map previously contained a mapping for the key, the old value is replaced by the specified value.
   *
   * @param aKey int - key with which the specified value is to be associated
   * @param aElem &lt;E&gt; - value to be associated with the specified key
   * @return &lt;E&gt; - the previous value associated with key, or <code>null</code> if there was no mapping
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  E put( int aKey, E aElem );

  /**
   * Removes the mapping for a key from this map if it is present.
   *
   * @param aKey int - key whose mapping is to be removed from the map
   * @return &lt;E&gt; - the previous value associated with key, or <code>null</code> if there was no mapping
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  E removeByKey( int aKey );

  // ------------------------------------------------------------------------------------
  // Convinience methods with default implementations
  //

  /**
   * Copies all of the mappings from the specified map to this map.
   * <p>
   * The effect of this call is equivalent to that of calling {@link #put(Object, Object)} on this map once for each
   * mapping from the specified map.
   *
   * @param aSrc {@link IIntMap} - mappings to be stored in this map
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void putAll( IIntMap<? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    IIntList srcKeys = aSrc.keys();
    IList<? extends E> srcValues = aSrc.values();
    for( int i = 0, n = srcKeys.size(); i < n; i++ ) {
      put( srcKeys.get( i ), srcValues.get( i ) );
    }
  }

  /**
   * Replaces all of the mappings from the specified map to this map.
   * <p>
   * The effect of this call is equivalent to that of calling {@link #clear()} and then {@link #putAll(IMap)}.
   *
   * @param aSrc {@link IIntMap} - mappings to be stored in this map
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( IIntMap<? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    clear();
    putAll( aSrc );
  }

}
