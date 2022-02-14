package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.common.PasChannel;

/**
 * Обработчик ошибок обработки запросов {@link IJSONError}
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public interface IJSONErrorHandler<CHANNEL extends PasChannel> {

  /**
   * Обработать ошибку обработки запроса.
   *
   * @param aChannel {@link PasChannel} канал по которому получена ошибка
   * @param aRequest {@link IJSONRequest} запрос обработка которого вызвала ошибку
   * @param aError {@link IJSONError} ошибка обработки
   */
  void handle( CHANNEL aChannel, IJSONRequest aRequest, IJSONError aError );
}
