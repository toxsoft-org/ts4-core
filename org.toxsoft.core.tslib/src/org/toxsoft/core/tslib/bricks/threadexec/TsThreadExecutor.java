package org.toxsoft.core.tslib.bricks.threadexec;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation {@link ITsThreadExecutor} for server and console applications.
 * <p>
 * Common usage:
 * <ul>
 * <li>create instance {@link TsThreadExecutor} or ;</li>
 * <li>from the <b><i>user thread</i></b> periodically call {@link #doJob()} method;</li>
 * <li>after usage by calling {@link ICloseable#close() close()}.</li>
 * </ul>
 *
 * @author mvk
 */
public final class TsThreadExecutor
    implements ITsThreadExecutor, ICloseable {

  private TsSynchronizer synchronizer;

  /**
   * Default constructor.
   */
  public TsThreadExecutor() {
    synchronizer = new TsSynchronizer();
  }

  /**
   * Constructor with specify synchronized doJob thread.
   *
   * @param aDoJobThread {@link Thread} synchronized doJob thread
   * @throws TsNullArgumentRtException arg = null
   */
  public TsThreadExecutor( Thread aDoJobThread ) {
    TsNullArgumentRtException.checkNull( aDoJobThread );
    synchronizer = new TsSynchronizer( aDoJobThread );
  }

  /**
   * Constructor with specify synchronized ExecutorService.
   *
   * @param aExecutor {@link Executor} executor of synchronized doJob thread
   * @throws TsNullArgumentRtException arg = null
   */
  public TsThreadExecutor( Executor aExecutor ) {
    TsNullArgumentRtException.checkNull( aExecutor );
    synchronizer = new TsSynchronizer( aExecutor );
  }

  /**
   * Set new executor for synchronizer
   *
   * @param aExecutor {@link Executor} executor
   * @throws TsNullArgumentRtException arg = null
   */
  public void setExecutor( Executor aExecutor ) {
    TsNullArgumentRtException.checkNull( aExecutor );
    synchronizer.setExecutor( aExecutor );
  }

  /**
   * Set new thread for synchronizer
   *
   * @param aThread {@link Thread} thread
   * @throws TsNullArgumentRtException arg = null
   */
  public void setThread( Thread aThread ) {
    TsNullArgumentRtException.checkNull( aThread );
    synchronizer.setThread( aThread );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  /**
   * Free all resources and close synchronizer.
   */
  @Override
  public void close() {
    synchronizer.close();
  }

  // ------------------------------------------------------------------------------------
  // ITsThreadExecutor
  //

  @Override
  public Thread thread() {
    return synchronizer.thread();
  }

  @Override
  public void asyncExec( Runnable aRunnable ) {
    TsNullArgumentRtException.checkNull( aRunnable );
    synchronizer.asyncExec( aRunnable );
  }

  @Override
  public void syncExec( Runnable aRunnable ) {
    TsNullArgumentRtException.checkNull( aRunnable );
    synchronizer.syncExec( aRunnable );
  }

  @Override
  public void timerExec( int aMilliseconds, Runnable aRunnable ) {
    TsNullArgumentRtException.checkNull( aRunnable );
    synchronizer.timerExec( aMilliseconds, aRunnable );
  }

  // ------------------------------------------------------------------------------------
  // ICooperativeMultiTaskable
  //
  @Override
  public void doJob() {
    TsIllegalStateRtException.checkFalse( thread().equals( Thread.currentThread() ) );
    // aAll = true
    synchronizer.runAsyncMessages();
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
}
