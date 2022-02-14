package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.tj.ITjValue;

/**
 * JSON(JavaScript Object Notation) ошибка обработки запроса {@link IJSONRequest}
 * <p>
 * Источник: https://www.jsonrpc.org/specification
 *
 * @author mvk
 */
public interface IJSONError
    extends IJSONResponse {

  /**
   * Возвращает код ошибки обработки запроса
   *
   * @return int код ошибки
   */
  int code();

  /**
   * Возвращает дополнительные данные об ошибке
   *
   * @return {@link ITjValue} дополнительные данные. null: неопределяются
   */
  ITjValue data();

  /**
   * Возвращает строку предоставляющую суть ошибки
   *
   * @return String текстовое описание ошибки
   */
  String message();

  /**
   * Уникальный идентификатор запроса {@link IJSONRequest#id()} на который сформирован ответ
   *
   * @return Integer идентификатор запроса. null: неопределенно (ошибка формата запроса)
   */
  Integer id();

}
