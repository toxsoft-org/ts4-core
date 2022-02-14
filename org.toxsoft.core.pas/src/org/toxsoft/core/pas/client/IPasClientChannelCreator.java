package org.toxsoft.core.pas.client;

import java.net.Socket;

import org.toxsoft.core.pas.common.PasHandlerHolder;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Интерфейс создания конкретного экземпляра {@link PasClientChannel}.
 * <p>
 * Этот интерфейс передается в конструктор сервера
 * {@link PasClient#PasClient(ITsContextRo, IPasClientChannelCreator, boolean)}. При подкулючении очередного клиента с
 * помощью {@link #createChannel(ITsContextRo, Socket, PasHandlerHolder, ILogger)} создается экземпляр канала,
 * реализованный разработчиком конкретного СПД.
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public interface IPasClientChannelCreator<CHANNEL extends PasClientChannel> {

  /**
   * Создает двунаправленный канал обслуживания клиента СПД.
   *
   * @param aContext {@link ITsContextRo} - контекст выполнения, общий для всех каналов и сервера
   * @param aSocket {@link Socket} сокет соединения
   * @param aHandlerHolder {@link PasHandlerHolder} хранитель обработчиков состояния канала
   * @param aLogger {@link ILogger} журнал используемый для работы канала
   * @return {@link PasClientChannel} созданный канал
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException ошибка создания читателя канала
   * @throws TsIllegalArgumentRtException ошибка создания писателя канала
   */
  CHANNEL createChannel( ITsContextRo aContext, Socket aSocket, PasHandlerHolder<CHANNEL> aHandlerHolder,
      ILogger aLogger );
}
