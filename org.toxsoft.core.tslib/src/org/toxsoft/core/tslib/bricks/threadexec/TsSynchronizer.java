package org.toxsoft.core.tslib.bricks.threadexec;

import java.util.*;
import java.util.concurrent.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Source: org.eclipse.swt.widgets.Synchronizer
 *
 * @author mvk
 */
final class TsSynchronizer {

  private Object                                startLock   = new Object();
  private Object                                finishLock  = new Object();
  private volatile Thread                       doJobThread;
  private boolean                               isInternalThread;
  private ConcurrentLinkedDeque<TsRunnableLock> messages    = new ConcurrentLinkedDeque<>();
  private Object                                messageLock = new Object();

  private static Timer timer = new Timer( "TsSynchronizer", true ); //$NON-NLS-1$

  private boolean queryShutdown;

  /**
   * Constructs a new instance of this class.
   */
  TsSynchronizer() {
    isInternalThread = true;
    Thread thread = new Thread( new InternalDoJobTask() );
    synchronized (startLock) {
      thread.start();
      try {
        // Wait thread start and setup doJobThread
        startLock.wait();
      }
      catch( InterruptedException ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Constructs a new instance of this class.
   *
   * @param aDoJobThread {@link Thread} synchronized doJob thread
   * @throws TsNullArgumentRtException argument = null
   */
  TsSynchronizer( Thread aDoJobThread ) {
    isInternalThread = false;
    TsNullArgumentRtException.checkNull( aDoJobThread );
    doJobThread = aDoJobThread;
  }

  /**
   * Constructs a new instance of this class.
   *
   * @param aExecutor {@link Executor} executor of synchronized doJob thread
   * @throws TsNullArgumentRtException argument = null
   */
  TsSynchronizer( Executor aExecutor ) {
    TsNullArgumentRtException.checkNull( aExecutor );
    isInternalThread = true;
    synchronized (startLock) {
      aExecutor.execute( new InternalDoJobTask() );
      try {
        // Wait thread start and setup doJobThread
        startLock.wait();
      }
      catch( InterruptedException ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Set(change) internal executor.
   *
   * @param aExecutor {@link Executor} executor of synchronized doJob thread
   * @throws TsNullArgumentRtException argument = null
   */
  void setExecutor( Executor aExecutor ) {
    TsNullArgumentRtException.checkNull( aExecutor );
    shutdownInternalThread();
    isInternalThread = true;
    queryShutdown = false;
    synchronized (startLock) {
      aExecutor.execute( new InternalDoJobTask() );
      try {
        // Wait thread start and setup doJobThread
        startLock.wait();
      }
      catch( InterruptedException ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Set(change) internal thread.
   *
   * @param aThread {@link Thread} the new doJob thread
   * @throws TsNullArgumentRtException argument = null
   */
  void setThread( Thread aThread ) {
    TsNullArgumentRtException.checkNull( aThread );
    shutdownInternalThread();
    isInternalThread = false;
    queryShutdown = false;
    doJobThread = aThread;
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
      messageLock.notifyAll();
    }
    synchronized (finishLock) {
      if( doJobThread != null ) {
        try {
          finishLock.wait();
        }
        catch( InterruptedException ex ) {
          LoggerUtils.errorLogger().error( ex );
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
  void asyncExec( Runnable runnable ) {
    addLast( new TsRunnableLock( runnable, 0 ) );
  }

  /**
   * Causes the <code>run()</code> method of the runnable to be invoked by the user-interface thread at the next
   * reasonable opportunity. The thread which calls this method is suspended until the runnable completes.
   *
   * @param runnable code to run on the user-interface thread.
   * @see #asyncExec
   */
  void syncExec( Runnable runnable ) {
    Thread currentThread = Thread.currentThread();
    synchronized (messageLock) {
      if( doJobThread == currentThread ) {
        try {
          runnable.run();
        }
        catch( RuntimeException | Error error ) {
          LoggerUtils.defaultLogger().error( error );
        }
        return;
      }
    }
    TsRunnableLock lock = new TsRunnableLock( runnable, 0 );
    /*
     * Only remember the syncThread for syncExec.
     */
    synchronized (lock) {
      lock.thread = currentThread;
      addLast( lock );
      boolean interrupted = false;
      while( !lock.done() ) {
        try {
          lock.wait();
        }
        catch( InterruptedException e ) {
          interrupted = true;
          LoggerUtils.defaultLogger().error( e );
        }
      }
      if( interrupted ) {
        Thread.currentThread().interrupt();
      }
      if( lock.throwable != null ) {
        LoggerUtils.defaultLogger().error( lock.throwable );
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
  void timerExec( int aMilliseconds, Runnable aRunnable ) {
    if( aMilliseconds < 0 ) {
      return;
    }
    addLast( new TsRunnableLock( aRunnable, aMilliseconds ) );
    timer.schedule( new InternalTimerTask(), aMilliseconds );
  }

  void runAsyncMessages() {
    do {
      TsRunnableLock lock = findNextLock();
      if( lock == null ) {
        break;
      }
      synchronized (lock) {
        // syncThread = lock.thread;
        try {
          lock.run();
        }
        catch( Throwable t ) {
          lock.throwable = t;
        }
        finally {
          // syncThread = null;
          lock.notifyAll();
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
      messageLock.notifyAll();
    }
  }

  private void toBack( IList<TsRunnableLock> aLocks ) {
    for( TsRunnableLock lock : aLocks ) {
      messages.addFirst( lock );
    }
  }

  private TsRunnableLock removeFirst() {
    return messages.poll();
  }

  // ------------------------------------------------------------------------------------
  // private classes
  //

  private class InternalDoJobTask
      implements Runnable {

    @Override
    public void run() {
      doJobThread = Thread.currentThread();
      synchronized (startLock) {
        // Thread start notification
        startLock.notifyAll();
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
            messages.addFirst( lock );
            break;
          }
          try {
            // suspend dojob thread - wait new calls
            messageLock.wait();
          }
          catch( @SuppressWarnings( "unused" ) Throwable e ) {
            // clear interrupted flag
            Thread.interrupted();
            // LoggerUtils.errorLogger().error( e );
          }
        }
      }
      synchronized (finishLock) {
        doJobThread = null;
        // Thread finish notification
        finishLock.notifyAll();
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
          messageLock.notifyAll();
        }
      }
    }
  }
}
