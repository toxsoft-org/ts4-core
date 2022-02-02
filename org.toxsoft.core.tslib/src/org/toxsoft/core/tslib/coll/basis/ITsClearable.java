package org.toxsoft.core.tslib.coll.basis;

/**
 * Mixin interface for collections allowing clear (remove all items) operation.
 * <p>
 * This interface is implemented by all editable tslib collections.
 *
 * @author hazard157
 */
public interface ITsClearable {

  /**
   * Clears collection (removes all items).
   */
  void clear();

}
