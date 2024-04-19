package org.toxsoft.core.pas.client;

import static java.net.InetAddress.*;
import static org.toxsoft.core.pas.client.ITsResources.*;
import static org.toxsoft.core.pas.common.PasUtils.*;
import static org.toxsoft.core.pas.server.IPasServerParams.*;

import java.io.IOException;
import java.net.Socket;

import org.toxsoft.core.pas.common.*;
import org.toxsoft.core.pas.json.IJSONMessage;
import org.toxsoft.core.pas.server.PasServer;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.ICooperativeMultiTaskable;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedList;
import org.toxsoft.core.tslib.coll.synch.SynchronizedListEdit;
import org.toxsoft.core.tslib.utils.ICloseable;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Реализация клиента Сервера Публичного Доступа (СПД) {@link PasServer}.
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером.
 */
public class PasClient<CHANNEL extends PasClientChannel>
    extends PasHandlerHolder<CHANNEL>
    implements Runnable, ICooperativeMultiTaskable, ICloseable {

  /**
   * Фабрика каналов.
   */
  private final IPasClientChannelCreator<CHANNEL> creator;

  /**
   * Признак того что {@link #doJob()} вызвается клиентом. Если признак не установлен (false), то для вызова
   * {@link #doJob()} создается внутренний поток.
   */
  private final boolean externalDoJobCall;

  /**
   * Список открытых каналов (в списке всегда должен быть один).
   */
  private final IListEdit<CHANNEL> channels = new SynchronizedListEdit<>( new ElemLinkedList<CHANNEL>() );

  private volatile Thread  clientThread;
  private volatile boolean running     = false;
  private volatile boolean stopQueried = false;
  private volatile boolean channelFail = false;

  /**
   * Сокет созданный вовремя инициализации null: сокет создается только на {@link #run()}.
   */
  private volatile Socket initSocket;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContextRo} - контекст приложения, использующего (запускающего) мост.
   * @param aChannelCreator {@link IPasClientChannelCreator} - создатель канала.
   * @param aExternalDoJobCall boolean <b>true</b> {@link #doJob()} вызывается клиентом;<b>false</b> для вызова.
   *          {@link #doJob()} создается внутрениий поток
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PasClient( ITsContextRo aContext, IPasClientChannelCreator<CHANNEL> aChannelCreator,
      boolean aExternalDoJobCall ) {
    super( aContext );
    creator = TsNullArgumentRtException.checkNull( aChannelCreator );
    externalDoJobCall = aExternalDoJobCall;
    setChannelHandler( new InternalPasChannelHandler() );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContextRo} - контекст приложения, использующего (запускающего) мост.
   * @param aChannelCreator {@link IPasClientChannelCreator} - создатель канала
   * @param aExternalDoJobCall boolean <b>true</b> {@link #doJob()} вызывается клиентом;<b>false</b> для вызова
   *          {@link #doJob()} создается внутрениий поток
   * @param aLogger {@link ILogger} реализация журнала работы класса
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PasClient( ITsContextRo aContext, IPasClientChannelCreator<CHANNEL> aChannelCreator,
      boolean aExternalDoJobCall, ILogger aLogger ) {
    super( aContext, aLogger );
    creator = TsNullArgumentRtException.checkNull( aChannelCreator );
    externalDoJobCall = aExternalDoJobCall;
    setChannelHandler( new InternalPasChannelHandler() );
  }

  // ------------------------------------------------------------------------------------
  // Открытое API
  //
  /**
   * Возвращает адрес PAS-сервера к которому производится подключение
   *
   * @return String адрес (сетевое имя или IP)
   */
  public final String remoteAddress() {
    return OP_PAS_SERVER_ADDRESS.getValue( context().params() ).asString();
  }

  /**
   * Возвращает порт PAS-сервера к которому производится подключение
   *
   * @return int номер порта
   */
  public final int remotePort() {
    return OP_PAS_SERVER_PORT.getValue( context().params() ).asInt();
  }

  /**
   * Инициализирует (подготавливает) клиента к запуску.
   * <p>
   * Метод выделен отдельно, чтобы разнести конструктор и реальные подготовительные работы.
   * <p>
   * Должен быть вызван один раз, перед {@link #run()}.
   *
   * @throws TsRuntimeException разнличные исключения с информацией об ошибках
   */
  public void init() {
    // nop
  }

  /**
   * Немедленно создает соединение с сервером
   *
   * @throws TsIllegalArgumentRtException соединение уже создано
   * @throws IOException ошибка создания соединения
   */
  public void createConnectionNow()
      throws IOException {
    if( initSocket != null ) {
      // Соединение уже создано
      throw new TsIllegalStateRtException( MSG_ERR_ALREADY_EXIST, remoteAddress(), Integer.valueOf( remotePort() ) );
    }
    initSocket = new Socket( getByName( remoteAddress() ), remotePort() );
  }

  /**
   * Определяет, выполняется ли мост.
   * <p>
   * Мост работает - означает, что он выполняет метод {@link #run()}.
   *
   * @return boolean - признак работы моста
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Возвращает канал обмена данными с сервером
   *
   * @return {@link PasClientChannel} канал. null: канал не установлен
   */
  public CHANNEL getChannelOrNull() {
    return channels.first();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Runnable
  //
  @Override
  public void run() {
    if( clientThread != null || stopQueried ) {
      // Недопустимый usecase
      throw new TsInternalErrorRtException();
    }
    clientThread = Thread.currentThread();
    clientThread.setName( makeThreadName( context() ) );
    running = true;
    stopQueried = false;
    try {
      // Запуск фоновой задачи
      PasDoJob doJob = null;
      if( !externalDoJobCall ) {
        doJob = new PasDoJob( "PasClient doJob", this, logger() ); //$NON-NLS-1$
        Thread thread = new Thread( doJob );
        thread.start();
      }
      try {
        ITsContextRo context = context();
        // Вступаем в цикл до момента запроса останова методом queryStop()
        while( !stopQueried ) {
          try {
            @SuppressWarnings( "resource" )
            Socket socket = initSocket != null ? initSocket : new Socket( getByName( remoteAddress() ), remotePort() );
            initSocket = null;
            channelFail = false;
            // Произведено подключение к серверу
            logger().info( FMT_INFO_CONNECT, socket );
            // Открытие канала связи при поступлении запроса
            CHANNEL channel = null;
            try {
              channel = creator.createChannel( context, socket, this, logger() );
            }
            catch( Throwable e ) {
              socket.close();
              // Неожиданная ошибка открытия канала
              throw new TsInternalErrorRtException( e, "unexpected create channel error. cause: %s", //$NON-NLS-1$
                  e.getLocalizedMessage() );
            }
            try {
              logger().info( FMT_INFO_CREATE_CHANNEL, channel );
              channels.add( channel );
              channel.run();
            }
            finally {
              try {
                if( channel != null ) {
                  channel.close();
                }
              }
              catch( Throwable e ) {
                logger().error( e );
              }
              channel = null;
            }
          }
          catch( IOException e ) {
            if( !channelFail ) {
              // Ошибка подключения к серверу
              logger().error( MSG_ERR_CANT_CONNECT, remoteAddress(), Integer.valueOf( remotePort() ), cause( e ) );
            }
            channelFail = true;
          }
          catch( Exception e ) {
            logger().error( e );
          }
        }
      }
      finally {
        if( doJob != null ) {
          doJob.close();
        }
      }
    }
    catch( Exception ex ) {
      logger().error( ex );
    }
    finally {
      stopQueried = false;
      // Снимаем с потока клиента состояние interrupted возможно установленное при close
      Thread.interrupted();
      synchronized (clientThread) {
        running = false;
        // Оповещение о завершении работы потока клиента
        clientThread.notifyAll();
      }
      clientThread = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICooperativeMultiTaskable
  //
  @Override
  public void doJob() {
    // В фоновой задачи проводится проверка работоспособности канала
    CHANNEL c = getChannelOrNull();
    if( c != null ) {
      c.checkAlive();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //
  @Override
  public void close() {
    Thread thread = clientThread;
    if( thread == null || stopQueried ) {
      return;
    }
    running = false;
    stopQueried = true;
    // Прерывание блокирующих вызовов потока
    thread.interrupt();
    // Завершение работы открытого канала
    CHANNEL channel = channels.first();
    if( channel != null ) {
      channel.close();
    }
    // Ожидание завершения потока клиента
    synchronized (thread) {
      if( running ) {
        try {
          thread.wait();
        }
        catch( InterruptedException e ) {
          logger().error( e );
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  /**
   * Возвращает имя потока клиента
   *
   * @param aContext {@link ITsContextRo} - контекст запускающего приложения
   * @return String имя потока
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static String makeThreadName( ITsContextRo aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IAtomicValue hostName = OP_PAS_SERVER_ADDRESS.getValue( aContext.params() );
    IAtomicValue portNo = OP_PAS_SERVER_PORT.getValue( aContext.params() );
    StringBuilder sb = new StringBuilder();
    Long now = Long.valueOf( System.currentTimeMillis() );
    sb.append( String.format( "PAS client [remote = %s:%s] %tF %tT", //$NON-NLS-1$
        hostName, portNo, now, now ) );
    return sb.toString();
  }

  /**
   * Обработчик событий каналов
   */
  class InternalPasChannelHandler
      implements IPasChannelHandler<CHANNEL> {

    // ------------------------------------------------------------------------------------
    // Реализация интерфейса IJSONRequestHandler
    //
    @Override
    public void onStart( CHANNEL aSource ) {
      // nop
    }

    @Override
    public void onShutdown( CHANNEL aSource ) {
      // Дерегистрация канала завершившего работу
      channels.remove( aSource );
    }

    @Override
    public void onSendError( CHANNEL aSource, IJSONMessage aMessage, Throwable aError ) {
      // Все ошибки приводят к завершению работы канала
      aSource.close();
    }

    @Override
    public void onReceiveError( CHANNEL aSource, Throwable aError ) {
      // Все ошибки приводят к завершению работы канала
      aSource.close();
    }
  }
}
