package org.toxsoft.core.tslib.coll.basis;

import org.toxsoft.core.tslib.coll.IList;

/**
 * Mixin interface of linear collections (lists) with fast indexed access.
 * <p>
 * There are two ways to access elements of collection: sequentally access using iterator {@link #iterator()} and random
 * acces using {@link #get(int)} method. For collections marked with this interface the preferred way in indexed access.
 * Iterators are created using <code>new</code> operator, so indexed access eliminates memory allocation.
 * <p>
 * Usage of this interface is the optimization having sense in following cases:
 * <ul>
 * <li>when you really need high performance;</li>
 * <li>when you are writing reusable code (eg. libraries).</li>
 * </ul>
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface ITsFastIndexListTag<E>
    extends IList<E> {

  // nop

}
