package org.toxsoft.core.tslib.coll.basis;

import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.utils.*;

/**
 * Mix-in interface for collection wrapper implementations with thread-safe access.
 * <p>
 * This interface should be used for optimization when it is really needed or when developing reusable library code.
 * <p>
 * This interface exposes TsLib synchronized collections internals:
 * <ul>
 * <li>{@link #getSourceCollection()} - all synchronized collections in TsLib are implemented as wrappers over
 * non-synchronized implementations;</li>
 * <li>{@link #getLockObject()} - all synchronized collections implementations has their own object used for access
 * lock.</li>
 * </ul>
 * <p>
 * <b>WARNING:</b> be careful implementing synchronized collections! All methods (including <code>default</code> methods
 * from collection API interfaces) must be re-implemented in wrapper with access locking.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface ITsSynchronizedCollectionWrapper<E>
    extends ITsSynchronizedWrapper {

  /**
   * Returns object used for access lock.
   *
   * @return {@link ReentrantReadWriteLock} - the lock object
   */
  @Override
  ReentrantReadWriteLock getLockObject();

  /**
   * Returns source collection.
   *
   * @return {@link ITsCollection} - wrapped (source) non-sinchronized collection
   */
  ITsCollection<E> getSourceCollection();

}
