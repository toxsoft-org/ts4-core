package org.toxsoft.core.tslib.coll.basis;

/**
 * Base interface of all collections with size restricion capabilities.
 *
 * @author hazard157
 */
public interface ITsSizeRestrictableCollection {

  /**
   * Determines if collection size (number of elements) is restricted.
   * <p>
   * While implementing this interface means that collection size <b>may</b> be restricted, this method determines if
   * size is <b>really</b> restricted.
   *
   * @return boolean - size restriction flag<br>
   *         <b>true</b> - size is restricted, number of elements can not exceed {@link #maxSize()};<br>
   *         <b>false</b> - collection may contain any number of elements.
   */
  boolean isSizeRestricted();

  /**
   * Returns maximum allowed size of collection.
   * <p>
   * If {@link #isSizeRestricted()}=<b>false</b> then return value of this method has no sense.
   * <p>
   * If method returns 0, collection can not contain any element.
   *
   * @return int - non-negative integer
   */
  int maxSize();

}
