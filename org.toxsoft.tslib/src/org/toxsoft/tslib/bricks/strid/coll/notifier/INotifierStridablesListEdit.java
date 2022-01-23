package org.toxsoft.tslib.bricks.strid.coll.notifier;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;

/**
 * An {@link IStridablesListEdit} extension with the ability to be a data model.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public interface INotifierStridablesListEdit<E extends IStridable>
    extends IStridablesListEdit<E>, INotifierStridablesListBasicEdit<E> {

  // nop

}
