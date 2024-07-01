package org.toxsoft.core.tslib.coll.synch;

import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Thread-safe wrapper over a basic editable list.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class SynchronizedListBasicEdit<E>
    extends SynchronizedListBase<E, IListBasicEdit<E>> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   *
   * @param aSource &lt;L&gt; - the source collection
   * @param aLock {@link ReentrantReadWriteLock} - thread safety lock
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedListBasicEdit( IListBasicEdit<E> aSource, ReentrantReadWriteLock aLock ) {
    super( aSource, aLock );
  }

  /**
   * Constructor.
   * <p>
   * Internally creates the new instance of {@link ReentrantReadWriteLock}.
   *
   * @param aSource &lt;L&gt; - the source collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SynchronizedListBasicEdit( IListBasicEdit<E> aSource ) {
    super( aSource );
  }

}
