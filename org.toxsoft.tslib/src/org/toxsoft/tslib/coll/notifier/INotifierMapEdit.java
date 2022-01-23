package org.toxsoft.tslib.coll.notifier;

import org.toxsoft.tslib.coll.IMapEdit;

// TRANSLATE

/**
 * An editable extension of the {@link INotifierMap}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public interface INotifierMapEdit<K, E>
    extends INotifierMap<K, E>, IMapEdit<K, E> {

  // nop

}
