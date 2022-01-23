package org.toxsoft.tslib.bricks.strid.coll;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.coll.IListEdit;

/**
 * Editable list (and map) of {@link IStridable} elements.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public interface IStridablesListEdit<E extends IStridable>
    extends IStridablesListBasicEdit<E>, IListEdit<E> {

  // nop

}
