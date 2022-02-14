package org.toxsoft.core.pas.common;

import org.toxsoft.core.pas.json.IJSONMessage;

/**
 * Слушатель канала {@link PasChannel}
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public interface IPasChannelHandler<CHANNEL extends PasChannel> {

  /**
   * Событие: запуск канала
   *
   * @param aSource {@link PasChannel} канал, источник сообщения
   */
  default void onStart( CHANNEL aSource ) {
    // nop
  }

  /**
   * Событие: завершение работы канала
   * <p>
   * В момент вызова {@link #onShutdown(PasChannel)} прием или передача данных по каналу невозможна
   *
   * @param aSource {@link PasChannel} канал, источник сообщения
   */
  default void onShutdown( CHANNEL aSource ) {
    // nop
  }

  /**
   * Событие: по каналу получено JSON сообщение
   *
   * @param aSource {@link PasChannel} канал по которому получено сообщение
   * @param aMessage {@link IJSONMessage} полученное JSON сообщение
   */
  default void onReceive( CHANNEL aSource, IJSONMessage aMessage ) {
    // nop
  }

  /**
   * Событие: по каналу отправлено JSON сообщение
   *
   * @param aSource {@link PasChannel} канал по которому передано сообщение
   * @param aMessage {@link IJSONMessage} отправленное сообщение
   */
  default void onSend( CHANNEL aSource, IJSONMessage aMessage ) {
    // nop
  }

  /**
   * Событие: при отправке сообщения произошла ошибка
   *
   * @param aSource {@link PasChannel} канал по которому отправляется сообщение
   * @param aMessage {@link IJSONMessage} сообщение при передачи которого произошла ошибка
   * @param aError {@link Throwable} ошибка
   */
  default void onSendError( CHANNEL aSource, IJSONMessage aMessage, Throwable aError ) {
    // nop
  }

  /**
   * Событие: при приеме сообщения произошла ошибка
   *
   * @param aSource {@link PasChannel} канал, источник сообщения
   * @param aError {@link Throwable} ошибка
   */
  default void onReceiveError( CHANNEL aSource, Throwable aError ) {
    // nop
  }

}
