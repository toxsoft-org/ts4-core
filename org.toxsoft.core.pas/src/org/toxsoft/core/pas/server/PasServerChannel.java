package org.toxsoft.core.pas.server;

import java.net.Socket;

import org.toxsoft.core.pas.common.PasChannel;
import org.toxsoft.core.pas.common.PasHandlerHolder;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Базовый класс сервера СПД двустороннего посимвольного канала связи между клиентом и сервером.
 *
 * @author mvk
 */
public class PasServerChannel
    extends PasChannel {

  /**
   * Фабрика каналов
   */
  public static final IPasServerChannelCreator<PasServerChannel> CREATOR = PasServerChannel::new;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContextRo} - контекст выполнения, общий для всех каналов и сервера
   * @param aSocket {@link Socket} сокет соединения
   * @param aHandlerHolder {@link PasHandlerHolder} хранитель обработчиков канала
   * @param aLogger {@link ILogger} журнал работы класса канала
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException ошибка создания читателя канала
   * @throws TsIllegalArgumentRtException ошибка создания писателя канала
   */
  protected PasServerChannel( ITsContextRo aContext, Socket aSocket,
      PasHandlerHolder<? extends PasServerChannel> aHandlerHolder, ILogger aLogger ) {
    super( aContext, aSocket, aHandlerHolder, aLogger );
  }
}
