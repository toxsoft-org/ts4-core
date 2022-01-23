package org.toxsoft.tslib.coll.notifier;

import org.toxsoft.tslib.coll.IListBasicEdit;

/**
 * Editable extension of {@link INotifierList}.
 * <p>
 * This interface adds basic editing methods to {@link INotifierList} from the {@link IListBasicEdit}. Editing methods
 * is applicable to any kind of linear collections, including the sorted collections.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface INotifierListBasicEdit<E>
    extends IListBasicEdit<E>, INotifierList<E> {

  // nop

}
