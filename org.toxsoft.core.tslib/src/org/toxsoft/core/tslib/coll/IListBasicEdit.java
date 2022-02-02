package org.toxsoft.core.tslib.coll;

import org.toxsoft.core.tslib.coll.basis.ITsCollectionEdit;

/**
 * Editable extension of {@link IList}.
 * <p>
 * This interface adds basic editing methods to {@link IList}. Editing methods is applicable to any kind of linear
 * collections, including the sorted collections.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface IListBasicEdit<E>
    extends IList<E>, ITsCollectionEdit<E> {

  // nop

}
