package org.toxsoft.core.tslib.bricks.events.msg;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGtMessageEventer} simple implementation.
 * <p>
 * Simple implementation simply remembers messages while {@link #isFiringPaused()} and then sends them one by one in the
 * order of appearance.
 * <p>
 * Note: it is prohibited to call {@link #addListener(IGtMessageListener)} or
 * {@link #removeListener(IGtMessageListener)} methods from the registered listeners
 * {@link IGtMessageListener#onGenericTopicMessage(GtMessage)} method.
 *
 * @author hazard157
 */
public class GtMessageEventer
    extends AbstractTsPausabeEventsProducer
    implements IGtMessageEventer {

  private final IMapEdit<IGtMessageListener, ITsFilter<String>> listenersMap   = new ElemMap<>();
  private final IListEdit<IGtMessageListener>                   mutedListeners = new ElemArrayList<>();

  /**
   * Messages remembered while eventer was paused.
   */
  private final IListEdit<GtMessage> msgsList = new ElemLinkedBundleList<>();

  /**
   * Constructor.
   */
  public GtMessageEventer() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void reallySend( GtMessage aMsg ) {
    if( listenersMap.isEmpty() ) {
      return;
    }
    for( IGtMessageListener l : listenersMap.keys() ) {
      ITsFilter<String> topicIdFilter = listenersMap.getByKey( l );
      if( topicIdFilter.accept( aMsg.topicId() ) ) {
        l.onGenericTopicMessage( aMsg );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsEventer
  //

  @Override
  public void addListener( IGtMessageListener aListener ) {
    listenersMap.removeByKey( aListener );
    listenersMap.put( aListener, ITsFilter.ALL );
  }

  @Override
  public void removeListener( IGtMessageListener aListener ) {
    listenersMap.removeByKey( aListener );
  }

  @Override
  public void muteListener( IGtMessageListener aListener ) {
    if( listenersMap.hasKey( aListener ) ) {
      if( !mutedListeners.hasElem( aListener ) ) {
        mutedListeners.add( aListener );
      }
    }
  }

  @Override
  public void unmuteListener( IGtMessageListener aListener ) {
    mutedListeners.remove( aListener );
  }

  @Override
  public boolean isListenerMuted( IGtMessageListener aListener ) {
    return mutedListeners.hasElem( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IGtMessageEventer
  //

  @Override
  public void setListener( IGtMessageListener aListener, ITsFilter<String> aTopicIdMatcher ) {
    listenersMap.put( aListener, aTopicIdMatcher );
    mutedListeners.remove( aListener );
  }

  @Override
  protected boolean doIsPendingEvents() {
    return !msgsList.isEmpty();
  }

  @Override
  protected void doFirePendingEvents() {
    for( GtMessage msg : msgsList ) {
      reallySend( msg );
    }
  }

  @Override
  protected void doClearPendingEvents() {
    msgsList.clear();
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Send the message respecting {@link #isFiringPaused()} state.
   *
   * @param aMessage {@link GtMessage} - the message to send to the listeners
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void sendMessage( GtMessage aMessage ) {
    TsNullArgumentRtException.checkNull( aMessage );
    if( isFiringPaused() ) {
      msgsList.add( aMessage );
    }
    else {
      reallySend( aMessage );
    }
  }

}
