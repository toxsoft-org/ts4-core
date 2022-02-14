package org.toxsoft.core.pas.json;

import org.toxsoft.core.pas.common.IJSONSpecification;
import org.toxsoft.core.pas.common.PasChannel;
import org.toxsoft.core.pas.tj.ITjValue;

/**
 * Обработчик уведомлений {@link IJSONNotification}
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public interface IJSONNotificationHandler<CHANNEL extends PasChannel> {

  /**
   * Обработать уведомление.
   * <p>
   * Реализация метода должна определить обработку уведомления. Формирование ответа - не требуется, но допускается
   * передача ошибок {@link PasChannel#sendError(int, String, ITjValue)}.
   * <p>
   * При формировании новых кодов ошибок следует учитывать уже существующие:
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_PARSE} - ошибка формата;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INVALID_METHOD_PARAMS} - неверные параметры уведомления;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INTERNAL} - неожиданная (не обработанная должным образом)
   * ошибка.</li>
   *
   * @param aChannel {@link PasChannel} канал по которому поступил запрос
   * @param aNotification {@link IJSONRequest} принятое уведомление
   */
  void notify( CHANNEL aChannel, IJSONNotification aNotification );
}
