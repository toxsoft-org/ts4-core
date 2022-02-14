package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.common.PasChannel;

/**
 * Обработчик ответов/результатов запросов {@link IJSONResult}
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public interface IJSONResultHandler<CHANNEL extends PasChannel> {

  /**
   * Обработать результаты обработки запроса.
   *
   * @param aChannel {@link PasChannel} канал по которому получена ошибка
   * @param aRequest {@link IJSONRequest} запрос обработка которого вызвала ошибку
   * @param aResult {@link IJSONResult} результаты обработки
   */
  void handle( CHANNEL aChannel, IJSONRequest aRequest, IJSONResult aResult );
}
