package org.toxsoft.core.tslib.coll.notifier;

import org.toxsoft.core.tslib.coll.primtypes.IStringMap;

/**
 * A {@link String}-keyed map with ability to be a data model.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public interface INotifierStringMap<E>
    extends IStringMap<E>, INotifierMap<String, E> {

  // nop

}
