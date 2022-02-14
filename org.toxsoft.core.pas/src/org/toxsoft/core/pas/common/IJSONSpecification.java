package org.toxsoft.core.pas.common;

/**
 * Описание спецификации (константные определения)
 * <p>
 * Источник: https://www.jsonrpc.org/specification
 *
 * @author mvk
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface IJSONSpecification {

  /**
   * Версия спецификациии
   */
  String SPEC_VERSION_VALUE = "2.0";

  // ------------------------------------------------------------------------------------
  // Поля запросов, ответов
  //
  String SPEC_FIELD_JSONRPC = "jsonrpc";
  String SPEC_FIELD_ID = "id";
  String SPEC_FIELD_METHOD = "method";
  String SPEC_FIELD_PARAMS = "params";
  String SPEC_FIELD_RESULT = "result";
  String SPEC_FIELD_ERROR = "error";
  String SPEC_FIELD_ERROR_CODE = "code";
  String SPEC_FIELD_ERROR_MESSAGE = "message";
  String SPEC_FIELD_ERROR_DATA = "data";

  // ------------------------------------------------------------------------------------
  // Системные коды ошибок выполнения запросов
  //
  /**
   * Нарушение формата JSON сообщения
   * <p>
   * Invalid JSON was received by the server. An error occurred on the server while parsing the JSON text.
   */
  int JSON_ERROR_CODE_PARSE = -32700;

  /**
   * The JSON sent is not a valid Request object.
   */
  int JSON_ERROR_CODE_INVALID_REQUEST = -32600;

  /**
   * The method does not exist / is not available.
   */
  int JSON_ERROR_CODE_METHOD_NOT_FOUND = -32601;

  /**
   * Invalid method parameter(s).
   */
  int JSON_ERROR_CODE_INVALID_METHOD_PARAMS = -32602;

  /**
   * Неожиданная(необработанная должным образом) ошибка обработки запроса
   * <p>
   * Internal JSON-RPC error.
   */
  int JSON_ERROR_CODE_INTERNAL = -32603;

  /**
   * Reserved range for implementation-defined server-errors.
   */
  int JSON_ERROR_CODE_SERVER_MIN = -32000;
  int JSON_ERROR_CODE_SERVER_MAX = -32099;

}
