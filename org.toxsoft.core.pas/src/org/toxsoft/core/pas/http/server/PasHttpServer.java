package org.toxsoft.core.pas.http.server;

import static org.toxsoft.core.pas.http.server.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.log4j.*;
import org.toxsoft.core.pas.common.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;

/**
 * Сервер обслуживания запросов от клиентов публичного API.
 *
 * @author mvk
 */
public class PasHttpServer
    implements ICooperativeMultiTaskable, ICloseable {

  /**
   * Версия API сервера
   */
  public static final int API_VERSION = 1;

  // ------------------------------------------------------------------------------------
  // Параметры подключения к PAS (Public Access Server), считываемые с файла конфигурации
  private static final String CFG_SECTION_PAS          = "pas";             //$NON-NLS-1$
  private static final String CFG_PAS_HOST             = "HostOrIpAddress"; //$NON-NLS-1$
  private static final String CFG_PAS_PORT             = "PortNo";          //$NON-NLS-1$
  private static final String CFG_PAS_READ_TIMEOUT     = "ReadTimeout";     //$NON-NLS-1$
  private static final String DEFAULT_PAS_HOST         = "127.0.0.1";       //$NON-NLS-1$
  private static final String DEFAULT_PAS_PORT         = "2194";            //$NON-NLS-1$
  private static final String DEFAULT_PAS_READ_TIMEOUT = "5000";            //$NON-NLS-1$

  /**
   * Таймаут (мсек) подключения к PAS-серверу
   */
  static final IAtomicValue PAS_CREATE_TIMEOUT = avInt( 3000 );

  /**
   * Таймаут (мсек) отказа PAS-сервера
   */
  static final int PAS_FAILURE_TIMEOUT = 10000;

  /**
   * Таймаут (мсек) ожидания данных PAS-сервера
   */
  static final int PAS_REQUEST_WAIT_TIMEOUT = 60000;

  // ------------------------------------------------------------------------------------
  // Параметры обработки запросов от табло
  private static final String CFG_SECTION_HTTP  = "http";            //$NON-NLS-1$
  private static final String CFG_HTTP_HOST     = "HostOrIpAddress"; //$NON-NLS-1$
  private static final String CFG_HTTP_PORT     = "PortNo";          //$NON-NLS-1$
  private static final String DEFAULT_HTTP_HOST = "127.0.0.1";       //$NON-NLS-1$
  private static final String DEFAULT_HTTP_PORT = "2195";            //$NON-NLS-1$

  /**
   * Таймаут (мсек) выполнения doJob по умолчанию
   */
  private static final long DOJOB_TIMEOUT = 1000;

  /**
   * Конфигурационные параметры, задаваемые в конструкторе.
   */
  private final IAppPreferences appPrefs;

  /**
   * PAS-клиент сервера обслуживающий запросы клиентов.
   */
  // private PasClient<PasClientChannel> pasClient = null;

  /**
   * HTTP-сервер
   */
  private PasHttpExecutor httpServer = null;

  /**
   * Фоновая задача сервера
   */
  private final PasDoJob doJobThread;

  /**
   * Журнал работы
   */
  private static final ILogger logger = LoggerWrapper.getLogger( PasHttpServer.class );

  /**
   * Конструктор.
   * <p>
   * Конструктор не выполняет никакой работы.
   *
   * @param aAppPrefs {@link IAppPreferences} - параметры сервера
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PasHttpServer( IAppPreferences aAppPrefs ) {
    appPrefs = TsNullArgumentRtException.checkNull( aAppPrefs );
    doJobThread = new PasDoJob( "server doJob thread", this, DOJOB_TIMEOUT, logger ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  private void init()
      throws Exception {
    try {
      // Попытка создать pas-сервер
      IPrefBundle httpBundle = appPrefs.getBundle( CFG_SECTION_HTTP );
      String httpHost = httpBundle.prefs().getStr( CFG_HTTP_HOST, DEFAULT_HTTP_HOST );
      int httpPort = Integer.parseInt( httpBundle.prefs().getStr( CFG_HTTP_PORT, DEFAULT_HTTP_PORT ) );
      IPrefBundle pasBundle = appPrefs.getBundle( CFG_SECTION_PAS );
      String pasHost = pasBundle.prefs().getStr( CFG_PAS_HOST, DEFAULT_PAS_HOST );
      int pasPort = Integer.parseInt( pasBundle.prefs().getStr( CFG_PAS_PORT, DEFAULT_PAS_PORT ) );
      int pasTimeout = Integer.parseInt( pasBundle.prefs().getStr( CFG_PAS_READ_TIMEOUT, DEFAULT_PAS_READ_TIMEOUT ) );
      httpServer = new PasHttpExecutor( httpHost, httpPort, pasHost, pasPort, pasTimeout );
    }
    catch( Throwable e ) {
      logger.error( e );
    }
    // Создание фонового потока
    Thread thread = new Thread( doJobThread );
    thread.start();
  }

  // ------------------------------------------------------------------------------------
  // IMmPasProvider
  //
  // @Override
  // public PasClient<PasClientChannel> createClientOrNull() {
  // try {
  // IPrefBundle pasBundle = appPrefs.getBundle( CFG_SECTION_PAS );
  // String host = pasBundle.prefs().getStr( CFG_PAS_HOST, DEFAULT_PAS_HOST );
  // int port = Integer.parseInt( pasBundle.prefs().getStr( CFG_PAS_PORT, DEFAULT_PAS_PORT ) );
  //
  // ITsContext ctx = new TsContext();
  // // ctx.put( S5CallbackClient.class, aReader );
  // OP_PAS_SERVER_ADDRESS.setValue( ctx.params(), avStr( host ) );
  // OP_PAS_SERVER_PORT.setValue( ctx.params(), avInt( port ) );
  // // Имя канала
  // String channelName = host.toString() + ':' + port;
  // // Идентификация канала (узел-сессия)
  // OP_NAME.setValue( ctx.params(), dvStr( channelName ) );
  // OP_DESCRIPTION.setValue( ctx.params(), dvStr( channelName ) );
  // // Установка таймаутов
  // IPasParams.OP_PAS_WRITE_TIMEOUT.setValue( ctx.params(), avInt( PAS_FAILURE_TIMEOUT ) );
  // IPasParams.OP_PAS_FAILURE_TIMEOUT.setValue( ctx.params(), avInt( PAS_FAILURE_TIMEOUT ) );
  // // aExternalDoJobCall = false: создавать внутренний поток для doJob
  // PasClient<PasClientChannel> retValue =
  // new PasClient<>( ctx, PasClientChannel.CREATOR, false, LoggerWrapper.getLogger( "PAS" ) ); //$NON-NLS-1$
  //
  // // Запуск потока
  // Thread thread = new Thread( retValue );
  // thread.start();
  // // Ожидание подключения
  // long startTime = System.currentTimeMillis();
  // while( retValue.getChannelOrNull() == null ) {
  // if( System.currentTimeMillis() - startTime > PAS_CREATE_TIMEOUT.asInt() ) {
  // // Ошибка подключения по таймату
  // logger.error( "can't connect to pas (timeout error %d)", PAS_CREATE_TIMEOUT ); //$NON-NLS-1$
  // retValue.close();
  // return null;
  // }
  // Thread.sleep( 10 );
  // }
  // return retValue;
  // }
  // catch( Throwable e ) {
  // logger.error( e );
  // return null;
  // }
  // }

  // ------------------------------------------------------------------------------------
  // ICooperativeMultiTaskable
  //
  @Override
  public void doJob() {
    logger.info( MSG_SERVER_DOJOB, Integer.valueOf( httpServer.requestCount() ),
        Integer.valueOf( httpServer.errorCount() ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //

  @Override
  public void close() {
    doJobThread.close();
    if( httpServer != null ) {
      httpServer.close();
      httpServer = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //
  /**
   * Запускает выполнение сервера.
   *
   * @throws Exception ошибка при инициализации или выполнении
   */
  public void start()
      throws Exception {
    init();
    httpServer.start();
  }
}
