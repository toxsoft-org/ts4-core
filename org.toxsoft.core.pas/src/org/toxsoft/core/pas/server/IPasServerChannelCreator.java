package org.toxsoft.core.pas.server;

import java.net.*;

import org.toxsoft.core.pas.client.*;
import org.toxsoft.core.pas.common.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Интерфейс создания конкретного экземпляра {@link PasServerChannel}.
 * <p>
 * Этот интерфейс передается в конструктор сервера
 * {@link PasServer#PasServer(ITsContextRo, IPasServerChannelCreator, boolean)}. При подкулючении очередного клиента с
 * помощью {@link #createChannel(ITsContextRo, Socket, PasHandlerHolder )} создается экземпляр канала, реализованный
 * разработчиком конкретного СПД.
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public interface IPasServerChannelCreator<CHANNEL extends PasServerChannel> {

  /**
   * Создает двунаправленный канал обслуживания клиента СПД.
   *
   * @param aContext {@link ITsContextRo} - контекст выполнения, общий для всех каналов и сервера
   * @param aSocket {@link Socket} сокет соединения
   * @param aHandlerHolder {@link PasHandlerHolder} хранитель обработчиков состояния канала
   * @return {@link PasClientChannel} созданный канал
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException ошибка создания читателя канала
   * @throws TsIllegalArgumentRtException ошибка создания писателя канала
   */
  CHANNEL createChannel( ITsContextRo aContext, Socket aSocket, PasHandlerHolder<CHANNEL> aHandlerHolder );
}
