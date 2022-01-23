package org.toxsoft.tslib.bricks.strid.coll.notifier;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListBasicEdit;

/**
 * An {@link IStridablesListBasicEdit} extension with the ability to be a data model.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public interface INotifierStridablesListBasicEdit<E extends IStridable>
    extends IStridablesListBasicEdit<E>, INotifierStridablesList<E> {

  // nop

}
