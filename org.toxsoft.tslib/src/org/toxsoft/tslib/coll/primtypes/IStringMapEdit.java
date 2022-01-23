package org.toxsoft.tslib.coll.primtypes;

import org.toxsoft.tslib.coll.IMap;
import org.toxsoft.tslib.coll.IMapEdit;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * An editable collection that maps {@link String} keys to values.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public interface IStringMapEdit<E>
    extends IStringMap<E>, IMapEdit<String, E> {

  /**
   * Copies all of the mappings from the specified map to this map.
   * <p>
   * The effect of this call is equivalent to that of calling {@link #put(Object, Object)} on this map once for each
   * mapping from the specified map.
   *
   * @param aSrc {@link IStringMap}&lt;E&gt; - mappings to be stored in this map
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void putAll( IStringMap<? extends E> aSrc ) {
    IMapEdit.super.putAll( aSrc );
  }

  /**
   * Replaces all of the mappings from the specified map to this map.
   * <p>
   * The effect of this call is equivalent to that of calling {@link #clear()} and then {@link #putAll(IMap)}.
   *
   * @param aSrc {@link IStringMap}&lt; E&gt; - mappings to be stored in this map
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default void setAll( IStringMap<? extends E> aSrc ) {
    IMapEdit.super.setAll( aSrc );
  }

}
