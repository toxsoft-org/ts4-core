package org.toxsoft.core.tslib.bricks.strid.coll.notifier;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.coll.notifier.INotifierList;
import org.toxsoft.core.tslib.coll.notifier.INotifierStringMap;

/**
 * An {@link IStridablesList} extension with the ability to be a data model.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public interface INotifierStridablesList<E extends IStridable>
    extends IStridablesList<E>, INotifierList<E>, INotifierStringMap<E> {

  // nop

}
