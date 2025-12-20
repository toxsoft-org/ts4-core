package org.toxsoft.core.pas.common;

import static java.lang.String.*;
import static org.toxsoft.core.pas.common.IJSONSpecification.*;
import static org.toxsoft.core.pas.common.IPasParams.*;
import static org.toxsoft.core.pas.common.IPasProtocol.*;
import static org.toxsoft.core.pas.common.ITsResources.*;
import static org.toxsoft.core.pas.common.JSONError.*;
import static org.toxsoft.core.pas.common.PasUtils.*;
import static org.toxsoft.core.pas.tj.impl.TjUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.time.impl.TimeUtils.*;

import java.io.*;
import java.net.*;

import org.toxsoft.core.pas.client.*;
import org.toxsoft.core.pas.json.*;
import org.toxsoft.core.pas.server.*;
import org.toxsoft.core.pas.tj.*;
import org.toxsoft.core.pas.tj.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;

/**
 * Базовый класс двустороннего посимвольного канала связи между клиентом и сервером СПД.
 * <p>
 * TODO: отработать проблему блокировки записи:
 * https://stackoverflow.com/questions/1338885/java-socket-output-stream-writes-do-they-block
 *
 * @author mvk
 */
public class PasChannel
    implements IPasTxChannel, Runnable, ICloseable {

  private static final String CHARSET = "UTF-8"; //$NON-NLS-1$

  private final ITsContextRo                 context;
  private final Socket                       socket;
  private final PasHandlerHolder<PasChannel> parentHandlerHolder;
  private final PasHandlerHolder<PasChannel> channelHandlerHolder;
  private final PasLoggableReader            in;
  private final OutputStreamWriter           out;
  private final IStrioReader                 strioReader;
  private final IStrioWriter                 strioWriter;
  private final IStringMapEdit<IJSONRequest> executingRequests    = new SynchronizedStringMap<>( new StringMap<>() );
  private final IListEdit<Thread>            writingThreads       = new ElemArrayList<>();
  private long                               lastWritingTimestamp = MIN_TIMESTAMP;

  private final PasChannelWriter channelWriter;
  private volatile Thread        channelThread;
  private volatile boolean       stopQueried        = false;
  private volatile int           requestCount;
  private final long             creationTimestamp  = System.currentTimeMillis();
  private volatile long          lastReadTimestamp  = System.currentTimeMillis();
  private volatile long          lastWriteTimestamp = System.currentTimeMillis();
  private volatile int           failureTimeout     = OP_PAS_FAILURE_TIMEOUT.defaultValue().asInt();
  private volatile int           writeTimeout       = OP_PAS_WRITE_TIMEOUT.defaultValue().asInt();

  private final ILogger logger;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContext} - контекст выполнения, общий для всех каналов и сервера
   * @param aSocket {@link Socket} сокет соединения
   * @param aHandlerHolder {@link PasHandlerHolder} родительский контейнер обработчиков канала
   * @param aLogger {@link ILogger} журнал работы класса канала
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException ошибка создания читателя канала
   * @throws TsIllegalArgumentRtException ошибка создания писателя канала
   */
  @SuppressWarnings( { "unchecked", "resource" } )
  protected PasChannel( ITsContextRo aContext, Socket aSocket, PasHandlerHolder<? extends PasChannel> aHandlerHolder,
      ILogger aLogger ) {
    context = TsNullArgumentRtException.checkNull( aContext );
    socket = TsNullArgumentRtException.checkNull( aSocket );
    parentHandlerHolder = (PasHandlerHolder<PasChannel>)TsNullArgumentRtException.checkNull( aHandlerHolder );
    channelHandlerHolder = new PasHandlerHolder<>( aContext );
    try {
      in = new PasLoggableReader( new InputStreamReader( socket.getInputStream(), CHARSET ) );
      ICharInputStream chIn = new CharInputStreamReader( in );
      strioReader = new StrioReader( chIn );
    }
    catch( IOException e ) {
      // Ошибка создания канала
      throw new TsIllegalArgumentRtException( e, ERR_CREATE_READER, cause( e ) );
    }
    try {
      out = new OutputStreamWriter( socket.getOutputStream(), CHARSET );
      CharOutputStreamWriter chOut = new CharOutputStreamWriter( out );
      strioWriter = new StrioWriter( chOut );
    }
    catch( IOException e ) {
      // Ошибка создания писателя канала
      throw new TsIllegalArgumentRtException( e, ERR_CREATE_WRITER, cause( e ) );
    }
    // Писатель данных в канал
    channelWriter = new PasChannelWriter( this, aLogger );
    // Журнал
    logger = TsNullArgumentRtException.checkNull( aLogger );

    if( aSocket.getLocalAddress().equals( aSocket.getInetAddress() ) && //
        aSocket.getLocalPort() == aSocket.getPort() ) {
      // Недопустимое соединение на 'самого себя': (совпадение адреса источника и приемника канала)
      throw new TsIoRtException( ERR_SELF_ADDR_CONNECTION, aSocket.getLocalAddress(),
          Integer.valueOf( aSocket.getLocalPort() ) );
    }

    // Конфигурация
    IOptionSet config = context.params();
    // Установка таймаута отказа связи
    setFailureTimeout( OP_PAS_FAILURE_TIMEOUT.getValue( config ).asInt() );
    // Установка таймаута ошибки записи в сокет
    setWriteTimeout( OP_PAS_WRITE_TIMEOUT.getValue( config ).asInt() );
  }

  // ------------------------------------------------------------------------------------
  // Открытое API
  //
  /**
   * Возвращает локальный адрес соединения
   *
   * @return {@link SocketAddress} адрес соединения
   */
  public final InetAddress getLocalAddress() {
    return socket.getLocalAddress();
  }

  /**
   * Возвращает локальный порт соединения
   *
   * @return int порт соединения
   */
  public final int getLocalPort() {
    return socket.getLocalPort();
  }

  /**
   * Возвращает удаленный адрес соединения
   *
   * @return {@link InetAddress} адрес соединения
   */
  public final InetAddress getRemoteAddress() {
    return socket.getInetAddress();
  }

  /**
   * Возвращает удаленный порт соединения
   *
   * @return int порт соединения
   */
  public final int getRemotePort() {
    return socket.getPort();
  }

  /**
   * Возвращает время создания канала
   *
   * @return long время (мсек с начала эпохи) создания канала
   */
  public final long getCreationTimestamp() {
    return creationTimestamp;
  }

  /**
   * Определает, выполняется ли метод {@link #run()}.
   * <p>
   * Возвращает <code>false</code> до и посте вызова метода {@link #run()}. В теле мтеода {@link #run()} возвращает
   * <code>true</code>.
   *
   * @return boolean - признак выполнения метода {@link #run()}
   */
  public final boolean isRunning() {
    return channelThread != null;
  }

  /**
   * Если необходимо, то отправляет тестового сообщение для проверки канала
   *
   * @return boolean <b>true</b> сообщение отправлено; <b>false</b> сообщение не отправлено
   */
  public final boolean checkAlive() {
    // Проверка таймаута у выполняемых потоков записи
    if( writeTimeout > 0 ) {
      synchronized (writingThreads) {
        if( lastWritingTimestamp != MIN_TIMESTAMP && //
            System.currentTimeMillis() - lastWritingTimestamp > writeTimeout ) {
          for( Thread writingThread : writingThreads ) {
            logger().error( ERR_WRITE_TIMEOUT, this, writingThread, Integer.valueOf( writeTimeout ) );
            writingThread.interrupt();
          }
          writingThreads.clear();
          lastWritingTimestamp = MIN_TIMESTAMP;
        }
      }
    }
    if( failureTimeout <= 0 ) {
      // Не установлен таймаут отказа канала (failureTimeout)
      logger().error( ERR_UNDEF_FAILURE_TIMEOUT, this );
      return false;
    }
    // Текущее время
    long currTime = System.currentTimeMillis();
    // Время (мсек) с последнего получения данных
    long fromReadTime = Math.abs( currTime - lastReadTimestamp );
    // Время (мсек) с последней передачи данных
    long fromWriteTime = Math.abs( currTime - lastWriteTimestamp );

    if( fromReadTime > failureTimeout ) {
      // Завершение работы канала по обнаруженному отказу работы (isFailure = true)
      Long time = Long.valueOf( fromReadTime );
      logger.error( ERR_CLOSE_CHANNEL_BY_FAILURE_TIMEOUT, this, Integer.valueOf( failureTimeout ), time );
      // 2025-12-20 TODO: mvkd ---
      close();
      return false;
    }
    if( fromWriteTime < ((2 * failureTimeout) / 3) ) {
      // Активность канала позволяет не посылать тестовое сообщение
      return false;
    }
    // Отправка сообщения проверки канала
    return sendPing( this, failureTimeout );
  }

  /**
   * Установить журнал принимаемых, передаваемых данных по каналу
   *
   * @param aLogger {@link IPasIoLogger} журнал. null: отключить журналирование
   */
  public void setIoLogger( IPasIoLogger aLogger ) {
    in.setIoLoggerOrNull( aLogger );
  }

  /**
   * Возвращает журнал работы класса
   *
   * @return {@link ILogger} журнал работы
   */
  public ILogger logger() {
    return logger;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPasTxChannel
  //
  @Override
  public void sendRequest( String aMethod, IStringMap<ITjValue> aParams ) {
    TsNullArgumentRtException.checkNull( aMethod );
    TsNullArgumentRtException.checkNull( aParams );
    // Формирование запроса
    JSONRequest request = new JSONRequest( ++requestCount, aMethod, aParams );
    // Сохранение запроса в списке выполняемых
    executingRequests.put( request.method(), request );
    // Отправка запроса по каналу
    writeToChannel( this, request );
  }

  @Override
  public void sendNotification( String aMethod, IStringMap<ITjValue> aParams ) {
    TsNullArgumentRtException.checkNull( aMethod );
    TsNullArgumentRtException.checkNull( aParams );
    // Формирование уведомления
    JSONNotification notification = new JSONNotification( aMethod, aParams );
    // Отправка уведомления по каналу
    writeToChannel( this, notification );
  }

  @Override
  public void sendError( int aErrorCode, String aErrorMessage, ITjValue aErrorData ) {
    TsNullArgumentRtException.checkNull( aErrorMessage );
    JSONError error = new JSONError( null, aErrorCode, aErrorMessage, aErrorData );
    writeToChannel( this, error );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Runnable
  //
  @Override
  public final void run() {
    if( channelThread != null || stopQueried ) {
      // Недопустимый usecase
      throw new TsInternalErrorRtException();
    }
    channelThread = Thread.currentThread();
    channelThread.setName( makeThreadName( socket ) );
    // Типизация канала
    try {
      // Опциональная инициализация наследника
      stopQueried = doInit( this );
      if( !stopQueried ) {
        try {
          channelHandler().onStart( this );
        }
        catch( Throwable e ) {
          logger.error( e );
        }
      }
      // Пока не запросят остановку канала, выпоняем цикл (который может и по другим причинам завершится)
      while( !stopQueried ) {
        try {
          // БЛОКИРУЮЩЕЕ чтение следующего сообщения в потоке
          IJSONMessage message = readFromChannel( this );
          if( message != null ) {
            switch( message.kind() ) {
              case NOTIFICATION:
                // Обработка уведомления
                handleNotification( this, (IJSONNotification)message );
                break;
              case REQUEST:
                // Обработка запроса
                handleRequest( this, (IJSONRequest)message );
                break;
              case RESULT:
                // Обработка результатов запроса
                handleResult( this, (IJSONResult)message );
                break;
              case ERROR:
                // Обработка ошибки обработки запроса
                handleError( this, (IJSONError)message );
                break;
              default:
                throw new TsNotAllEnumsUsedRtException();
            }
          }
        }
        catch( Exception e ) {
          // Любая ошибка на соединении приводит к его завершению
          logger.error( e );
          stopQueried = true;
        }
      }
      // Завершение соединения
      close( this );
      try {
        channelHandler().onShutdown( this );
      }
      catch( Throwable e ) {
        logger.error( e );
      }
    }
    finally {
      channelWriter.close();
      // Снимаем с потока канала состояние interrupted возможно установленное при close
      Thread.interrupted();
      channelThread = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //
  @Override
  public final void close() {
    Thread thread = channelThread;
    if( thread == null || stopQueried ) {
      return;
    }
    stopQueried = true;
    try {
      // Поток канала будет находится в состоянии 'interrupted' поэтому любые обращения к блокировкам в этом
      // потоке будет поднимать исключение InterruptedException (например завершение работы с s5). Чтобы не создавать
      // отдельный поток "завершения" мы даем наследнику возможность освободить ресурсы до перехода в состояние
      // 'interrupted'
      doClose();
    }
    catch( Exception e ) {
      logger.error( e );
    }
    try {
      // Необходимо закрыть сокет, так как thread.interrupt() может не сработать и поток "зависнет" на чтении
      socket.close();
    }
    catch( IOException e ) {
      logger.error( e );
    }
    // Прерывание блокирующих вызовов потока
    thread.interrupt();
  }

  // ------------------------------------------------------------------------------------
  // Реализация Object
  //
  @Override
  @SuppressWarnings( "boxing" )
  public String toString() {
    InetAddress localAddr = getLocalAddress();
    InetAddress remoteAddr = getRemoteAddress();
    String localIp = localAddr.getHostAddress();
    String remoteIp = remoteAddr.getHostAddress();
    int localPort = getLocalPort();
    int remotePort = getRemotePort();
    String creationTime = timestampToString( getCreationTimestamp() );
    return format( FMT_CHANNEL, remoteIp, remotePort, localIp, localPort, creationTime );
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + socket.toString().hashCode();
    return result;
  }

  @Override
  public boolean equals( Object aObject ) {
    if( this == aObject ) {
      return true;
    }
    if( aObject == null ) {
      return false;
    }
    if( getClass() != aObject.getClass() ) {
      return false;
    }
    PasChannel other = (PasChannel)aObject;
    return socket.toString().equals( other.socket.toString() );
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //
  /**
   * Возвращает контекст запускающего приложения.
   *
   * @return {@link ITsContextRo} - контекст запускающего приложения
   */
  protected final ITsContextRo context() {
    return context;
  }

  /**
   * Возвращает контейнер обработчиков зарегистрированные на уровне канала
   * <p>
   * В отличии от {@link PasServer#channelHandler()} и {@link PasClient#channelHandler()} в контейнере канала
   * зарегистрированы только обработчики зарегистрированные для целевого канала. По этой причине, после создания канала,
   * контейнер канала не имеет собственных обработчиков.
   *
   * @return {@link IPasChannelHandler} контейнер обработчиков
   */
  protected final PasHandlerHolder<PasChannel> handlerHolder() {
    return channelHandlerHolder;
  }

  /**
   * Возвращает текущий таймаута отказа работоспособности канала.
   *
   * @return aTimeout int значение таймаута. <= 0: отключение механизма проверки работоспособности канала.
   */
  protected final int failureTimeout() {
    return failureTimeout;
  }

  /**
   * Установка таймаута отказа работоспособности канала.
   *
   * @param aTimeout значение таймаута. <= 0: отключить проверку работоспособности канала
   */
  protected final void setFailureTimeout( int aTimeout ) {
    // Установка таймаута отказа работоспособности канала
    logger.debug( MSG_SET_FAILURE_TIMEOUT, this, Integer.valueOf( failureTimeout ), Integer.valueOf( aTimeout ) );
    failureTimeout = aTimeout;
    if( failureTimeout > 0 ) {
      try {
        socket.setSoTimeout( failureTimeout );
      }
      catch( SocketException ex ) {
        logger().error( ex );
      }
      // Отправка сообщения проверки канала - передача клиенту нового значения failureTimeout
      sendPing( this, failureTimeout );
      return;
    }
    try {
      socket.setSoTimeout( 0 );
    }
    catch( SocketException ex ) {
      logger().error( ex );
    }
  }

  /**
   * Возвращает текущий таймаут записи
   * <p>
   * Необходимость таймаута определена в источнике:
   * https://stackoverflow.com/questions/1338885/java-socket-output-stream-writes-do-they-block
   *
   * @return aTimeout {@link IAtomicValue} значение таймаута (мсек). <= 0: Отключение механизма.
   */
  protected final int writeTimeout() {
    return writeTimeout;
  }

  /**
   * Установка таймаута записи данных в канал
   * <p>
   * Необходимость таймаута определена в источнике:
   * https://stackoverflow.com/questions/1338885/java-socket-output-stream-writes-do-they-block
   *
   * @param aTimeout Значение таймаута (мсек). <= 0: Отключение механизма.
   */
  protected final void setWriteTimeout( int aTimeout ) {
    // Установка таймаута отказа работоспособности канала
    logger.debug( MSG_SET_WRITE_TIMEOUT, this, Integer.valueOf( writeTimeout ), Integer.valueOf( aTimeout ) );
    writeTimeout = aTimeout;
  }

  /**
   * Наследник может подготовиться к обслуживанию клиента.
   * <p>
   * Метод вызывается один раз из {@link #run()}, <b>до того</b> как будет принят первый запрос
   * <p>
   * Возвращаемое значение <code>true</code> позволяет остановить работу канала (точнее, просто не начать его
   * выполнение). В этом случае не будет принято ни одного запроса, но {@link #doClose()} - будет вызван штатно.
   * <p>
   * В базовом классе просто возвращает <code>false</code>, вызывать родительский метод в наследнике не нужно.
   *
   * @return boolean - признак завершения работы канала
   */
  protected boolean doInit() {
    return false;
  }

  /**
   * Прием уведомления (не требующего ответа) на обработку от удаленной стороны соединения.
   * <p>
   * Обработка уведомления может сформировать и передать ошибку {@link #sendError(int, String, ITjValue)}. При
   * формировании новых кодов ошибок следует учитывать уже существующие:
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_PARSE} - ошибка формата;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_METHOD_NOT_FOUND} - метод не существует;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INVALID_METHOD_PARAMS} - неверно заданы параметры вызова метода;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INTERNAL} - неожиданная (не обработанная должным образом)
   * ошибка.</li>
   *
   * @param aNotification {@link IJSONRequest} принятый запрос
   * @return boolean <b>true</b> уведомление обработано; <b>false</b> уведомление не обработано
   */
  protected boolean doReceiveNotification( IJSONNotification aNotification ) {
    return false;
  }

  /**
   * Прием запроса на обработку от удаленной стороны соединения.
   * <p>
   * Реализация метода определяется наследником и является альтернативой механизму регистрации исполнителей запросов
   * {@link PasHandlerHolder#registerRequestHandler(String, IJSONRequestHandler)}.
   * <p>
   * Наследник должен определить обработку принятого запроса и сформировать на него ответ или ошибку.
   * <p>
   * При формировании новых кодов ошибок следует учитывать уже существующие:
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_PARSE} - ошибка формата;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_METHOD_NOT_FOUND} - метод не существует;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INVALID_METHOD_PARAMS} - неверно заданы параметры вызова метода;</li>
   * <li>{@link IJSONSpecification#JSON_ERROR_CODE_INTERNAL} - неожиданная (не обработанная должным образом)
   * ошибка.</li>
   *
   * @param aRequest {@link IJSONRequest} принятый запрос
   * @return {@link JSONResponse} ответ (результат или ошибка) обработки запроса. null: запрос не обработан
   */
  protected JSONResponse doReceiveRequest( IJSONRequest aRequest ) {
    return null;
  }

  /**
   * Прием ответа обработки запроса от удаленной стороны соединения.
   * <p>
   * Реализация метода определяется наследником и является альтернативой механизму регистрации обработчиков результатов
   * запросов {@link PasHandlerHolder#registerResultHandler(String, IJSONResultHandler)}.
   * <p>
   * Наследник должен определить обработку принятого ответа.
   * <p>
   * По мере получения сообщений от удаленной стороны этот метод непрерывно вызывается из цикла метода {@link #run()} до
   * тех пор, пока не будет запрошен останов канала методом {@link #close()}.
   * <p>
   * В момент вызов этого метода, для обслуживания клиента, наследнику доступны:
   * <ul>
   * <li>опциональные параметры выполнения, переданные сервером в контексте {@link #context()}.</li>
   * </ul>
   *
   * @param aResult {@link IJSONResult} принятый результат
   * @return boolean <b>true</b> результаты запроса обработаны; <b>false</b> результаты запроса не обработаны
   */
  protected boolean doReceiveResult( IJSONResult aResult ) {
    return false;
  }

  /**
   * Прием ошибки обработки запроса от удаленной стороны соединения.
   * <p>
   * Реализация метода определяется наследником и является альтернативой механизму регистрации обработчиков ошибок
   * запросов {@link PasHandlerHolder#registerErrorHandler(String, IJSONErrorHandler)}.
   * <p>
   * Наследник должен определить обработку принятой ошибки, например формирование статистики.
   * <p>
   * По мере получения сообщений от удаленной стороны этот метод непрерывно вызывается из цикла метода {@link #run()} до
   * тех пор, пока не будет запрошен останов канала методом {@link #close()}.
   * <p>
   * В момент вызов этого метода, для обслуживания клиента, наследнику доступны:
   * <ul>
   * <li>опциональные параметры выполнения, переданные сервером в контексте {@link #context()}.</li>
   * </ul>
   *
   * @param aError {@link IJSONResult} принятая ошибка
   * @return boolean <b>true</b> ошибка запроса обработана; <b>false</b> ошибка запроса не обработана
   */
  protected boolean doReceiveError( IJSONError aError ) {
    return false;
  }

  /**
   * Наследник может предпринять дополнительные меры по совобождению ресурсов перед завершением работы.
   * <p>
   * Метод вызывается вызывается из {@link #run()}, когда основной цикл завершен, но работа канала еще не завершена.
   * <p>
   * Надо учесть, что {@link #close()} также может быть вызван извне, если после запроса остановки канала прошел
   * таймаует а канал все еще работает.
   * <p>
   * В базовом классе ничего не делает, вызывать родительский метод в наследнике не нужно.
   */
  protected void doClose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //
  /**
   * Возвращает обработчик событий каналов
   *
   * @return {@link IPasChannelHandler} обработчик событий
   */
  final IPasChannelHandler<PasChannel> channelHandler() {
    return parentHandlerHolder.channelHandler();
  }

  /**
   * Возвращает зарегистрированный обработчик уведомления
   *
   * @param aMethod String имя метода
   * @return {@link IJSONRequestHandler} обработчик уведомления. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  final IJSONNotificationHandler<PasChannel> findNotificationHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    IJSONNotificationHandler<PasChannel> retValue = channelHandlerHolder.findNotificationHandler( aMethod );
    if( retValue != null ) {
      return retValue;
    }
    return parentHandlerHolder.findNotificationHandler( aMethod );
  }

  /**
   * Возвращает зарегистрированный обработчик метода
   *
   * @param aMethod String имя метода
   * @return {@link IJSONRequestHandler} обработчик метода. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  final IJSONRequestHandler<PasChannel> findRequestHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    IJSONRequestHandler<PasChannel> retValue = channelHandlerHolder.findRequestHandler( aMethod );
    if( retValue != null ) {
      return retValue;
    }
    return parentHandlerHolder.findRequestHandler( aMethod );
  }

  /**
   * Возвращает зарегистрированный обработчик результатов выполнения метода
   *
   * @param aMethod String имя метода
   * @return {@link IJSONResultHandler} обработчик метода. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  final IJSONResultHandler<PasChannel> findResultHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    IJSONResultHandler<PasChannel> retValue = channelHandlerHolder.findResultHandler( aMethod );
    if( retValue != null ) {
      return retValue;
    }
    return parentHandlerHolder.findResultHandler( aMethod );
  }

  /**
   * Возвращает зарегистрированный обработчик ошибок выполнения метода
   *
   * @param aMethod String имя метода
   * @return {@link IJSONErrorHandler} обработчик метода. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  final IJSONErrorHandler<PasChannel> findErrorHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    IJSONErrorHandler<PasChannel> retValue = channelHandlerHolder.findErrorHandler( aMethod );
    if( retValue != null ) {
      return retValue;
    }
    return parentHandlerHolder.findErrorHandler( aMethod );
  }

  /**
   * Возвращает карту выполняемых в данный момент запросов
   *
   * @return {@link IStringMapEdit} карта запросов с возможностью редактирования;<br>
   *         Ключ: имя метода запроса {@link IJSONRequest#method()}; <br>
   *         Значение: запрос {@link IJSONRequest}.
   */
  final IStringMapEdit<IJSONRequest> executingRequests() {
    return executingRequests;
  }

  /**
   * Возвращает читателя символьного потока данных канала
   *
   * @return {@link IStrioReader} читатель потока
   */
  final IStrioReader inputStreamReader() {
    return strioReader;
  }

  /**
   * Возвращает писателя данных канала
   *
   * @return {@link PasChannelWriter} писатель данных
   */
  final PasChannelWriter channelWriter() {
    return channelWriter;
  }

  /**
   * Запись объект {@link ITjObject} в канале
   *
   * @param aObj {@link ITjObject} - JSON-объект
   * @throws TsNullArgumentRtException аргумент = null
   * @throws IOException ошибка ввода/вывода
   */
  final void writeJsonObject( ITjObject aObj )
      throws IOException {
    TsNullArgumentRtException.checkNull( aObj );
    Thread writingThread = Thread.currentThread();
    synchronized (writingThreads) {
      if( writingThreads.size() == 0 ) {
        lastWritingTimestamp = System.currentTimeMillis();
      }
      writingThreads.add( writingThread );
    }
    try {
      // 2021-06-12
      synchronized (strioWriter) {
        // Запись
        TjUtils.saveObject( strioWriter, aObj );
        // Фактическая передача данных
        out.flush();
      }
    }
    finally {
      synchronized (writingThreads) {
        writingThreads.remove( writingThread );
        if( writingThreads.size() == 0 ) {
          lastWritingTimestamp = MIN_TIMESTAMP;
        }
      }
    }
    // Обновление времени активности на канале
    lastWriteTimestamp = System.currentTimeMillis();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  /**
   * Передача пинг сообщения
   *
   * @param aChannel {@link PasChannel} канал по которому передается сообщение
   * @param aFailureTimeout long таймаут отказа работоспособности передаваемый клиенту
   * @return boolean <b>true</b> передача выполнена;<b>false</b> передача не произошла
   */
  private static boolean sendPing( PasChannel aChannel, long aFailureTimeout ) {
    TsNullArgumentRtException.checkNull( aChannel );
    IStringMapEdit<ITjValue> params = new StringMap<>();
    IAtomicValue channelName = (IAtomicValue)aChannel.context.find( TSID_NAME );
    IAtomicValue channelDescription = (IAtomicValue)aChannel.context.find( TSID_DESCRIPTION );
    if( channelName != null && channelName.isAssigned() ) {
      params.put( JSON_PARAM_NAME, createString( channelName.asString() ) );
    }
    if( channelDescription != null && channelDescription.isAssigned() ) {
      params.put( JSON_PARAM_DESCRIPTION, createString( channelDescription.asString() ) );
    }
    params.put( JSON_PARAM_FAILURE_TIMEOUT, createNumber( aFailureTimeout ) );
    aChannel.sendNotification( JSON_NOTIFY_ALIVE, params );
    // Запись в журнал
    aChannel.logger.debug( MSG_SEND_ALIVE, aChannel, Long.valueOf( aFailureTimeout ) );
    return true;
  }

  /**
   * Обработка уведомления канала
   *
   * @param aChannel {@link PasChannel} канал
   * @param aNotification {@link IJSONNotification} уведомление
   * @return boolean <b>true</b> уведомление принято; <b>false</b> не найден обработчик уведомления
   * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static <CHANNEL extends PasChannel> boolean handleNotification( CHANNEL aChannel,
      IJSONNotification aNotification ) {
    TsNullArgumentRtException.checkNull( aChannel );
    TsNullArgumentRtException.checkNull( aNotification );
    try {
      if( aNotification.method().equals( JSON_NOTIFY_ALIVE ) ) {
        // Уведомление проверки работоспособности канала.
        int failureTimeout = aNotification.params().getByKey( JSON_PARAM_FAILURE_TIMEOUT ).asNumber().intValue();
        // Имя канала
        ITjValue name = aNotification.params().findByKey( JSON_PARAM_NAME );
        // Описание канала
        ITjValue description = aNotification.params().findByKey( JSON_PARAM_DESCRIPTION );
        // Установка таймаута отказа
        if( aChannel.failureTimeout() <= 0 ) {
          // Установка таймаута удаленного клиента
          aChannel.setFailureTimeout( failureTimeout );
        }
        // Запись в журнал
        String n = (name != null ? name.asString() : MSG_UNDEF);
        String d = (name != null ? description.asString() : MSG_UNDEF);
        aChannel.logger().debug( MSG_RECEIVE_ALIVE, aChannel, n, d, Long.valueOf( failureTimeout ) );
        return true;
      }
      // Попытка обработки запроса наследником
      if( doReceiveNotification( aChannel, aNotification ) ) {
        return true;
      }
    }
    catch( Exception e ) {
      // Неожиданная ошибка обработки уведомления. Передача сообщения удаленной стороне
      sendExceptionError( aChannel, aNotification, e );
      throw e;
    }
    IJSONNotificationHandler<PasChannel> notificationHandler =
        aChannel.findNotificationHandler( aNotification.method() );
    if( notificationHandler == null ) {
      // Не найден обработчик уведомления
      int errorCode = JSON_ERROR_CODE_METHOD_NOT_FOUND;
      String errorMsg = String.format( ERR_METHOD_NOT_FOUND, aNotification.method() );
      aChannel.logger().warning( errorMsg );
      aChannel.sendError( errorCode, errorMsg, null );
      return false;
    }
    try {
      // Уведомление
      notificationHandler.notify( aChannel, aNotification );
      return true;
    }
    catch( Exception e ) {
      // Неожиданная ошибка обработки уведомления. Передача сообщения удаленной стороне
      sendExceptionError( aChannel, aNotification, e );
      throw e;
    }
  }

  /**
   * Обработка запросов канала
   *
   * @param aChannel {@link PasChannel} канал
   * @param aRequest {@link IJSONRequest} запрос
   * @return boolean <b>true</b> запрос обработан; <b>false</b> не найден обработчик запроса
   * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static <CHANNEL extends PasChannel> boolean handleRequest( CHANNEL aChannel, IJSONRequest aRequest ) {
    TsNullArgumentRtException.checkNull( aChannel );
    TsNullArgumentRtException.checkNull( aRequest );
    try {
      // Попытка обработки запроса наследником
      JSONResponse response = doReceiveRequest( aChannel, aRequest );
      if( response != null ) {
        // Отправление результата обработки
        writeToChannel( aChannel, response );
        return true;
      }
    }
    catch( Exception e ) {
      // Неожиданная ошибка обработки запроса. Передача сообщения удаленной стороне
      sendExceptionError( aChannel, aRequest, e );
      throw e;
    }
    IJSONRequestHandler<PasChannel> requestHandler = aChannel.findRequestHandler( aRequest.method() );
    if( requestHandler == null ) {
      // Не найден обработчик запроса
      int errorCode = JSON_ERROR_CODE_METHOD_NOT_FOUND;
      String errorMsg = String.format( ERR_METHOD_NOT_FOUND, aRequest.method() );
      aChannel.logger().warning( errorMsg );
      writeToChannel( aChannel, createError( aRequest, errorCode, errorMsg, null ) );
      return false;
    }
    try {
      // Выполнение запроса
      JSONResponse response = requestHandler.execute( aChannel, aRequest );
      // Отправление результата обработки
      writeToChannel( aChannel, response );
      return true;
    }
    catch( Exception e ) {
      // Неожиданная ошибка обработки запроса. Передача сообщения удаленной стороне
      sendExceptionError( aChannel, aRequest, e );
      throw e;
    }
  }

  /**
   * Обработка результатов обработки запросов канала
   *
   * @param aChannel {@link PasChannel} канал
   * @param aResult {@link IJSONResult} результат обработки запроса
   * @return boolean <b>true</b> результат обработан; <b>false</b> не найден обработчик результата запроса
   * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static <CHANNEL extends PasChannel> boolean handleResult( CHANNEL aChannel, IJSONResult aResult ) {
    TsNullArgumentRtException.checkNull( aChannel );
    TsNullArgumentRtException.checkNull( aResult );
    // Поиск запроса
    IJSONRequest request = findAndRemoveRequest( aChannel.executingRequests(), aResult.id() );
    // Попытка обработки результата запроса наследником
    if( doReceiveResult( aChannel, aResult ) ) {
      return true;
    }
    if( request == null ) {
      // НеожидАемое получение ответа
      aChannel.logger().warning( ERR_UNEXPECTED, aResult );
      return false;
    }
    IJSONResultHandler<PasChannel> resultHandler = aChannel.findResultHandler( request.method() );
    if( resultHandler == null ) {
      // Не найден обработчик результатов запроса
      aChannel.logger().debug( ERR_RESULT_HANDLER_NOT_FOUND, request );
      return false;
    }
    // Обработка результатов
    resultHandler.handle( aChannel, request, aResult );
    return true;
  }

  /**
   * Обработка ошибки обработки запросов канала
   *
   * @param aChannel {@link PasChannel} канал
   * @param aError {@link IJSONError} ошибка обработки запроса
   * @return boolean <b>true</b> ошибка обработана; <b>false</b> не найден обработчик ошибки запроса
   * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static <CHANNEL extends PasChannel> boolean handleError( CHANNEL aChannel, IJSONError aError ) {
    TsNullArgumentRtException.checkNull( aChannel );
    TsNullArgumentRtException.checkNull( aError );
    Integer id = aError.id();
    int errorCode = aError.code();
    String errorMessage = aError.message();
    ITjValue errorData = aError.data();
    if( errorCode == JSON_ERROR_CODE_PARSE ) {
      // Удаленная сторона сообщает об нарушении формата запроса
      aChannel.logger().error( ERR_REMOTE_FORMAT, errorMessage, errorData );
    }
    if( errorCode == JSON_ERROR_CODE_INTERNAL ) {
      // Удаленная сторона сообщает об неожиданной(необработанной должным образом) ошибке обработки запроса
      aChannel.logger().error( ERR_REMOTE_UNEXPECTED, errorMessage, errorData );
    }
    IJSONRequest request = (id != null ? findAndRemoveRequest( aChannel.executingRequests(), id.intValue() ) : null);
    // Попытка обработки результата запроса наследником
    if( doReceiveError( aChannel, aError ) ) {
      return true;
    }
    if( id == null ) {
      return false;
    }
    if( request == null ) {
      // НеожидАемое получение ошибки
      aChannel.logger().warning( ERR_UNEXPECTED, aError );
      return false;
    }
    IJSONErrorHandler<PasChannel> errorHandler = aChannel.findErrorHandler( request.method() );
    if( errorHandler == null ) {
      // Не найден обработчик результатов запроса
      aChannel.logger().debug( ERR_RESULT_HANDLER_NOT_FOUND, request );
      return false;
    }
    // Обработка ошибки
    errorHandler.handle( aChannel, request, aError );
    return true;
  }

  /**
   * Передача удаленной стороне канала сообщения об неожиданной(необработанная должным образом) ошибке обработки запроса
   *
   * @param aChannel {@link PasChannel} aChannel канал по которому произошла ошибка
   * @param aRequest {@link IJSONRequest} запрос при обработке которого возникла ошибка
   * @param aError {@link Exception} ошибка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static void sendExceptionError( PasChannel aChannel, IJSONRequest aRequest, Exception aError ) {
    TsNullArgumentRtException.checkNull( aChannel );
    TsNullArgumentRtException.checkNull( aRequest );
    TsNullArgumentRtException.checkNull( aError );
    // Неожиданная(необработанная должным образом) ошибка обработки запроса
    String message = String.format( ERR_REQUEST_UNEXPECTED, aRequest, cause( aError ) );
    int code = JSON_ERROR_CODE_INTERNAL;
    ITjValue data = TjUtils.createString( errorStackToString( aError ) );
    writeToChannel( aChannel, createError( aRequest, code, message, data ) );
  }

  /**
   * Передача удаленной стороне канала сообщения об неожиданной(необработанная должным образом) ошибке обработки
   * уведомления
   *
   * @param aChannel {@link PasChannel} aChannel канал по которому произошла ошибка
   * @param aNotification {@link IJSONNotification} уведомление при обработке которого возникла ошибка
   * @param aError {@link Exception} ошибка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static void sendExceptionError( PasChannel aChannel, IJSONNotification aNotification, Exception aError ) {
    TsNullArgumentRtException.checkNull( aChannel );
    TsNullArgumentRtException.checkNull( aNotification );
    TsNullArgumentRtException.checkNull( aError );
    // Неожиданная(необработанная должным образом) ошибка обработки запроса
    String message = String.format( ERR_NOTIFICATION_UNEXPECTED, aNotification, cause( aError ) );
    int code = JSON_ERROR_CODE_INTERNAL;
    ITjValue data = TjUtils.createString( errorStackToString( aError ) );
    aChannel.sendError( code, message, data );
  }

  /**
   * Оповещение наследников об ошибке на канале
   *
   * @param aChannel {@link PasChannel} канал
   * @param aHandler {@link IPasChannelHandler} обработчик событий канала
   * @param aError {@link Throwable} ошибка
   * @param aLogger {@link ILogger} журнал работы
   * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
   */
  private static <CHANNEL extends PasChannel> void handlerErrorNotify( CHANNEL aChannel,
      IPasChannelHandler<PasChannel> aHandler, Throwable aError, ILogger aLogger ) {
    try {
      // Оповещение об ошибке обработчика канала
      aHandler.onReceiveError( aChannel, aError );
    }
    catch( Throwable e2 ) {
      aLogger.error( e2 );
    }
  }

  /**
   * БЛОКИРУЮЩЕЕ чтение из канала следующего сообщения
   *
   * @param aChannel {@link PasChannel} канал из которого производится чтение сообщения
   * @return {@link IJSONMessage} прочитанное JSON сообщение. null: ошибка чтения сообщения (уже обработана)
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static IJSONMessage readFromChannel( PasChannel aChannel ) {
    TsNullArgumentRtException.checkNull( aChannel );
    ILogger logger = aChannel.logger();
    IPasChannelHandler<PasChannel> handler = aChannel.channelHandler();
    IJSONMessage retValue = null;
    try {
      // Чтение сообщения из канала
      ITjObject jsonObj = TjUtils.loadObject( aChannel.inputStreamReader() );
      // Обновление времени активности на канале
      aChannel.lastReadTimestamp = System.currentTimeMillis();
      // Типизация сообщения
      if( jsonObj.fields().hasKey( IJSONSpecification.SPEC_FIELD_METHOD ) ) {
        if( jsonObj.fields().hasKey( IJSONSpecification.SPEC_FIELD_ID ) ) {
          // По спецификации запросы имеют имя метода и идентификатор запроса
          retValue = new JSONRequest( jsonObj );
        }
        else {
          // По спецификации запросы имеют имя метода, но не имеют идентификатор запроса
          retValue = new JSONNotification( jsonObj );
        }
      }
      if( retValue == null && jsonObj.fields().hasKey( IJSONSpecification.SPEC_FIELD_RESULT ) ) {
        retValue = new JSONResult( jsonObj );
      }
      if( retValue == null && jsonObj.fields().hasKey( IJSONSpecification.SPEC_FIELD_ERROR ) ) {
        retValue = new JSONError( jsonObj );
      }
      // По каналу получено сообщение
      if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
        logger.debug( MSG_RECEIVE_MESSAGE, aChannel, retValue );
      }
      try {
        if( retValue != null ) {
          handler.onReceived( aChannel, retValue );
        }
      }
      catch( Throwable e ) {
        logger.error( e );
      }
    }
    catch( TsIoRtException e ) {
      // Обнаружен разрыв соединения
      logger.error( ERR_BREAK_CONNECTION, aChannel, cause( e ) );
      // Оповещение наследника об ошибке
      handlerErrorNotify( aChannel, handler, e, logger );
    }
    catch( Throwable e ) {
      // Ошибка чтения объекта из потока канала
      String message = String.format( ERR_READ_CHANNEL, aChannel, cause( e ) );
      logger.error( e, message );
      try {
        // Ошибка не связана с потерей соединения, то отправляем ответ об ошибке разбора
        aChannel.sendError( JSON_ERROR_CODE_PARSE, message, TjUtils.createString( errorStackToString( e ) ) );
      }
      catch( Throwable e2 ) {
        logger.error( e2 );
      }
      // Оповещение наследника об ошибке
      handlerErrorNotify( aChannel, handler, e, logger );
    }
    return retValue;
  }

  /**
   * БЛОКИРУЮЩАЯ запись в канал следующего сообщения
   *
   * @param aChannel {@link PasChannel} канал в который производится запись сообщения
   * @param aMessage {@link JSONMessage} сообщение
   * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static <CHANNEL extends PasChannel> void writeToChannel( CHANNEL aChannel, JSONMessage aMessage ) {
    TsNullArgumentRtException.checkNull( aChannel );
    TsNullArgumentRtException.checkNull( aMessage );
    ILogger logger = aChannel.logger();
    IPasChannelHandler<PasChannel> handler = aChannel.channelHandler();
    try {
      // Запись через писателя
      aChannel.channelWriter().writeJsonObject( aMessage.value() );
      // По каналу передано сообщение
      if( logger.isSeverityOn( ELogSeverity.DEBUG ) ) {
        logger.debug( MSG_SEND_MESSAGE, aChannel, aMessage );
      }
      try {
        handler.onSended( aChannel, aMessage );
      }
      catch( Throwable e ) {
        logger.error( e );
      }
    }
    catch( Throwable e ) {
      // Ошибка записи объекта в поток канала
      logger.error( e, ERR_WRITE_CHANNEL, aMessage, aChannel, cause( e ) );
      try {
        handler.onSendError( aChannel, aMessage, e );
      }
      catch( Throwable e2 ) {
        logger.error( e2 );
      }
    }
  }

  /**
   * Наследник может подготовиться к обслуживанию клиента.
   *
   * @param aChannel {@link PasChannel} канал
   * @return boolean - признак завершения работы канала
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static boolean doInit( PasChannel aChannel ) {
    TsNullArgumentRtException.checkNulls( aChannel );
    try {
      return aChannel.doInit();
    }
    catch( Throwable e ) {
      aChannel.logger.error( e );
      return false;
    }
  }

  /**
   * Прием уведомления (не требующего ответа) на обработку от удаленной стороны соединения.
   *
   * @param aChannel {@link PasChannel} канал
   * @param aNotification {@link IJSONRequest} принятый запрос
   * @return boolean <b>true</b> уведомление обработано; <b>false</b> уведомление не обработано
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static boolean doReceiveNotification( PasChannel aChannel, IJSONNotification aNotification ) {
    TsNullArgumentRtException.checkNulls( aChannel, aNotification );
    try {
      return aChannel.doReceiveNotification( aNotification );
    }
    catch( Throwable e ) {
      aChannel.logger.error( e );
      return false;
    }
  }

  /**
   * Прием запроса на обработку от удаленной стороны соединения.
   *
   * @param aChannel {@link PasChannel} канал
   * @param aRequest {@link IJSONRequest} принятый запрос
   * @return {@link JSONResponse} ответ (результат или ошибка) обработки запроса. null: запрос не обработан
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static JSONResponse doReceiveRequest( PasChannel aChannel, IJSONRequest aRequest ) {
    TsNullArgumentRtException.checkNulls( aChannel, aRequest );
    try {
      return aChannel.doReceiveRequest( aRequest );
    }
    catch( Throwable e ) {
      aChannel.logger.error( e );
      return null;
    }
  }

  /**
   * Прием ответа обработки запроса от удаленной стороны соединения.
   *
   * @param aChannel {@link PasChannel} канал
   * @param aResult {@link IJSONResult} принятый результат
   * @return boolean <b>true</b> результаты запроса обработаны; <b>false</b> результаты запроса не обработаны
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static boolean doReceiveResult( PasChannel aChannel, IJSONResult aResult ) {
    TsNullArgumentRtException.checkNulls( aChannel, aResult );
    try {
      return aChannel.doReceiveResult( aResult );
    }
    catch( Throwable e ) {
      aChannel.logger.error( e );
      return false;
    }
  }

  /**
   * Прием ошибки обработки запроса от удаленной стороны соединения.
   *
   * @param aChannel {@link PasChannel} канал
   * @param aError {@link IJSONResult} принятая ошибка
   * @return boolean <b>true</b> ошибка запроса обработана; <b>false</b> ошибка запроса не обработана
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static boolean doReceiveError( PasChannel aChannel, IJSONError aError ) {
    TsNullArgumentRtException.checkNulls( aChannel, aError );
    try {
      return aChannel.doReceiveError( aError );
    }
    catch( Throwable e ) {
      aChannel.logger.error( e );
      return false;
    }
  }

  /**
   * Безопасное завершение работы канала
   *
   * @param aChannel {@link PasChannel} канал
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static void close( PasChannel aChannel ) {
    TsNullArgumentRtException.checkNull( aChannel );
    // Закрываем каналы
    try {
      aChannel.in.close();
    }
    catch( IOException e ) {
      aChannel.logger.error( e );
    }
    try {
      aChannel.out.close();
    }
    catch( IOException e ) {
      aChannel.logger.error( e );
    }
    // // Закрываем потоки сокета
    // try {
    // aChannel.socket.shutdownInput();
    // }
    // catch( IOException e ) {
    // aChannel.logger.error( e );
    // }
    // // Закрываем потоки сокета
    // try {
    // aChannel.socket.shutdownOutput();
    // }
    // catch( IOException e ) {
    // aChannel.logger.error( e );
    // }
    // Закрываем сокет
    try {
      aChannel.socket.close();
    }
    catch( IOException e ) {
      aChannel.logger.error( e );
    }
  }

  /**
   * Возвращает и удаляет из указанного списка JSON-запрос с указанным идентификатором или null если запрос не найден
   *
   * @param aRequests {@link IStringMap}&lt;{@link IJSONRequest}&gt; редактируемая карта запросов по методам.<br>
   *          Ключ: Имя метода запроса;<br>
   *          Значение: JSON-запрос переданный на выполнение.
   * @param aRequestId int идентификатор запроса
   * @return {@link IJSONRequest} найденный запрос. null: запрос не найден
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static IJSONRequest findAndRemoveRequest( IStringMapEdit<IJSONRequest> aRequests, int aRequestId ) {
    TsNullArgumentRtException.checkNull( aRequests );
    for( IJSONRequest request : aRequests ) {
      if( request.id() == aRequestId ) {
        aRequests.removeByKey( request.method() );
        return request;
      }
    }
    return null;
  }

  /**
   * Возвращает имя потока для канала с указанным сокетом
   *
   * @param aSocket {@link Socket} сокет
   * @return String имя потока
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static String makeThreadName( Socket aSocket ) {
    if( aSocket == null ) {
      throw new TsNullArgumentRtException();
    }
    StringBuilder sb = new StringBuilder();
    Long now = Long.valueOf( System.currentTimeMillis() );
    sb.append( String.format( "PAS reader [remote = %s, local = %s] %tF %tT", //$NON-NLS-1$
        aSocket.getRemoteSocketAddress(), aSocket.getLocalSocketAddress(), now, now ) );
    return sb.toString();
  }

  /**
   * Возвращает текстовое представления стека ошибки
   *
   * @param aError {@link Throwable} ошибка
   * @return String текстовое представление
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static String errorStackToString( Throwable aError ) {
    TsNullArgumentRtException.checkNull( aError );
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter( sw );
    aError.printStackTrace( pw );
    return sw.toString();
  }
}
