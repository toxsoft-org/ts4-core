package org.toxsoft.core.pas.json;

/**
 * Сообщение JSON-ответ на запрос передаваемое по протоколу JSON(JavaScript Object Notation) может содержать результаты
 * обработки {@link IJSONResult} или ошибку {@link IJSONError} выполнения запроса {@link IJSONRequest}.
 * <p>
 * Источник: https://www.jsonrpc.org/specification
 *
 * @author mvk
 */
public interface IJSONResponse
    extends IJSONMessage {
  // nop
}
