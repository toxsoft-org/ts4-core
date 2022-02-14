package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.IJSONSpecification.*;
import static org.toxsoft.core.pas.common.ITsResources.*;
import static org.toxsoft.core.pas.tj.impl.TjUtils.*;

import org.toxsoft.core.pas.json.*;
import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация JSON ответа
 *
 * @author mvk
 */
public final class JSONResult
    extends JSONResponse
    implements IJSONResult {

  /**
   * Формирование результата обработки на запроса
   *
   * @param aRequest {@link IJSONRequest} запрос на который формируется результат
   * @param aResult {@link ITjValue} результат выполнения запроса.
   * @return {@link JSONResult} результат обработки запроса
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static JSONResult createResult( IJSONRequest aRequest, ITjValue aResult ) {
    TsNullArgumentRtException.checkNull( aRequest );
    TsNullArgumentRtException.checkNull( aResult );
    return new JSONResult( aRequest.id(), aResult );
  }

  /**
   * Конструктор нового ответа на запрос
   *
   * @param aID int идентификатор запроса на который формируется ответ уникальный в рамках сессии обмена данными
   * @param aResult {@link ITjValue} результат выполнения запроса.
   * @throws TsNullArgumentRtException аргумент = null
   */
  JSONResult( int aID, ITjValue aResult ) {
    super( EJSONKind.RESULT );
    TsNullArgumentRtException.checkNull( aResult );
    ITjObject value = value();
    value.fields().put( SPEC_FIELD_RESULT, aResult );
    value.fields().put( SPEC_FIELD_ID, createNumber( aID ) );
  }

  /**
   * Конструктор полученного ответа на запрос
   *
   * @param aValue {@link ITjObject} запрос в формате {@link ITjObject}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException неверный формат ответа на запрос
   */
  JSONResult( ITjObject aValue ) {
    super( EJSONKind.RESULT, aValue );
    ITjObject value = value();
    ITjValue result = value.fields().findByKey( SPEC_FIELD_RESULT );
    ITjValue id = value.fields().findByKey( SPEC_FIELD_ID );
    TsIllegalArgumentRtException.checkFalse( result != null );
    TsIllegalArgumentRtException.checkFalse( id != null );
    TsIllegalArgumentRtException.checkFalse( id != null && id.isInteger() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация IJSONResult
  //
  @Override
  public ITjValue result() {
    return value().fields().getByKey( SPEC_FIELD_RESULT );
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
    return String.format( JSON_RESULT_TOSTRING_FORMAT, Integer.valueOf( id() ), result() );
  }
}
