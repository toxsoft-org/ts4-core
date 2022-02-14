package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.IJSONSpecification.*;
import static org.toxsoft.core.pas.common.ITsResources.*;
import static org.toxsoft.core.pas.tj.impl.TjUtils.*;

import org.toxsoft.core.pas.json.EJSONKind;
import org.toxsoft.core.pas.json.IJSONRequest;
import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация JSON запроса
 * <p>
 * Реализация скрыта на уровне пакета в котором находится базовый канал обмена {@link PasChannel}. Предполагается, что
 * пользователи для создания запросов будут использовать метод {@link PasChannel#sendRequest(String, IStringMap)}
 *
 * @author mvk
 */
final class JSONRequest
    extends JSONMessage
    implements IJSONRequest {

  /**
   * Конструктор нового запроса
   *
   * @param aID int идентификатор запроса уникальный в рамках сессии обмена данными
   * @param aMethod String имя запроса
   * @param aParams {@link IStringMap} карта параметров вызываемого метода.<br>
   *          Ключ: строковый идентификатор параметра;<br>
   *          Значение: значение параметра в формате {@link ITjValue}.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат запроса
   */
  JSONRequest( int aID, String aMethod, IStringMap<ITjValue> aParams ) {
    super( EJSONKind.REQUEST );
    TsNullArgumentRtException.checkNull( aMethod );
    TsNullArgumentRtException.checkNull( aParams );
    ITjObject value = value();
    value.fields().put( SPEC_FIELD_METHOD, createString( aMethod ) );
    // По спецификации JSON поле "params" может отсутствовать в запросе
    if( aParams.keys().size() > 0 ) {
      value.fields().put( SPEC_FIELD_PARAMS, createObject( createTjObject() ) );
      value.fields().getByKey( SPEC_FIELD_PARAMS ).asObject().fields().putAll( aParams );
    }
    value.fields().put( SPEC_FIELD_ID, createNumber( aID ) );
  }

  /**
   * Конструктор полученного запроса
   *
   * @param aValue {@link ITjObject} запрос в формате {@link ITjObject}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат запроса
   */
  JSONRequest( ITjObject aValue ) {
    super( EJSONKind.REQUEST, aValue );
    ITjObject value = value();
    ITjValue method = value.fields().findByKey( SPEC_FIELD_METHOD );
    ITjValue params = value.fields().findByKey( SPEC_FIELD_PARAMS );
    ITjValue id = value.fields().findByKey( SPEC_FIELD_ID );
    TsIllegalArgumentRtException.checkFalse( method != null );
    TsIllegalArgumentRtException.checkFalse( method != null && method.isString() );
    // По спецификации JSON поле "params" может отсутствовать в запросе
    if( params != null ) {
      TsIllegalArgumentRtException.checkFalse( params.isObject() );
    }
    TsIllegalArgumentRtException.checkFalse( id != null );
    TsIllegalArgumentRtException.checkFalse( id != null && id.isInteger() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация IJSONRequest
  //
  @Override
  public String method() {
    return value().fields().getByKey( SPEC_FIELD_METHOD ).asString();
  }

  @Override
  public IStringMap<ITjValue> params() {
    return value().fields().getByKey( SPEC_FIELD_PARAMS ).asObject().fields();
  }

  @Override
  public int id() {
    return value().fields().getByKey( SPEC_FIELD_ID ).asNumber().intValue();
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //
  @Override
  public String toString() {
    return String.format( JSON_REQUEST_TOSTRING_FORMAT, Integer.valueOf( id() ), method() );
  }
}
