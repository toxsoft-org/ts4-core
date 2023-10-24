package org.toxsoft.core.tsgui.ved.incub;

/**
 * Interface of entities that require the release of OS and other resources.
 *
 * @author hazard157
 */
public interface IDisposable {

  /**
   * Determines whether previously allocated resources are released.
   *
   * @return boolean - a sign that OS resources have already been released
   */
  boolean isDisposed();

  /**
   * Releases OS resources previously allocated in the class.
   * <p>
   * Disposal of already disposed resources is harmless, however duplicate disposal is an error so the log message must
   * be posted.
   */
  void dispose();

}
