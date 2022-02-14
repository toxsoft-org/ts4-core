package org.toxsoft.core.pas.server;

import static org.toxsoft.core.pas.server.IPasServerParams.*;
import static org.toxsoft.core.pas.server.ITsResources.*;

import java.io.IOException;
import java.net.*;

import org.toxsoft.core.pas.common.*;
import org.toxsoft.core.pas.json.IJSONMessage;
import org.toxsoft.core.tslib.bricks.ICooperativeMultiTaskable;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedList;
import org.toxsoft.core.tslib.coll.synch.SynchronizedListEdit;
import org.toxsoft.core.tslib.utils.ICloseable;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Реализация Сервера Публичного Доступа (СПД)
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public class PasServer<CHANNEL extends PasServerChannel>
    extends PasHandlerHolder<CHANNEL>
    implements Runnable, ICooperativeMultiTaskable, ICloseable {

  /**
   * Фабрика каналов
   */
  private final IPasServerChannelCreator<CHANNEL> creator;

  /**
   * Признак того что {@link #doJob()} вызвается клиентом. Если признак не установлен (false), то для вызова
   * {@link #doJob()} создается внутренний поток
   */
  private final boolean externalDoJobCall;

  /**
   * Список открытых каналов
   */
  private final IListEdit<CHANNEL> channels = new SynchronizedListEdit<>( new ElemLinkedList<CHANNEL>() );

  /**
   * Серверный сокет для прослушивания запросов от табло.
   * <p>
   * Инициализируется в {@link #init()}, закрывается в {@link #close()}.
   */
  private ServerSocket serverSocket = null;

  /**
   * Признак завершения инициализации сервера
   */
  private volatile boolean inited = false;

  /**
   * Признак того, что сервер работает и готов принимать новые соединения
   */
  private volatile boolean running = false;

  /**
   * Признак требования завершить работу сервера
   */
  private volatile boolean stopQueried = false;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContextRo} - контекст приложения, использующего (запускающего) мост
   * @param aChannelCreator {@link IPasServerChannelCreator} - создатель канала
   * @param aExternalDoJobCall boolean <b>true</b> {@link #doJob()} вызывается клиентом;<b>false</b> для вызова
   *          {@link #doJob()} создается внутрениий поток
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PasServer( ITsContextRo aContext, IPasServerChannelCreator<CHANNEL> aChannelCreator,
      boolean aExternalDoJobCall ) {
    super( aContext );
    creator = TsNullArgumentRtException.checkNull( aChannelCreator );
    externalDoJobCall = aExternalDoJobCall;
    setChannelHandler( new InternalPasChannelHandler() );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContextRo} - контекст приложения, использующего (запускающего) мост
   * @param aChannelCreator {@link IPasServerChannelCreator} - создатель канала
   * @param aExternalDoJobCall boolean <b>true</b> {@link #doJob()} вызывается клиентом;<b>false</b> для вызова
   *          {@link #doJob()} создается внутрениий поток
   * @param aLogger {@link ILogger} реализация журнала работы класса
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PasServer( ITsContextRo aContext, IPasServerChannelCreator<CHANNEL> aChannelCreator,
      boolean aExternalDoJobCall, ILogger aLogger ) {
    super( aContext, aLogger );
    creator = TsNullArgumentRtException.checkNull( aChannelCreator );
    externalDoJobCall = aExternalDoJobCall;
    setChannelHandler( new InternalPasChannelHandler() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Runnable
  //
  @Override
  public void run() {
    TsIllegalStateRtException.checkFalse( inited );
    TsIllegalStateRtException.checkTrue( running );
    running = true;
    stopQueried = false;
    try {
      // Запуск фоновой задачи
      PasDoJob doJob = null;
      if( !externalDoJobCall ) {
        doJob = new PasDoJob( "PasServer doJob", this, logger() ); //$NON-NLS-1$
        Thread thread = new Thread( doJob );
        thread.start();
      }
      try {
        // вступаем в цикл до момента запроса останова методом queryStop()
        while( !stopQueried ) {
          try {
            // Сокет нового соединения с клиентом
            @SuppressWarnings( "resource" )
            Socket socket = serverSocket.accept();
            try {
              // Проверка существования ранее созданного канала
              CHANNEL prevChannel = findChannelByAddress( channels, socket.getInetAddress(), socket.getPort() );
              if( prevChannel != null ) {
                // Повтор создания канала с клиентом. Завершение работы предыдущего канала
                logger().warning( MSG_ERR_RECREATE_CHANNEL, prevChannel );
                prevChannel.close();
              }
              // Открытие канала связи при поступлении запроса
              CHANNEL channel = creator.createChannel( context(), socket, this, logger() );
              // Сохранение созданного канала
              channels.add( channel );
              // Запуск потока канала
              Thread thread = new Thread( channel );
              // Канал приступает к работе
              thread.start();
              // Запись в журнал о подключении клиента
              logger().info( FMT_INFO_CLIENT_ACCEPTED, channel );
            }
            catch( Exception e ) {
              logger().error( e );
              try {
                socket.close();
              }
              catch( Throwable e2 ) {
                logger().error( e2 );
              }
            }
          }
          catch( Exception e ) {
            logger().error( e );
          }
        }
        // Цикл прослушивания завершился, но надо остановить работающие каналы
        stopAndDisposeItemsWithTimeout( channels );
      }
      catch( Exception e ) {
        logger().error( e );
      }
      finally {
        if( doJob != null ) {
          doJob.close();
        }
      }
    }
    finally {
      stopQueried = false;
      running = false;
      inited = false;
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICooperativeMultiTaskable
  //
  @Override
  public void doJob() {
    for( CHANNEL channel : channels.copyTo( new ElemArrayList<>( channels.size() ) ) ) {
      try {
        channel.checkAlive();
      }
      catch( Exception e ) {
        logger().error( e );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //
  @Override
  public void close() {
    if( stopQueried || !running ) {
      // Сервер уже завершает или завершил работу
      return;
    }
    running = false;
    stopQueried = true;
    if( serverSocket != null ) {
      try {
        serverSocket.close();
      }
      catch( IOException ex ) {
        logger().error( ex );
      }
      serverSocket = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // Открытое API
  //
  /**
   * Инициализирует (подготавливает) процесс моста к запуску.
   * <p>
   * Метод выделен отдельно, чтобы разнести конструктор и реальные подготовительные работы.
   * <p>
   * Должен быть вызван один раз, перед {@link #run()}.
   *
   * @throws TsRuntimeException разнличные исключения с информацией об ошибках
   */
  public void init() {
    // инициализация прослушивания запросов к себе
    try {
      serverSocket = createServerSocket( context(), logger() );
    }
    catch( IOException ex ) {
      logger().error( ex );
      throw new TsRemoteIoRtException( ex, MSG_ERR_CANT_INIT_PAS );
    }
    // все, есть готовность к запуску процесса обработки запросов (то есть, готовы к запуску сервера СПД) в run()
    inited = true;
  }

  /**
   * Возвращает признак того, что сервер принимает соединения.
   * <p>
   * Сервер работает - означает, что он выполняет метод {@link #run()}.
   *
   * @return boolean - признак работы сервера
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Возвращает локальный адрес на котором принимаются соединения от клиентов
   *
   * @return {@link SocketAddress} локальный адрес сервера
   * @throws TsIllegalStateRtException сервер не слушает соединения от клиентов
   */
  public SocketAddress getLocalSocketAddress() {
    if( serverSocket == null ) {
      throw new TsIllegalStateRtException();
    }
    return serverSocket.getLocalSocketAddress();
  }

  /**
   * Возращает список открытых каналов сервера
   *
   * @return {@link IList}&lt;{@link PasChannel}&gt; список каналов
   */
  public IList<CHANNEL> channels() {
    return channels;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  /**
   * Создает серверный сокет приема соединений клиентов
   *
   * @param aContext {@link ITsContextRo} контекст приложения
   * @param aLogger {@link ILogger} журнал работы
   * @return {@link ServerSocket} созданный сокет
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws IOException ошибка создания сокета
   */
  private static ServerSocket createServerSocket( ITsContextRo aContext, ILogger aLogger )
      throws IOException {
    TsNullArgumentRtException.checkNulls( aContext, aLogger );
    // считывание конфигурации
    String hostName = OP_PAS_SERVER_ADDRESS.getValue( aContext.params() ).asString();
    int portNo = OP_PAS_SERVER_PORT.getValue( aContext.params() ).asInt();
    int inConnQueueSize = OP_PAS_IN_CONN_QUEUE_SIZE.getValue( aContext.params() ).asInt();
    // инициализация сервера прослушивания табло
    aLogger.info( FMT_INFO_INIT_SERV_SOCK, hostName, Integer.valueOf( portNo ) );
    if( !hostName.isEmpty() ) {
      InetAddress inetAddress = InetAddress.getByName( hostName );
      return new ServerSocket( portNo, inConnQueueSize, inetAddress );
    }
    return new ServerSocket( portNo, inConnQueueSize );
  }

  /**
   * Проводит поиск канала в указанном списке с указанным удаленным адресом и портом соединения
   *
   * @param aChannels {@link IList}&lt;{@link PasServerChannel}&gt; список каналов
   * @param aAddress {@link Inet4Address} удаленный адрес соединения
   * @param aPort int номер порта соединения
   * @param <CHANNEL> тип канала
   * @return {@link PasServerChannel} найденный канал. null: канал не найден
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static <CHANNEL extends PasServerChannel> CHANNEL findChannelByAddress( IList<CHANNEL> aChannels,
      InetAddress aAddress, int aPort ) {
    TsNullArgumentRtException.checkNulls( aChannels, aAddress );
    String hostname = aAddress.getCanonicalHostName();
    for( CHANNEL channel : aChannels.copyTo( new ElemArrayList<>( aChannels.size() ) ) ) {
      if( channel.getRemoteAddress().getCanonicalHostName().equals( hostname ) && channel.getRemotePort() == aPort ) {
        return channel;
      }
    }
    return null;
  }

  /**
   * Завершение работы каналов
   *
   * @param aChannels {@link IList} список завершаемых
   * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static <CHANNEL extends PasServerChannel> void stopAndDisposeItemsWithTimeout( IList<CHANNEL> aChannels ) {
    TsNullArgumentRtException.checkNull( aChannels );
    // запросим остановку на добровольной основе
    for( PasServerChannel channel : aChannels.copyTo( new ElemArrayList<CHANNEL>( aChannels.size() ) ) ) {
      channel.close();
    }
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
