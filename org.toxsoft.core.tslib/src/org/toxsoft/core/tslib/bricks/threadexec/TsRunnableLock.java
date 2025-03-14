package org.toxsoft.core.tslib.bricks.threadexec;

import org.toxsoft.core.tslib.utils.logs.*;

/**
 * Source: org.eclipse.swt.widgets.RunnableLock
 *
 * @author mvk
 */
class TsRunnableLock {

  static long           getId = 0;
  final long            id;
  final long            timestamp;
  volatile Runnable     runnable;
  volatile Thread       thread;
  volatile Throwable    throwable;
  private final ILogger logger;

  TsRunnableLock( Runnable aRunnable, int aDelay, ILogger aLogger ) {
    synchronized (TsRunnableLock.class) {
      id = ++getId;
    }
    runnable = aRunnable;
    timestamp = System.currentTimeMillis() + aDelay;
    logger = aLogger;
  }

  boolean done() {
    return runnable == null || throwable != null;
  }

  void run() {
    if( runnable != null ) {
      try {
        runnable.run();
      }
      catch( Throwable t ) {
        throwable = t;
        logger.error( t );
      }
    }
    runnable = null;
  }
}
