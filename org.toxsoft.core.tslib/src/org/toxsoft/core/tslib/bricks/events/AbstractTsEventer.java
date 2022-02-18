package org.toxsoft.core.tslib.bricks.events;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

//TRANSLATE

/**
 * Base implementation for {@link ITsEventer}.
 *
 * @author hazard157
 * @param <L> - listener interface
 */
public abstract class AbstractTsEventer<L>
    implements ITsEventer<L> {

  private final IListEdit<L> listeners      = new ElemArrayList<>();
  private final IListEdit<L> mutedListeners = new ElemArrayList<>();

  private int pauseCounter = 0;

  // ------------------------------------------------------------------------------------
  // ITsPausabeEventsProducer
  //

  @Override
  public void pauseFiring() {
    TsInternalErrorRtException.checkTrue( pauseCounter == Integer.MAX_VALUE );
    if( ++pauseCounter == 1 ) {
      doStartEventsAccrual();
    }
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    if( pauseCounter < 0 ) {
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
  public boolean isFiringPaused() {
    return pauseCounter > 0;
  }

  @Override
  public boolean isPendingEvents() {
    return doIsPendingEvents();
  }

  @Override
  public void resetPendingEvents() {
    doClearPendingEvents();
  }

  // ------------------------------------------------------------------------------------
  // IServiceEventsFiringSupport
  //

  @Override
  public void addListener( L aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeListener( L aListener ) {
    if( listeners.remove( aListener ) >= 0 ) {
      mutedListeners.remove( aListener );
    }
  }

  @Override
  public void muteListener( L aListener ) {
    if( listeners.hasElem( aListener ) ) {
      if( !mutedListeners.hasElem( aListener ) ) {
        mutedListeners.add( aListener );
      }
    }
  }

  @Override
  public void unmuteListener( L aListener ) {
    mutedListeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  protected IList<L> listeners() {
    if( listeners.isEmpty() ) {
      return IList.EMPTY;
    }
    int sz = listeners.size() - mutedListeners.size();
    if( sz <= 0 ) {
      return IList.EMPTY;
    }
    IListEdit<L> ll = new ElemArrayList<>( sz );
    for( L l : listeners ) {
      if( !mutedListeners.hasElem( l ) ) {
        ll.add( l );
      }
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Removes all listeners from the internal list.
   */
  public void clearListenersList() {
    listeners.clear();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may prepare to accumulating events.
   * <p>
   * In base class does nothing, there is no need to call superclass method in subclasses.
   */
  protected void doStartEventsAccrual() {
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
   * pending event. There no need to excplicitly clear pending events queue, superclass will call
   * {@link #doClearPendingEvents()} after this method.
   */
  protected abstract void doFirePendingEvents();

  /**
   * Subclass must clear pending events queue.
   */
  protected abstract void doClearPendingEvents();

}
