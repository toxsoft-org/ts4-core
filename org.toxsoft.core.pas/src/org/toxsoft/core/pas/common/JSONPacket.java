package org.toxsoft.core.pas.common;

import org.toxsoft.core.pas.json.*;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListBasicEdit;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedList;
import org.toxsoft.core.tslib.coll.synch.SynchronizedListBasicEdit;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Список JSON-сообщений (уведомлений, запросов или ответов) передаваемых одним пакетом
 * <p>
 * В одном пакете могут быть одновременно:
 * <ul>
 * <li>Уведомления или запросы;</li>
 * <li>Результаты или ошибки выполнения запросов;</li>
 * </ul>
 * Порядок возвращаемых ответов (результат или ошибка) соответствует порядку и количеству запросов (не уведомлений!) в
 * соотвествующем пакете.
 *
 * @author mvk
 */
class JSONPacket {

  private final boolean                     batch;
  private final IListBasicEdit<JSONMessage> messages = new SynchronizedListBasicEdit<>( new ElemLinkedList<>() );
  private boolean                           response;

  /**
   * Конструктор пакета сообщений
   *
   * @param aBatch boolean <b>true</b> пакет представяет массив {@link IJSONMessage} сообщений; <b>false</b> одиночное
   *          {@link IJSONMessage} сообщение.
   */
  JSONPacket( boolean aBatch ) {
    batch = aBatch;
  }

  /**
   * Возвращает значение признака того, пакет представяет массив сообщений {@link IJSONMessage}
   *
   * @return boolean <b>true</b> пакет представяет массив {@link IJSONMessage} сообщений; <b>false</b> одиночное
   *         {@link IJSONMessage} сообщение.
   */
  public boolean batch() {
    return batch;
  }

  /**
   * Возвращает признак того, что пакет содержит ответ(ы)(результаты и/или ошибки) на ранее полученные запросы
   *
   * @return boolean<b>true</b> пакет содержит ответы ({@link IJSONResult} и/или {@link IJSONError}) ;<b>false</b> пакет
   *         содержит запросы {@link IJSONRequest} или уведомления {@link IJSONNotification}
   * @throws TsIllegalStateRtException пустой пакет
   */
  public boolean isResponse() {
    if( messages.size() == 0 ) {
      throw new TsIllegalStateRtException();
    }
    return response;
  }

  /**
   * Возвращает список JSON сообщений представляющих пакет
   *
   * @return {@link IList}&lt;{@link IJSONMessage} список сообщений пакета
   */
  public IList<JSONMessage> messages() {
    return messages;
  }

  /**
   * Добавляет JSON сообщений в пакет
   *
   * @param aMessage {@link JSONMessage} сообщение
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException пакет не пустой и имеет несовместимый тип с добавляемым сообщением (смотри
   *           {@link #isResponse()}).
   * @throws TsIllegalStateRtException попытка добавить несколько сообщений в пакет с {@link #batch()}==<b>false</b>
   */
  public void add( JSONMessage aMessage ) {
    TsNullArgumentRtException.checkNull( aMessage );
    boolean responseMessage = aMessage instanceof IJSONResponse;
    if( messages.size() > 0 && isResponse() && !responseMessage ) {
      // Недопустимый тип сообщения
      throw new TsIllegalArgumentRtException();
    }
    if( messages.size() == 1 && !batch ) {
      // Попытка добавить несколько сообщений в пакет с {@link #batch()}==<b>false</b>
      throw new TsIllegalStateRtException();
    }
    messages.add( aMessage );
    response = responseMessage;
  }

}
