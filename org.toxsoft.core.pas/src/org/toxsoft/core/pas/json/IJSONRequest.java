package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;

/**
 * JSON(JavaScript Object Notation) запрос
 * <p>
 * Источник: https://www.jsonrpc.org/specification
 *
 * @author mvk
 */
public interface IJSONRequest
    extends IJSONMessage {

  /**
   * Возвращает имя вызываемого метода (запроса)
   *
   * @return String имя метода
   */
  String method();

  /**
   * Возвращает параметры вызываемого метода
   *
   * @return {@link IStringMap} карта параметров вызываемого метода.<br>
   *         Ключ: строковый идентификатор параметра;<br>
   *         Значение: значение параметра в формате {@link ITjValue}.
   */
  IStringMap<ITjValue> params();

  /**
   * Уникальный идентификатор запроса в рамках существующего канала.
   *
   * @return int идентификатор запроса
   */
  int id();

}
