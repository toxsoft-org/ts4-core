package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;

/**
 * JSON(JavaScript Object Notation) уведомление (не требующее ответа)
 * <p>
 * Источник: https://www.jsonrpc.org/specification
 *
 * @author mvk
 */
public interface IJSONNotification
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
}
