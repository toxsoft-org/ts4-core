package org.toxsoft.core.tslib.bricks.threadexec;

import static org.toxsoft.core.tslib.bricks.threadexec.TsThreadExecutorUtils.*;

import java.util.*;
import java.util.concurrent.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;

/**
 * Developed based on org.eclipse.swt.widgets.Synchronizer
 *
 * @author mvk
 */
final class TsSynchronizer {

  private static final String METHOD_ASYNC_EXEC         = "asyncExec";        //$NON-NLS-1$
  private static final String METHOD_SYNC_EXEC          = "syncExec";         //$NON-NLS-1$
  private static final String METHOD_TIMER_EXEC         = "timerExec";        //$NON-NLS-1$
  private static final String METHOD_RUN_ASYNC_MESSAGES = "runAsyncMessages"; //$NON-NLS-1$

  /**
   * Время (мсек) ожидания завершения синхронного запроса после которого выдается предупреждение в журнал о длительном
   * выполнении.
   */
  private static final long LONG_SYNC_WAIT_TIMEOUT = 10000;

  private final int           instanceId;
  private static volatile int instanceCounter = 0;
  private final String        name;

  private Object          startLock       = new Object();
  private Object          finishLock      = new Object();
  private Object          doJobThreadLock = new Object();
  private volatile Thread doJobThread;
  private boolean         isInternalThread;

  private ElemLinkedList<TsRunnableLock> messages    = new ElemLinkedList<>();
  private Object                         messageLock = new Object();

  private static final Timer timer = new Timer( "TsSynchronizerTimerSingleton", true ); //$NON-NLS-1$

  private boolean queryShutdown;
  private ILogger logger;

  /**
   * Constructs a new instance of this class.
   *
   * @param aName String synchronizer name
   * @throws TsNullArgumentRtException any argument = <b>null</b>
   */
  TsSynchronizer( String aName, ILogger aLogger ) {
    TsNullArgumentRtException.checkNulls( aName, aLogger );
    instanceId = ++instanceCounter;
    name = aName;
    setLogger( aLogger );
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
    TsNullArgumentRtException.checkNulls( aName, aDoJobThread, aLogger );
    instanceId = ++instanceCounter;
    name = aName;
    setLogger( aLogger );
    isInternalThread = false;
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
    TsNullArgumentRtException.checkNulls( aName, aExecutor, aLogger );
    instanceId = ++instanceCounter;
    name = aName;
    setLogger( aLogger );
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
    synchronized (doJobThreadLock) {
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
  }

  /**
   * Set(change) internal thread.
   *
   * @param aThread {@link Thread} the new doJob thread. null: set internal thread
   */
  void setThread( Thread aThread ) {
    synchronized (doJobThreadLock) {
      shutdownInternalThread();
      if( aThread == null ) {
        createInternalThread();
        return;
      }
      isInternalThread = false;
      queryShutdown = false;
      doJobThread = aThread;
    }
  }

  /**
   * Set a new logger for synchronizer.
   *
   * @param aLogger {@link ILogger} the new logger.
   * @throws TsNullArgumentRtException arg = null
   */
  void setLogger( ILogger aLogger ) {
    TsNullArgumentRtException.checkNull( aLogger );
    logger = aLogger;
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
   * @param aRunnable code to run on the user-interface thread.
   * @see #syncExec
   */
  @SuppressWarnings( "nls" )
  void asyncExec( Runnable aRunnable ) {
    TsRunnableLock lock = new TsRunnableLock( aRunnable, 0, logger );
    debug( this, lock, METHOD_ASYNC_EXEC, "is created" );
    addLast( lock );
    debug( this, lock, METHOD_ASYNC_EXEC, "is registered" );
  }

  /**
   * Causes the <code>run()</code> method of the runnable to be invoked by the user-interface thread at the next
   * reasonable opportunity. The thread which calls this method is suspended until the runnable completes.
   *
   * @param aRunnable code to run on the user-interface thread.
   * @see #asyncExec
   */
  @SuppressWarnings( "nls" )
  void syncExec( Runnable aRunnable ) {
    Thread currentThread = Thread.currentThread();
    synchronized (doJobThreadLock) {
      if( doJobThread == currentThread ) {
        try {
          aRunnable.run();
        }
        catch( RuntimeException | Error error ) {
          logger.error( error );
        }
        return;
      }
    }

    TsRunnableLock lock = new TsRunnableLock( aRunnable, 0, logger );
    lock.thread = currentThread;
    debug( this, lock, METHOD_SYNC_EXEC, "is created" );

    synchronized (lock) {
      addLast( lock );
      debug( this, lock, METHOD_SYNC_EXEC, "is registered" );
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
          debug( this, lock, METHOD_SYNC_EXEC, "is DONE" );
          break;
        }
        if( !lock.done() ) {
          debug( this, lock, METHOD_SYNC_EXEC, "IS NOT done" );
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
    debug( this, lock, METHOD_TIMER_EXEC, "is created" );
    addLast( lock );
    debug( this, lock, METHOD_TIMER_EXEC, "is registered" );
    timer.schedule( new InternalTimerTask(), aMilliseconds );
    debug( this, lock, METHOD_TIMER_EXEC, "is scheduled" );
  }

  @SuppressWarnings( "nls" )
  void runAsyncMessages() {
    do {
      TsRunnableLock lock = findNextLock();
      if( lock == null ) {
        break;
      }
      debug( this, lock, METHOD_RUN_ASYNC_MESSAGES, "is found" );
      synchronized (lock) {
        try {
          // syncThread = lock.thread;
          try {
            debug( this, lock, METHOD_RUN_ASYNC_MESSAGES, "run BEFORE" );
            lock.run();
          }
          catch( Throwable t ) {
            lock.throwable = t;
          }
        }
        finally {
          debug( this, lock, METHOD_RUN_ASYNC_MESSAGES, "lock.notify() BEFORE" );
          // syncThread = null;
          lock.notify();
          debug( this, lock, METHOD_RUN_ASYNC_MESSAGES, "lock.notify() AFTER" );
        }
      }
    } while( true );
    return;
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
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

  private static String threadName( TsSynchronizer aSynchronize ) {
    return TsSynchronizer.class.getSimpleName() + Integer.valueOf( aSynchronize.instanceId ) + '@'
        + InternalDoJobTask.class.getSimpleName() + '(' + aSynchronize.name + ')';
  }

  private static void debug( TsSynchronizer aSynchronizer, TsRunnableLock aLock, String aMethod, String aText ) {
    ILogger logger = aSynchronizer.logger;
    if( !logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
      return;
    }
    String threadName = TsSynchronizer.class.getSimpleName() + Integer.valueOf( aSynchronizer.instanceId ) + '('
        + aSynchronizer.name + ')';
    Long id = Long.valueOf( aLock.id );
    Long time = Long.valueOf( System.currentTimeMillis() - aLock.timestamp );
    switch( aMethod ) {
      case METHOD_ASYNC_EXEC:
      case METHOD_SYNC_EXEC:
      case METHOD_TIMER_EXEC: {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement elem = (stack[4].toString().contains( "SkThreadExecutorService" ) ? stack[5] : stack[4]); //$NON-NLS-1$
        String source = getSource( elem.toString() );
        logger.debug( "[%s] %s(...): exec(#%d) %s, source = %s, time = %d(msec)", threadName, aMethod, id, aText, //$NON-NLS-1$
            source, time );
        break;
      }
      default: {
        logger.debug( "[%s] %s(...): exec(#%d) %s, time = %d(msec)", threadName, aMethod, id, aText, time ); //$NON-NLS-1$
        break;
      }
    }
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

  private static String getSource( String aStackItem ) {
    @SuppressWarnings( "nls" )
    String items[] = aStackItem.split( "\\." );
    int length = items.length;
    return items[length - 3] + '.' + items[length - 2] + '.' + items[length - 1];
  }

  public static void main( String[] aArgs ) {
    String t =
        "deployment.skat-backend-deploy.jar//org.toxsoft.uskat.s5.server.backend.addons.S5AbstractBackend.run(S5AbstractBackend.java:443)"; //$NON-NLS-1$
    System.out.println( getSource( t ) );
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

        logger.info( "InternalDoJobTask()run(...): thread %s is started!", threadName( TsSynchronizer.this ) );

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
