package org.toxsoft.tslib.coll.notifier;

import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;

/**
 * Editable extension of {@link INotifierStringMap}.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public interface INotifierStringMapEdit<E>
    extends INotifierStringMap<E>, IStringMapEdit<E> {

  // nop

}
