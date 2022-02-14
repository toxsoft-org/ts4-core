package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.common.*;

/**
 * Обработчик запросов {@link IJSONRequest}
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public interface IJSONRequestHandler<CHANNEL extends PasChannel> {

  /**
   * Обработать запрос.
   * <p>
   * Реализация метода должна определить обработку запроса и сформировать на него ответ (результат или ошибку).
   * <p>
   * При формировании новых кодов ошибок следует учитывать уже существующие:
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_PARSE} - ошибка формата;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INVALID_METHOD_PARAMS} - неверно заданы параметры вызова метода;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INTERNAL} - неожиданная (не обработанная должным образом)
   * ошибка.</li>
   *
   * @param aChannel {@link PasChannel} канал по которому поступил запрос
   * @param aRequest {@link IJSONRequest} принятый запрос
   * @return JSONResponse ответ(результат или ошибка) обработки запроса
   */
  JSONResponse execute( CHANNEL aChannel, IJSONRequest aRequest );
}
