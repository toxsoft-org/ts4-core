package org.toxsoft.core.tslib.bricks.events;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

//TRANSLATE

/**
 * Интерфейс поддержки работы со слушателями.
 * <p>
 * Интерфейс публикует поддержку слушателей для клиентов служб, а реализациям служб следует расширить
 * {@link AbstractTsEventer} и воспользоваться дополнительным API класса.
 *
 * @author hazard157
 * @param <L> - интерфейс слушателя службы
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
   * Temorary truns off (mutes) notification of the specified listener.
   * <p>
   * If listener is not registered or is already muted then method does nothing.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void muteListener( L aListener );

  /**
   * Truns off (unmutes) previously muted listener.
   * <p>
   * If listener is not registered or is not muted then method does nothing.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void unmuteListener( L aListener );

}
