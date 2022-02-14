package org.toxsoft.core.pas.common;

import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Канал передачи данных
 *
 * @author mvk
 */
public interface IPasTxChannel {

  /**
   * Отправляет запрос на выполнение удаленной стороне
   *
   * @param aMethod String имя запроса
   * @param aParams {@link IStringMap} карта параметров вызываемого метода.<br>
   *          Ключ: строковый идентификатор параметра;<br>
   *          Значение: значение параметра в формате {@link ITjValue}.
   * @throws TsNullArgumentRtException аргумент = null
   */
  void sendRequest( String aMethod, IStringMap<ITjValue> aParams );

  /**
   * Отправляет уведомление (не требующего ответа) удаленной стороне
   *
   * @param aMethod String имя уведомления
   * @param aParams {@link IStringMap} карта параметров уведомления.<br>
   *          Ключ: строковый идентификатор параметра;<br>
   *          Значение: значение параметра в формате {@link ITjValue}.
   * @throws TsNullArgumentRtException аргумент = null
   */
  void sendNotification( String aMethod, IStringMap<ITjValue> aParams );

  /**
   * Отправляет ошибку сформированную на канале которая не связана с обработкой запроса.
   *
   * @param aErrorCode int код ошибки
   * @param aErrorMessage String текст ошибки
   * @param aErrorData ITjValue дополнительные данные об ошибке. null: данные не определяются.
   * @throws TsNullArgumentRtException любой(кроме aErrorData) аргумент = null
   */
  void sendError( int aErrorCode, String aErrorMessage, ITjValue aErrorData );

}
