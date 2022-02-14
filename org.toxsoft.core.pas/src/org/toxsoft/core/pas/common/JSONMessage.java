package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.IJSONSpecification.*;
import static org.toxsoft.core.pas.tj.impl.TjUtils.*;

import org.toxsoft.core.pas.json.EJSONKind;
import org.toxsoft.core.pas.json.IJSONMessage;
import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация JSON ошибки обработки запроса
 *
 * @author mvk
 */
class JSONMessage
    implements IJSONMessage {

  private final EJSONKind kind;
  private final ITjObject value;

  /**
   * Конструктор нового сообщения
   *
   * @param aMessageKind {@link EJSONKind} вид сообщения
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат сообщения
   */
  protected JSONMessage( EJSONKind aMessageKind ) {
    kind = TsNullArgumentRtException.checkNull( aMessageKind );
    value = createTjObject();
    value.fields().put( SPEC_FIELD_JSONRPC, createString( SPEC_VERSION_VALUE ) );
  }

  /**
   * Конструктор полученного сообщения
   *
   * @param aMessageKind {@link EJSONKind} вид сообщения
   * @param aValue {@link ITjObject} запрос в формате {@link ITjObject}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат сообщения
   */
  protected JSONMessage( EJSONKind aMessageKind, ITjObject aValue ) {
    kind = TsNullArgumentRtException.checkNull( aMessageKind );
    value = TsNullArgumentRtException.checkNull( aValue );
    ITjValue jsonrpc = aValue.fields().findByKey( SPEC_FIELD_JSONRPC );
    TsIllegalArgumentRtException.checkFalse( jsonrpc != null );
    TsIllegalArgumentRtException.checkFalse( jsonrpc != null && jsonrpc.isString() );
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников и пакета
  //
  /**
   * Возвращает объект хранения сообщения
   *
   * @return {@link ITjObject} объект хранения
   */
  protected final ITjObject value() {
    return value;
  }

  // ------------------------------------------------------------------------------------
  // Реализация IJSONMessage
  //
  @Override
  public String jsonrpc() {
    return value.fields().getByKey( SPEC_FIELD_JSONRPC ).asString();
  }

  @Override
  public EJSONKind kind() {
    return kind;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //
  @Override
  public String toString() {
    return value.toString();
  }

}
