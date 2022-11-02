package org.toxsoft.core.tsgui.ved.incub;

/**
 * Mixin interface of visual entities that require the release of OS resources.
 *
 * @author hazard157
 */
public interface ITsDisposable {

  /**
   * Determines if resources is relead or not allocated.
   *
   * @return boolean - <code>true</code> if resources is not allocated
   */
  boolean isDisposed();

  /**
   * Releases OS resources previously allocated in the class.
   * <p>
   * Method does nothing if resources were already released or not even allocated.
   */
  void dispose();

}
