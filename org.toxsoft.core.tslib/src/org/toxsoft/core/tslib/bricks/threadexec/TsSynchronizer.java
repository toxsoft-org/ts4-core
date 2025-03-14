package org.toxsoft.core.tslib.bricks.threadexec;

import static org.toxsoft.core.tslib.bricks.threadexec.TsThreadExecutorUtils.*;

import java.util.*;
import java.util.concurrent.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;

/**
 * Source: org.eclipse.swt.widgets.Synchronizer
 *
 * @author mvk
 */
final class TsSynchronizer {

  /**
   * Время (мсек) ожидания и проверки завершения синхронного запроса.
   */
  private static final long LONG_SYNC_WAIT_TIMEOUT = 10000;

  private final int           instanceId;
  private static volatile int instanceCounter = 0;
  private final String        name;

  private Object          startLock  = new Object();
  private Object          finishLock = new Object();
  private volatile Thread doJobThread;
  private boolean         isInternalThread;

  private ElemLinkedList<TsRunnableLock> messages    = new ElemLinkedList<>();
  private Object                         messageLock = new Object();

  private static Timer timer = new Timer( "TsSynchronizer", true ); //$NON-NLS-1$

  private boolean       queryShutdown;
  private final ILogger logger;

  /**
   * Constructs a new instance of this class.
   *
   * @param aName String synchronizer name
   * @throws TsNullArgumentRtException any argument = <b>null</b>
   */
  TsSynchronizer( String aName, ILogger aLogger ) {
    instanceId = ++instanceCounter;
    name = TsNullArgumentRtException.checkNull( aName );
    logger = TsNullArgumentRtException.checkNull( aLogger );
    createInternalThread();
  }

  /**
   * Constructs a new instance of this class.
   *
   * @param aName String synchronizer name
   * @param aDoJobThread {@link Thread} synchronized doJob thread
   * @throws TsNullArgumentRtException any argument = <b>null</b>
   */
  TsSynchronizer( String aName, Thread aDoJobThread, ILogger aLogger ) {
    instanceId = ++instanceCounter;
    name = TsNullArgumentRtException.checkNull( aName );
    logger = TsNullArgumentRtException.checkNull( aLogger );
    isInternalThread = false;
    TsNullArgumentRtException.checkNull( aDoJobThread );
    doJobThread = aDoJobThread;
  }

  /**
   * Constructs a new instance of this class.
   *
   * @param aName String synchronizer name
   * @param aExecutor {@link Executor} executor of synchronized doJob thread
   * @throws TsNullArgumentRtException any argument = <b>null</b>
   */
  TsSynchronizer( String aName, Executor aExecutor, ILogger aLogger ) {
    instanceId = ++instanceCounter;
    name = TsNullArgumentRtException.checkNull( aName );
    logger = TsNullArgumentRtException.checkNull( aLogger );
    TsNullArgumentRtException.checkNull( aExecutor );
    isInternalThread = true;
    synchronized (startLock) {
      aExecutor.execute( new InternalDoJobTask() );
      try {
        // Wait thread start and setup doJobThread
        startLock.wait();
      }
      catch( InterruptedException ex ) {
        logger.error( ex );
      }
    }
  }

  /**
   * Set(change) internal executor.
   *
   * @param aExecutor {@link Executor} executor of synchronized doJob thread. null: set internal thread
   */
  void setExecutor( Executor aExecutor ) {
    TsNullArgumentRtException.checkNull( aExecutor );
    shutdownInternalThread();
    if( aExecutor == null ) {
      createInternalThread();
      return;
    }
    isInternalThread = true;
    queryShutdown = false;
    synchronized (startLock) {
      aExecutor.execute( new InternalDoJobTask() );
      try {
        // Wait thread start and setup doJobThread
        startLock.wait();
      }
      catch( InterruptedException ex ) {
        logger.error( ex );
      }
    }
  }

  /**
   * Set(change) internal thread.
   *
   * @param aThread {@link Thread} the new doJob thread. null: set internal thread
   */
  void setThread( Thread aThread ) {
    shutdownInternalThread();
    if( aThread == null ) {
      createInternalThread();
      return;
    }
    isInternalThread = false;
    queryShutdown = false;
    doJobThread = aThread;
  }

  private void createInternalThread() {
    isInternalThread = true;
    Thread thread = new Thread( new InternalDoJobTask() );
    synchronized (startLock) {
      thread.start();
      try {
        // Wait thread start and setup doJobThread
        startLock.wait();
      }
      catch( InterruptedException ex ) {
        logger.error( ex );
      }
    }
  }

  /**
   * Shutdown internal thread if exist
   */
  private void shutdownInternalThread() {
    synchronized (messageLock) {
      if( !isInternalThread ) {
        return;
      }
      queryShutdown = true;
      messageLock.notify();
    }
    synchronized (finishLock) {
      if( doJobThread != null ) {
        try {
          finishLock.wait();
        }
        catch( InterruptedException ex ) {
          logger.error( ex );
        }
      }
    }
  }

  /**
   * Free all resources and close synchronizer.
   */
  void close() {
    if( isInternalThread ) {
      queryShutdown = true;
      Thread t = doJobThread;
      if( t != null ) {
        t.interrupt();
      }
    }
  }

  /**
   * Return synchronized doJob thread
   *
   * @return {@link Thread} synchronized doJob thread
   */
  Thread thread() {
    return doJobThread;
  }

  /**
   * Causes the <code>run()</code> method of the runnable to be invoked by the user-interface thread at the next
   * reasonable opportunity. The caller of this method continues to run in parallel, and is not notified when the
   * runnable has completed.
   *
   * @param runnable code to run on the user-interface thread.
   * @see #syncExec
   */
  // @SuppressWarnings( "nls" )
  void asyncExec( Runnable runnable ) {
    TsRunnableLock lock = new TsRunnableLock( runnable, 0, logger );
    addLast( lock );
    // if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
    // String from = Thread.currentThread().getStackTrace()[4].toString();
    // Long id = Long.valueOf( lock.id );
    // logger.debug( "asyncExec(...): exec(#%d) is registred from: %s", id, from );
    // }
  }

  /**
   * Causes the <code>run()</code> method of the runnable to be invoked by the user-interface thread at the next
   * reasonable opportunity. The thread which calls this method is suspended until the runnable completes.
   *
   * @param runnable code to run on the user-interface thread.
   * @see #asyncExec
   */
  @SuppressWarnings( "nls" )
  void syncExec( Runnable runnable ) {
    Thread currentThread = Thread.currentThread();
    // synchronized (messageLock) {
    if( doJobThread == currentThread ) {
      try {
        runnable.run();
      }
      catch( RuntimeException | Error error ) {
        logger.error( error );
      }
      return;
    }
    // }

    TsRunnableLock lock = new TsRunnableLock( runnable, 0, logger );
    lock.thread = currentThread;

    synchronized (lock) {
      addLast( lock );
      if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
        String from = currentThread.getStackTrace()[4].toString();
        Long id = Long.valueOf( lock.id );
        logger.debug( "syncExec(...): exec(#%d) is registred from: %s", id, from );
      }

      boolean interrupted = false;
      while( !lock.done() ) {
        try {
          lock.wait();
        }
        catch( InterruptedException e ) {
          interrupted = true;
          logger.error( e );
          break;
        }
        if( lock.done() ) {
          if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
            String from = currentThread.getStackTrace()[3].toString();
            Long id = Long.valueOf( lock.id );
            logger.debug( "syncExec(...): exec(#%d) is DONE from: %s", id, from );
          }
          break;
        }
        if( !lock.done() ) {
          if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
            String from = currentThread.getStackTrace()[3].toString();
            Long id = Long.valueOf( lock.id );
            logger.debug( "syncExec(...): exec(#%d) IS NOT done from: %s", id, from );
          }
        }
        if( !lock.done() && System.currentTimeMillis() - lock.timestamp > LONG_SYNC_WAIT_TIMEOUT ) {
          longExecToLog( lock, currentThread, logger );
        }
      }
      if( interrupted ) {
        currentThread.interrupt();
      }
      if( lock.throwable != null ) {
        logger.error( lock.throwable );
      }
    }
  }

  /**
   * Causes the <code>run()</code> method of the runnable to be invoked by the user-interface thread after the specified
   * number of milliseconds have elapsed. If milliseconds is less than zero, the runnable is not executed.
   *
   * @param aMilliseconds the delay before running the runnable
   * @param aRunnable code to run on the user-interface thread
   */
  @SuppressWarnings( "nls" )
  void timerExec( int aMilliseconds, Runnable aRunnable ) {
    if( aMilliseconds < 0 ) {
      return;
    }
    TsRunnableLock lock = new TsRunnableLock( aRunnable, aMilliseconds, logger );

    if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
      String from = Thread.currentThread().getStackTrace()[4].toString();
      Long id = Long.valueOf( lock.id );
      logger.debug( "timerExec(...): timer(#%d) call addLast(...)", id, from );
    }

    addLast( lock );

    if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
      String from = Thread.currentThread().getStackTrace()[4].toString();
      Long id = Long.valueOf( lock.id );
      logger.debug( "timerExec(...): timer(#%d) call schedule(...)", id, from );
    }

    timer.schedule( new InternalTimerTask(), aMilliseconds );
  }

  @SuppressWarnings( "nls" )
  void runAsyncMessages() {
    do {
      TsRunnableLock lock = findNextLock();
      if( lock == null ) {
        break;
      }
      if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
        Long id = Long.valueOf( lock.id );
        logger.debug( "runAsyncMessages(...): exec(#%d) is starting.", id );
      }
      synchronized (lock) {
        try {
          // syncThread = lock.thread;
          try {
            if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
              Long id = Long.valueOf( lock.id );
              logger.debug( "runAsyncMessages(...): exec(#%d) before run.", id );
            }
            lock.run();
          }
          catch( Throwable t ) {
            lock.throwable = t;
          }
        }
        finally {
          if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
            Long id = Long.valueOf( lock.id );
            Long time = Long.valueOf( System.currentTimeMillis() - lock.timestamp );
            logger.debug( "runAsyncMessages(...): exec(#%d) before lock.notify(). time = %d(msec).", id, time );
          }
          // syncThread = null;
          lock.notify();
          if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
            Long id = Long.valueOf( lock.id );
            Long time = Long.valueOf( System.currentTimeMillis() - lock.timestamp );
            logger.debug( "runAsyncMessages(...): exec(#%d) after lock.notify(). time = %d(msec).", id, time );
          }
        }
      }
    } while( true );
    return;
  }

  private TsRunnableLock findNextLock() {
    synchronized (messageLock) {
      if( messages.size() == 0 ) {
        return null;
      }
      long currTime = System.currentTimeMillis();
      IListEdit<TsRunnableLock> notReadyLocks = null;
      try {
        do {
          TsRunnableLock lock = removeFirst();
          if( lock == null ) {
            return null;
          }
          if( lock.timestamp > currTime ) {
            // lock is not ready yet
            if( notReadyLocks == null ) {
              notReadyLocks = new ElemLinkedList<>();
            }
            // first-input-last-output order
            notReadyLocks.insert( 0, lock );
            continue;
          }
          return lock;
        } while( true );
      }
      finally {
        if( notReadyLocks != null ) {
          toBack( notReadyLocks );
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
  private void addLast( TsRunnableLock aLock ) {
    synchronized (messageLock) {
      messages.add( aLock );
      // resume dojob thread
      messageLock.notify();
    }
  }

  private void toBack( IList<TsRunnableLock> aLocks ) {
    for( TsRunnableLock lock : aLocks ) {
      // messages.addFirst( lock );
      messages.insert( 0, lock );
    }
  }

  private TsRunnableLock removeFirst() {
    // return messages.poll();
    if( messages.size() == 0 ) {
      return null;
    }
    return messages.removeByIndex( 0 );
  }

  private static void longExecToLog( TsRunnableLock aLock, Thread aThread, ILogger aLogger ) {
    Long id = Long.valueOf( aLock.id );
    Long time = Long.valueOf( System.currentTimeMillis() - aLock.timestamp );
    String from = aThread.getStackTrace()[5].toString();
    if( aLogger.isSeverityOn( ELogSeverity.INFO ) ) {
      aLogger.warning( "syncExec(..): long exec(#%d) is detected. time = %d(msec). From: '%s'. Stack trace:\n%s", id, //$NON-NLS-1$
          time, from, threadStackToString( aThread ) );
      return;
    }
    aLogger.warning(
        "syncExec(..): long exec(#%d) is detected. time = %d(msec). From: '%s'. Сhange LogLevel => TRACE to details", //$NON-NLS-1$
        id, time, from );
  }

  private static String threadName( TsSynchronizer aSynchronize ) {
    return TsSynchronizer.class.getSimpleName() + Integer.valueOf( aSynchronize.instanceId ) + '@'
        + InternalDoJobTask.class.getSimpleName() + '(' + aSynchronize.name + ')';
  }

  // ------------------------------------------------------------------------------------
  // private classes
  //

  private class InternalDoJobTask
      implements Runnable {

    @SuppressWarnings( "nls" )
    @Override
    public void run() {
      try {
        doJobThread = Thread.currentThread();
        doJobThread.setName( threadName( TsSynchronizer.this ) );

        logger.info( "InternalDoJobTask()run(...): thread %s is started!", Thread.currentThread().getName() );

        synchronized (startLock) {
          // Thread start notification
          startLock.notify();
        }
        while( !queryShutdown ) {
          // processing calls
          runAsyncMessages();
          synchronized (messageLock) {
            if( queryShutdown ) {
              break;
            }
            // check next call existence
            TsRunnableLock lock = findNextLock();
            if( lock != null ) {
              // rollback
              // messages.addFirst( lock );
              messages.insert( 0, lock );
              continue;
            }
            try {
              // suspend dojob thread - wait new calls
              messageLock.wait();
            }
            catch( @SuppressWarnings( "unused" ) Throwable e ) {
              // clear interrupted flag
              Thread.interrupted();
              // logger.error( e );
            }
          }
        }
        synchronized (finishLock) {
          doJobThread = null;
          // Thread finish notification
          finishLock.notify();
        }
        logger.info( "InternalDoJobTask()run(...): thread %s is finished!", Thread.currentThread().getName() );
      }
      catch( Throwable t ) {
        logger.error( t );
      }
    }
  }

  private class InternalTimerTask
      extends TimerTask {

    @Override
    public void run() {
      if( !queryShutdown ) {
        synchronized (messageLock) {
          // resume dojob thread
          messageLock.notify();
        }
      }
    }
  }
}
