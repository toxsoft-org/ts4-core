package org.toxsoft.core.pas.client;

import java.net.Socket;

import org.toxsoft.core.pas.common.PasChannel;
import org.toxsoft.core.pas.common.PasHandlerHolder;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Базовый класс клиента СПД двустороннего посимвольного канала связи между клиентом и сервером.
 *
 * @author mvk
 */
public class PasClientChannel
    extends PasChannel {

  /**
   * Фабрика каналов
   */
  public static final IPasClientChannelCreator<PasClientChannel> CREATOR = PasClientChannel::new;

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
  protected PasClientChannel( ITsContextRo aContext, Socket aSocket,
      PasHandlerHolder<? extends PasClientChannel> aHandlerHolder, ILogger aLogger ) {
    super( aContext, aSocket, aHandlerHolder, aLogger );
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //
}
