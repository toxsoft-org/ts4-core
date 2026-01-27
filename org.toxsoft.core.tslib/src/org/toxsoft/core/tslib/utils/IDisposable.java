package org.toxsoft.core.tslib.utils;

/**
 * Interface of entities that require the release of OS and other resources.
 * <p>
 * This interface refers to the objects created in GUI environment. Mostly this interface has same meaning as
 * <code>org.eclipse.ui.services.IDisposable</code> adding {@link #isDisposed()} method. Both interfaces may be
 * implemented by one type.
 *
 * @author hazard157
 */
public interface IDisposable {

  /**
   * Determines if previously allocated resources are released.
   *
   * @return boolean - a sign that OS resources have already been released<br>
   *         <b>true</b> - resources was released, this object is useless;<br>
   *         <b>false</b> - object is still holding allocated resources.
   */
  boolean isDisposed();

  /**
   * Releases resources previously allocated in the class.
   * <p>
   * Disposal of already disposed resources is harmless, however duplicate disposal is a kind or programming error so
   * the log message may be posted.
   */
  void dispose();

}
