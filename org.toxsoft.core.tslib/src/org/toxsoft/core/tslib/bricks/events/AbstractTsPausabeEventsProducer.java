package org.toxsoft.core.tslib.bricks.events;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsPausabeEventsProducer} abstract implementation.
 *
 * @author hazard157
 */
public abstract class AbstractTsPausabeEventsProducer
    implements ITsPausabeEventsProducer {

  private int pauseCounter = 0;

  /**
   * Constructor.
   */
  public AbstractTsPausabeEventsProducer() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ITsPausabeEventsProducer
  //

  @Override
  final public void pauseFiring() {
    TsInternalErrorRtException.checkTrue( pauseCounter == Integer.MAX_VALUE );
    if( ++pauseCounter == 1 ) {
      doStartEventsAccural();
    }
  }

  @Override
  final public void resumeFiring( boolean aFireDelayed ) {
    if( pauseCounter == 0 ) { // already fired or not even paused yet
      return;
    }
    --pauseCounter;
    if( pauseCounter == 0 ) {
      if( isPendingEvents() ) {
        try {
          if( aFireDelayed ) {
            doFirePendingEvents();
          }
        }
        finally {
          doClearPendingEvents();
        }
      }
    }
  }

  @Override
  final public boolean isFiringPaused() {
    return pauseCounter > 0;
  }

  @Override
  final public boolean isPendingEvents() {
    return doIsPendingEvents();
  }

  @Override
  final public void resetPendingEvents() {
    doClearPendingEvents();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may prepare to accumulate events when firing is became paused.
   * <p>
   * This method is called when {@link #pauseFiring()} is called when firing is not paused.
   * <p>
   * In base class does nothing, there is no need to call superclass method in subclasses.
   */
  protected void doStartEventsAccural() {
    // nop
  }

  /**
   * The subclass must determine if there is at least one pending event.
   *
   * @return boolean - <code>true</code> if there is at least one pending event to fire
   */
  protected abstract boolean doIsPendingEvents();

  /**
   * Subclass must fire all pending events.
   * <p>
   * This method is called from {@link #resumeFiring(boolean) resumeFiring(<b>true</b>)} only if there is at least one
   * pending event. There is no need to excplicitly clear pending events queue, superclass will call
   * {@link #doClearPendingEvents()} after this method.
   */
  protected abstract void doFirePendingEvents();

  /**
   * Subclass must clear pending events queue.
   */
  protected abstract void doClearPendingEvents();

}
