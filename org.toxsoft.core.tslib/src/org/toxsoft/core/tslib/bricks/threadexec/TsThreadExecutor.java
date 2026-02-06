package org.toxsoft.core.tslib.bricks.threadexec;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;

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

  private final TsSynchronizer synchronizer;

  /**
   * Default constructor.
   *
   * @param aName String synchronizer name
   * @param aLogger {@link ILogger} logger
   * @throws TsNullArgumentRtException any argument = <b>null</b>
   */
  public TsThreadExecutor( String aName, ILogger aLogger ) {
    synchronizer = new TsSynchronizer( aName, aLogger );
  }

  /**
   * Constructor with specify synchronized doJob thread.
   *
   * @param aName String synchronizer name
   * @param aDoJobThread {@link Thread} synchronized doJob thread
   * @param aLogger {@link ILogger} logger
   * @throws TsNullArgumentRtException any args = <b>null</b>.
   */
  public TsThreadExecutor( String aName, Thread aDoJobThread, ILogger aLogger ) {
    TsNullArgumentRtException.checkNulls( aDoJobThread, aLogger );
    synchronizer = new TsSynchronizer( aName, aDoJobThread, aLogger );
  }

  /**
   * Constructor with specify synchronized ExecutorService.
   *
   * @param aName String synchronizer name
   * @param aExecutor {@link Executor} executor of synchronized doJob thread
   * @param aLogger {@link ILogger} logger
   * @throws TsNullArgumentRtException any args = <b>null</b>.
   */
  public TsThreadExecutor( String aName, Executor aExecutor, ILogger aLogger ) {
    TsNullArgumentRtException.checkNulls( aExecutor, aLogger );
    synchronizer = new TsSynchronizer( aName, aExecutor, aLogger );
  }

  /**
   * Set a new executor for synchronizer.
   *
   * @param aExecutor {@link Executor} executor. null: set internal thread
   */
  public void setExecutor( Executor aExecutor ) {
    synchronizer.setExecutor( aExecutor );
  }

  /**
   * Set a new thread for synchronizer.
   *
   * @param aThread {@link Thread} the new doJob thread. null: set internal thread
   */
  public void setThread( Thread aThread ) {
    synchronizer.setThread( aThread );
  }

  /**
   * Set the maximum message queue size at which a warning is generated in the log.
   *
   * @param aQueueMax int queue size
   */
  public void setLoggerWarningQueueMax( int aQueueMax ) {
    synchronizer.setLoggerWarningQueueMax( aQueueMax );
  }

  /**
   * Set the maximum message queue size at which a error is generated in the log.
   *
   * @param aQueueMax int queue size
   */
  public void setLoggerErrorQueueMax( int aQueueMax ) {
    synchronizer.setLoggerErrorQueueMax( aQueueMax );
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

  @Override
  public void setLogger( ILogger aLogger ) {
    synchronizer.setLogger( aLogger );
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
