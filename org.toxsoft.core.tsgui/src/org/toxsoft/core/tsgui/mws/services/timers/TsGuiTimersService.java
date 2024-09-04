package org.toxsoft.core.tsgui.mws.services.timers;

import static org.toxsoft.core.tsgui.mws.services.timers.ITsGuiTimersServiceConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link ITsGuiTimersService} implementation.
 *
 * @author hazard157
 */
public final class TsGuiTimersService
    implements ITsGuiTimersService, ICloseable {

  private static final String THREAD_NAME_QUICK_TIMER = "TsGuiTimerService.QuickTimerThread"; //$NON-NLS-1$
  private static final String THREAD_NAME_SLOW_TIMER  = "TsGuiTimerService.SlowTimerThread";  //$NON-NLS-1$

  private static final IntRange  GRANULARITY_RATIO_RANGE = new IntRange( 2, 100 );
  private static final LongRange QUICK_PERIOD_RANGE      = new LongRange( 4, 299 );
  private static final LongRange SLOW_PERIOD_RANGE       = new LongRange( 300, 10_000 );
  private static final LongRange SLEEP_PERIOD_RANGE      = new LongRange( 1, 300 );

  /**
   * Timer service eventer implementation.
   *
   * @author hazard157
   */
  static class Eventer
      extends AbstractTsEventer<IRealTimeSensitive> {

    long lastTime = 0;

    @Override
    protected boolean doIsPendingEvents() {
      return lastTime > 0;
    }

    @Override
    protected void doFirePendingEvents() {
      reallyFire( lastTime );
    }

    @Override
    protected void doClearPendingEvents() {
      lastTime = 0L;
    }

    private void reallyFire( long aTime ) {
      for( IRealTimeSensitive l : listeners() ) {
        l.whenRealTimePassed( aTime );
      }
    }

    void fireEvent( long aTime ) {
      if( isPendingEvents() ) {
        lastTime = aTime;
        return;
      }
      reallyFire( aTime );
    }

  }

  /**
   * Internal implementation of threads for {@link ITsGuiTimersService} timers.
   *
   * @author hazard157
   */
  class TsTimerServiceThread
      extends Thread {

    final long     period;
    final long     sleepPeriod;
    final Runnable runnable;

    long lastRunFinishedAtTime = 0L;

    TsTimerServiceThread( String aName, long aPeriod, long aSleepPeriod, Runnable aRunnable ) {
      super( aName );
      period = aPeriod;
      sleepPeriod = aSleepPeriod;
      runnable = aRunnable;
    }

    @Override
    public void run() {
      try {
        while( !interrupted() ) {
          long time = System.currentTimeMillis();
          while( time - lastRunFinishedAtTime < period ) {
            sleep( sleepPeriod );
            time = System.currentTimeMillis();
          }
          try {
            display.syncExec( runnable );
          }
          catch( Exception ex ) {
            LoggerUtils.errorLogger().error( ex );
          }
          lastRunFinishedAtTime = System.currentTimeMillis();
        }
      }
      catch( InterruptedException ex ) {
        if( !closeQueried ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

  }

  private final Eventer quickEventer;
  private final Eventer slowEventer;

  private final ITsGuiContext        tsContext;
  private final Display              display;
  private final TsTimerServiceThread threadQuick;
  private final TsTimerServiceThread threadSlow;

  private boolean closeQueried = false;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsGuiTimersService( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    display = tsContext.get( Display.class );
    quickEventer = new Eventer();
    slowEventer = new Eventer();
    int gratio = GRANULARITY_RATIO_RANGE.inRange( OPDEF_GRANULARITY_DIVIDER.getValue( tsContext.params() ).asInt() );
    // quick
    long quickPer = QUICK_PERIOD_RANGE.inRange( OPDEF_QUICK_TIMER_PERIOD.getValue( tsContext.params() ).asLong() );
    long quickSleepPeriod = SLEEP_PERIOD_RANGE.inRange( quickPer / gratio );
    threadQuick = new TsTimerServiceThread( THREAD_NAME_QUICK_TIMER, quickPer, quickSleepPeriod, //
        () -> quickEventer.fireEvent( System.currentTimeMillis() ) //
    );
    // slow
    long slowPer = SLOW_PERIOD_RANGE.inRange( OPDEF_SLOW_TIMER_PERIOD.getValue( tsContext.params() ).asLong() );
    long slowSleepPeriod = SLEEP_PERIOD_RANGE.inRange( slowPer / gratio );
    threadSlow = new TsTimerServiceThread( THREAD_NAME_SLOW_TIMER, slowPer, slowSleepPeriod, //
        () -> slowEventer.fireEvent( System.currentTimeMillis() ) //
    );
    threadSlow.start();
    threadQuick.start();
  }

  // ------------------------------------------------------------------------------------
  // ITsTimersService
  //

  @Override
  public long getQuickTimerPeriod() {
    return threadQuick.period;
  }

  @Override
  public long getSlowTimerPeriod() {
    return threadSlow.period;
  }

  @Override
  public ITsEventer<IRealTimeSensitive> quickTimers() {
    return quickEventer;
  }

  @Override
  public ITsEventer<IRealTimeSensitive> slowTimers() {
    return slowEventer;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    closeQueried = true;
    threadSlow.interrupt();
    threadQuick.interrupt();
  }

}
