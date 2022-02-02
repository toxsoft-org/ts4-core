package org.toxsoft.core.tslib.bricks.events;

import org.toxsoft.core.tslib.utils.ITsPausabeEventsProducer;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

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
   * Замораживает слушатель.
   * <p>
   * Замороженный слушатель перестает вызываться при генерации событий. Размораживание слушателя происходит методом
   * {@link #unmuteListener(Object)}.
   * <p>
   * Если слушатель уже заморожен, метод ничего не делает.
   *
   * @param aListener &lt;L&gt; - слушатель
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void muteListener( L aListener );

  /**
   * Размораживает слушатель, замороженный методом {@link #muteListener(Object)}.
   * <p>
   * Если слушатель не заморожен, метод ничего не делает.
   *
   * @param aListener &lt;L&gt; - слушатель
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void unmuteListener( L aListener );

}
