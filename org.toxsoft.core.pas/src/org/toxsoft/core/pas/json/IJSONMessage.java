package org.toxsoft.core.pas.json;

/**
 * Сообщение передаваемое по протоколу JSON(JavaScript Object Notation)
 * <p>
 * Источник: https://www.jsonrpc.org/specification
 *
 * @author mvk
 */
public interface IJSONMessage {

  /**
   * Возвращает версию протокола
   *
   * @return String версия протокола
   */
  String jsonrpc();

  /**
   * Возвращает тип сообщения
   *
   * @return {@link EJSONKind} тип сообщения
   */
  EJSONKind kind();
}
