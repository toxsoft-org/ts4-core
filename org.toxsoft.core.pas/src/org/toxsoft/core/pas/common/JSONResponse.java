package org.toxsoft.core.pas.common;

import org.toxsoft.core.pas.json.EJSONKind;
import org.toxsoft.core.pas.json.IJSONResponse;
import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация JSON ошибки обработки запроса
 *
 * @author mvk
 */
public class JSONResponse
    extends JSONMessage
    implements IJSONResponse {

  /**
   * Конструктор нового сообщения
   *
   * @param aMessageKind {@link EJSONKind} вид сообщения
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат сообщения
   */
  protected JSONResponse( EJSONKind aMessageKind ) {
    super( aMessageKind );
  }

  /**
   * Конструктор полученной ошибки
   *
   * @param aMessageKind {@link EJSONKind} вид сообщения
   * @param aValue {@link ITjObject} запрос в формате {@link ITjObject}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат сообщения
   */
  protected JSONResponse( EJSONKind aMessageKind, ITjObject aValue ) {
    super( aMessageKind, aValue );
  }
}
