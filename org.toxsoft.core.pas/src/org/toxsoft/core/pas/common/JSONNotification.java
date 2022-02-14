package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.IJSONSpecification.*;
import static org.toxsoft.core.pas.common.ITsResources.*;
import static org.toxsoft.core.pas.tj.impl.TjUtils.*;

import org.toxsoft.core.pas.json.EJSONKind;
import org.toxsoft.core.pas.json.IJSONNotification;
import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация JSON уведомления
 * <p>
 * Реализация скрыта на уровне пакета в котором находится базовый канал обмена {@link PasChannel}. Предполагается, что
 * пользователи для создания уведомлений будут использовать метод
 * {@link PasChannel#sendNotification(String, IStringMap)}
 *
 * @author mvk
 */
final class JSONNotification
    extends JSONMessage
    implements IJSONNotification {

  /**
   * Конструктор нового запроса
   *
   * @param aMethod String имя запроса
   * @param aParams {@link IStringMap} карта параметров вызываемого метода.<br>
   *          Ключ: строковый идентификатор параметра;<br>
   *          Значение: значение параметра в формате {@link ITjValue}.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат запроса
   */
  JSONNotification( String aMethod, IStringMap<ITjValue> aParams ) {
    super( EJSONKind.NOTIFICATION );
    TsNullArgumentRtException.checkNull( aMethod );
    TsNullArgumentRtException.checkNull( aParams );
    ITjObject value = value();
    value.fields().put( SPEC_FIELD_METHOD, createString( aMethod ) );
    // По спецификации JSON поле "params" может отсутствовать в запросе
    if( aParams.keys().size() > 0 ) {
      value.fields().put( SPEC_FIELD_PARAMS, createObject( createTjObject() ) );
      value.fields().getByKey( SPEC_FIELD_PARAMS ).asObject().fields().putAll( aParams );
    }
  }

  /**
   * Конструктор полученного уведомления
   *
   * @param aValue {@link ITjObject} уведомление в формате {@link ITjObject}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат запроса
   */
  JSONNotification( ITjObject aValue ) {
    super( EJSONKind.NOTIFICATION, aValue );
    ITjObject value = value();
    ITjValue method = value.fields().findByKey( SPEC_FIELD_METHOD );
    ITjValue params = value.fields().findByKey( SPEC_FIELD_PARAMS );
    TsIllegalArgumentRtException.checkFalse( method != null );
    TsIllegalArgumentRtException.checkFalse( method != null && method.isString() );
    // По спецификации JSON поле "params" может отсутствовать в запросе
    if( params != null ) {
      TsIllegalArgumentRtException.checkFalse( params.isObject() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация IJSONNotification
  //
  @Override
  public String method() {
    return value().fields().getByKey( SPEC_FIELD_METHOD ).asString();
  }

  @Override
  public IStringMap<ITjValue> params() {
    return value().fields().getByKey( SPEC_FIELD_PARAMS ).asObject().fields();
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //
  @Override
  public String toString() {
    return String.format( JSON_NOTIFICATION_TOSTRING_FORMAT, method() );
  }
}
