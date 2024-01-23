package org.toxsoft.core.tslib.utils;

import java.util.concurrent.*;

/**
 * Immutable, completed future.
 *
 * @param <T> the future result type of an asynchronous operation.
 * @author hazard157
 */
public class CompletedFuture<T>
    implements Future<T> {

  private final T result;

  /**
   * Constructor.
   *
   * @param aResult &lt;T&gt; - the result to be returned by {@link #get()}
   */
  public CompletedFuture( final T aResult ) {
    result = aResult;
  }

  // ------------------------------------------------------------------------------------
  // Future
  //

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return true;
  }

  @Override
  public T get() {
    return this.result;
  }

  @Override
  public T get( final long timeout, final TimeUnit unit ) {
    return this.result;
  }

  @Override
  public boolean cancel( final boolean mayInterruptIfRunning ) {
    return false;
  }

}
