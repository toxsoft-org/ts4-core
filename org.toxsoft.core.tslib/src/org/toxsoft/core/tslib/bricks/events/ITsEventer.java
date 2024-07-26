package org.toxsoft.core.tslib.bricks.events;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An interface to make it easier to work with listeners.
 *
 * @author hazard157
 * @param <L> - interface of the supported listener
 */
public interface ITsEventer<L>
    extends ITsPausabeEventsProducer {

  /**
   * Add the listener.
   * <p>
   * Already added listeners are ignored.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addListener( L aListener );

  /**
   * Removes the listener.
   * <p>
   * If listener was not added then method does nothing.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeListener( L aListener );

  /**
   * Temporary turns off (mutes) notification of the specified listener.
   * <p>
   * If listener is not registered or is already muted then method does nothing.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void muteListener( L aListener );

  /**
   * Turns off (un-mutes) previously muted listener.
   * <p>
   * If listener is not registered or is not muted then method does nothing.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void unmuteListener( L aListener );

  /**
   * Determines if listener is muted.
   * <p>
   * If listener is not registered returns <code>false</code>.
   *
   * @param aListener &lt;L&gt; - the listener
   * @return boolean - <code>true</code> if listener is muted
   */
  boolean isListenerMuted( L aListener );

}
