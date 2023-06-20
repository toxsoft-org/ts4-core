package org.toxsoft.core.tslib.coll.basis;

/**
 * Mixin interface for collections with known number of elements in it.
 * <p>
 * This interface is implemented by all tslib collections.
 * <p>
 * Note that tslib collections do not allow more than {@link Integer#MAX_VALUE} elements.
 *
 * @author hazard157
 */
public interface ITsCountableCollection {

  /**
   * Determines if collection is empty (contains no elements).
   *
   * @return boolean - empty collection flag<br>
   *         <b>true</b> - collection is empty, so {@link #size()}=0 ;<br>
   *         <b>false</b> - collection has at list one item, so {@link #size()} >= 1.
   */
  default boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns the number of elements in this collection.
   *
   * @return int - number of elements
   */
  int size();

}
