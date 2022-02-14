package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.tj.ITjValue;

/**
 * JSON(JavaScript Object Notation) ответ
 * <p>
 * Источник: https://www.jsonrpc.org/specification
 *
 * @author mvk
 */
public interface IJSONResult
    extends IJSONResponse {

  /**
   * Возвращает результат выполнения метода
   *
   * @return {@link ITjValue} результат выполнения метода.
   */
  ITjValue result();

  /**
   * Уникальный идентификатор запроса {@link IJSONRequest#id()} на который сформирован ответ
   *
   * @return int идентификатор запроса
   */
  int id();

}
