package org.toxsoft.core.tslib.coll.notifier;

import org.toxsoft.core.tslib.coll.IListBasicEdit;
import org.toxsoft.core.tslib.coll.IListEdit;

/**
 * Editable extension of {@link INotifierList}.
 * <p>
 * Extends {@link IListBasicEdit} with editing methods applicable only to unsorted collections.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface INotifierListEdit<E>
    extends IListEdit<E>, INotifierListBasicEdit<E> {

  // nop

}
