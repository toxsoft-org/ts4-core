package org.toxsoft.tslib.coll.basis;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Mixin interface for collection wrapper implementations with thread-safe access.
 * <p>
 * This interface should be used for optimization when it is really needed or when developing reusable library code.
 * <p>
 * This interface exposes TsLib synchronized collections internals:
 * <ul>
 * <li>{@link #getSourceCollection()} - all synchronized collections in TsLib are implemented as wrappers over
 * non-sinchronized implementations;</li>
 * <li>{@link #getLockObject()} - all synchronized collections implementations hasown object used for access lock.</li>
 * </ul>
 * <p>
 * <b>WARNING:</b> be careful implementing syncronized collections! All methods (including <code>default</code> methods
 * from collection API interfaces) must be re-implemented in wrapper with access locking.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface ITsSynchronizedCollectionTag<E> {

  /**
   * Returns object used for access lock.
   *
   * @return {@link ReentrantReadWriteLock} - the lock object
   */
  ReentrantReadWriteLock getLockObject();

  /**
   * Returns source collection.
   *
   * @return {@link ITsCollection} - wrapped (source) non-sinchronized collection
   */
  ITsCollection<E> getSourceCollection();

}
