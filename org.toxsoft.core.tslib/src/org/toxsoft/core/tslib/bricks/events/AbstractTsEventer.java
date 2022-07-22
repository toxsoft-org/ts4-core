package org.toxsoft.core.tslib.bricks.events;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Base implementation for {@link ITsEventer}.
 *
 * @author hazard157
 * @param <L> - listener interface
 */
public abstract class AbstractTsEventer<L>
    extends AbstractTsPausabeEventsProducer
    implements ITsEventer<L> {

  private final IListEdit<L> listeners      = new ElemArrayList<>();
  private final IListEdit<L> mutedListeners = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public AbstractTsEventer() {
    // nop
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

}
