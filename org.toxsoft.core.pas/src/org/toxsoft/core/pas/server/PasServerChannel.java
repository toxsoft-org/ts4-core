package org.toxsoft.core.pas.server;

import java.net.*;

import org.toxsoft.core.pas.common.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException ошибка создания читателя канала
   * @throws TsIllegalArgumentRtException ошибка создания писателя канала
   */
  protected PasServerChannel( ITsContextRo aContext, Socket aSocket,
      PasHandlerHolder<? extends PasServerChannel> aHandlerHolder ) {
    super( aContext, aSocket, aHandlerHolder );
  }
}
