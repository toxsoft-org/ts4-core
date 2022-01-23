package org.toxsoft.tslib.bricks.strid.coll.notifier;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.coll.notifier.INotifierList;
import org.toxsoft.tslib.coll.notifier.INotifierStringMap;

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
