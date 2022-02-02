package org.toxsoft.core.tslib.coll.basis;

/**
 * Mixin interface of collections with sorted element order.
 * <p>
 * There is no sense to sort elemets in collections tagged with this interface, they are already sorted and keeped in
 * sorted state.
 * <p>
 * Usage of this interface is the optimization having sense in following cases:
 * <ul>
 * <li>when you really need high performance;</li>
 * <li>when you are writing reusable code (eg. libraries).</li>
 * </ul>
 *
 * @author hazard157
 */
public interface ITsSortedCollectionTag {

  // nop

}
