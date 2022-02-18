package org.toxsoft.core.tslib.utils;

import java.util.concurrent.locks.*;

/**
 * Mixin interface for wrapper implementations over some source with thread-safe access.
 * <p>
 * <b>WARNING:</b> be careful implementing syncronized wreppers! All methods (including <code>default</code> methods
 * from API interfaces) must be re-implemented in wrapper with access locking.
 *
 * @author hazard157
 */
public interface ITsSynchronizedWrapper {

  /**
   * Returns object used for access lock.
   *
   * @return {@link ReentrantReadWriteLock} - the lock object
   */
  ReentrantReadWriteLock getLockObject();

}
