package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.IJSONSpecification.*;
import static org.toxsoft.core.pas.common.ITsResources.*;
import static org.toxsoft.core.pas.tj.impl.TjUtils.*;

import org.toxsoft.core.pas.json.*;
import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация JSON ошибки обработки запроса
 *
 * @author mvk
 */
public final class JSONError
    extends JSONResponse
    implements IJSONError {

  /**
   * Формирование ошибки выполнения запроса
   *
   * @param aRequest {@link IJSONRequest} запрос на который формируется ошибка
   * @param aErrorCode int код ошибки
   * @param aErrorMessage String текст ошибки
   * @param aErrorData ITjValue дополнительные данные об ошибке. null: данные не определяются.
   * @return {@link JSONError} ошибка обработки запроса
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static JSONError createError( IJSONRequest aRequest, int aErrorCode, String aErrorMessage,
      ITjValue aErrorData ) {
    TsNullArgumentRtException.checkNull( aRequest );
    TsNullArgumentRtException.checkNull( aErrorMessage );
    return new JSONError( Integer.valueOf( aRequest.id() ), aErrorCode, aErrorMessage, aErrorData );
  }

  /**
   * Конструктор новой ошибки
   *
   * @param aID Integer идентификатор запроса уникальный в рамках сессии обмена данными. null: идентификатор
   *          неопределяется - нарушение формата запроса Ключ: строковый идентификатор параметра;<br>
   *          Значение: значение параметра в формате {@link ITjValue}.
   * @param aErrorCode int код ошибки
   * @param aErrorMessage String текст ошибки
   * @param aErrorData ITjValue дополнительные данные об ошибке. null: данные не определяются.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат запроса
   */
  JSONError( Integer aID, int aErrorCode, String aErrorMessage, ITjValue aErrorData ) {
    super( EJSONKind.ERROR );
    TsNullArgumentRtException.checkNull( aErrorMessage );
    ITjObject value = value();
    // По спецификации JSON поле "id" может отсутствовать в запросе
    if( aID != null ) {
      value.fields().put( SPEC_FIELD_ID, createNumber( aID.intValue() ) );
    }
    value.fields().put( SPEC_FIELD_ERROR, createObject( createTjObject() ) );
    ITjObject error = value.fields().getByKey( SPEC_FIELD_ERROR ).asObject();
    error.fields().put( SPEC_FIELD_ERROR_CODE, createNumber( aErrorCode ) );
    error.fields().put( SPEC_FIELD_ERROR_MESSAGE, createString( aErrorMessage ) );
    // По спецификации JSON поле "error.data" может отсутствовать в запросе
    if( aErrorData != null ) {
      error.fields().put( SPEC_FIELD_ERROR_DATA, aErrorData );
    }
  }

  /**
   * Конструктор полученной ошибки
   *
   * @param aValue {@link ITjObject} запрос в формате {@link ITjObject}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат ошибки
   */
  JSONError( ITjObject aValue ) {
    super( EJSONKind.ERROR, aValue );
    ITjObject value = value();
    ITjValue error = value.fields().findByKey( SPEC_FIELD_ERROR );
    ITjValue id = value.fields().findByKey( SPEC_FIELD_ID );
    // По спецификации JSON поле "id" может отсутствовать в запросе
    if( id != null && !id.isInteger() ) {
      throw new TsIllegalArgumentRtException();
    }
    if( error == null ) {
      throw new TsIllegalArgumentRtException();
    }
    TsIllegalArgumentRtException.checkFalse( error.isObject() );
    IStringMapEdit<ITjValue> errorFields = error.asObject().fields();
    ITjValue code = errorFields.findByKey( SPEC_FIELD_ERROR_CODE );
    // По спецификации JSON поле "error.data" может отсутствовать в запросе
    // ITjValue data = errorFields.findByKey( SPEC_FIELD_ERROR_DATA );
    ITjValue message = errorFields.findByKey( SPEC_FIELD_ERROR_MESSAGE );
    TsIllegalArgumentRtException.checkFalse( code != null );
    TsIllegalArgumentRtException.checkFalse( code != null && code.isInteger() );
    TsIllegalArgumentRtException.checkFalse( message != null );
    TsIllegalArgumentRtException.checkFalse( message != null && message.isString() );
    // По спецификации JSON поле "error.data" может отсутствовать в запросе
    // if( data != null ) {
    // TsIllegalArgumentRtException.checkFalse( data.isObject() );
    // }
  }

  // ------------------------------------------------------------------------------------
  // Реализация IJSONError
  //
  @Override
  public int code() {
    ITjObject error = value().fields().getByKey( SPEC_FIELD_ERROR ).asObject();
    return error.fields().getByKey( SPEC_FIELD_ERROR_CODE ).asNumber().intValue();
  }

  @Override
  public ITjValue data() {
    ITjObject error = value().fields().getByKey( SPEC_FIELD_ERROR ).asObject();
    return error.fields().findByKey( SPEC_FIELD_ERROR_DATA );
  }

  @Override
  public String message() {
    ITjObject error = value().fields().getByKey( SPEC_FIELD_ERROR ).asObject();
    return error.fields().getByKey( SPEC_FIELD_ERROR_MESSAGE ).asString();
  }

  @Override
  public Integer id() {
    ITjValue id = value().fields().findByKey( SPEC_FIELD_ID );
    if( id == null ) {
      return null;
    }
    return Integer.valueOf( id.asNumber().intValue() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //
  @Override
  public String toString() {
    return String.format( JSON_ERROR_TOSTRING_FORMAT, id(), Integer.valueOf( code() ), message(), data() );
  }
}
