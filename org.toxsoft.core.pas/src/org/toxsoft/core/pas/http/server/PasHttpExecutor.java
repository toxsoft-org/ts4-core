package org.toxsoft.core.pas.http.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.toxsoft.core.log4j.Logger;
import org.toxsoft.core.tslib.utils.ICloseable;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.ILogger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

/**
 * Исполнитель запросов http
 * <p>
 * Источник:
 * https://overcoder.net/q/840728/java-%D0%BF%D1%80%D0%BE%D1%81%D1%82%D0%BE%D0%B5-%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5-http-server-%D0%BA%D0%BE%D1%82%D0%BE%D1%80%D0%BE%D0%B5-%D0%BE%D1%82%D0%B2%D0%B5%D1%87%D0%B0%D0%B5%D1%82-%D0%B2-%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%82%D0%B5-json
 *
 * @author mvk
 */
class PasHttpExecutor
    implements ICloseable {

  private static final int     BACKLOG                   = 1;
  private static final int     STOP_DELAY                = 10000;
  private static final int     READ_DATA_TIMEOUT_DEFAULT = 5000;
  private static final Charset CHARSET                   = StandardCharsets.UTF_8;
  private static final String  ROOT_URI_PATH             = "/";                   //$NON-NLS-1$

  private static final String HEADER_ALLOW        = "Allow";        //$NON-NLS-1$
  private static final String HEADER_CONTENT_TYPE = "Content-Type"; //$NON-NLS-1$

  private static final int STATUS_OK                 = 200;
  private static final int STATUS_METHOD_NOT_ALLOWED = 405;

  private static final int NO_RESPONSE_LENGTH = -1;

  private static final String METHOD_POST     = "POST";     //$NON-NLS-1$
  private static final String ALLOWED_METHODS = METHOD_POST;

  private final HttpServer http;
  private final String     pasHost;
  private final int        pasPort;
  private final int        readDataTimeout;

  private int     requestCount;
  private int     errorCount;
  private ILogger logger = Logger.getLogger( getClass() );

  /**
   * Конструктор
   *
   * @param aHttpHost String хост на котором работает http-сервер
   * @param aHttpPort int порт который обслуживает http-сервер
   * @param aPasHost String хост на котором работает pas-сервер
   * @param aPasPort int порт который обслуживает pas-сервер
   * @param aReadDataTimeout int (мсек) таймаут ожидания данных сервера. 0 <= автоматический выбор
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  PasHttpExecutor( String aHttpHost, int aHttpPort, String aPasHost, int aPasPort, int aReadDataTimeout ) {
    TsNullArgumentRtException.checkNulls( aHttpHost, aPasHost );
    try {
      pasHost = aPasHost;
      pasPort = aPasPort;
      readDataTimeout = (aReadDataTimeout >= 0 ? aReadDataTimeout : READ_DATA_TIMEOUT_DEFAULT);
      // Создание сервера
      http = HttpServer.create( new InetSocketAddress( aHttpHost, aHttpPort ), BACKLOG );
      // Установка обработчика для URI
      createHttpContext( this );
    }
    catch( IOException e ) {
      logger.error( e );
      throw new TsIllegalArgumentRtException( e );
    }
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //
  /**
   * Запуск сервера
   * <p>
   * Метод блокирует поток выполнения до вызова {@link #close()}
   */
  void start() {
    http.start();
    synchronized (http) {
      try {
        http.wait();
      }
      catch( InterruptedException e ) {
        logger.error( e );
      }
    }
  }

  /**
   * Возвращает количество выполненных запросв
   *
   * @return int количество обработанных запросов
   */
  int requestCount() {
    return requestCount;
  }

  /**
   * Возвращает количество ошибок обработки запросов
   *
   * @return int количество ошибок
   */
  int errorCount() {
    return errorCount;
  }

  // ------------------------------------------------------------------------------------
  // IClosable
  //
  @Override
  public void close() {
    http.stop( STOP_DELAY );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  @SuppressWarnings( "resource" )
  private static void createHttpContext( PasHttpExecutor aServer ) {
    TsNullArgumentRtException.checkNull( aServer );
    aServer.http.createContext( ROOT_URI_PATH, he -> {
      try {
        final Headers headers = he.getResponseHeaders();
        final String requestMethod = he.getRequestMethod().toUpperCase();
        String requestBody = TsLibUtils.EMPTY_STRING;
        try {
          try( InputStream is = he.getRequestBody() ) {
            requestBody = readString( is, aServer.readDataTimeout );
          }
        }
        catch( Throwable e ) {
          aServer.errorCount++;
          aServer.logger.error( "can't read requestBody. cause: %s", e.getLocalizedMessage() ); //$NON-NLS-1$
          headers.set( HEADER_ALLOW, ALLOWED_METHODS );
          he.sendResponseHeaders( STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH );
          return;
        }
        switch( requestMethod ) {
          case METHOD_POST:
            aServer.logger.info( "receive = %s, content = %s", requestMethod, requestBody ); //$NON-NLS-1$
            aServer.requestCount++;

            Socket socket = null;
            try {
              socket = new Socket( aServer.pasHost, aServer.pasPort );
              aServer.logger.info( "connect to %s", socket ); //$NON-NLS-1$
            }
            catch( Throwable e ) {
              // Нет связи с pas
              aServer.errorCount++;
              aServer.logger.error( "can't create pas connect. cause: %s", e.getLocalizedMessage() ); //$NON-NLS-1$
              headers.set( HEADER_ALLOW, ALLOWED_METHODS );
              he.sendResponseHeaders( STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH );
              break;
            }
            try {
              socket.getOutputStream().write( requestBody.getBytes( CHARSET ) );
              aServer.logger.info( "write request to pas = %s", requestBody ); //$NON-NLS-1$
              String responseBody = TsLibUtils.EMPTY_STRING;
              try {
                try( InputStream is = he.getRequestBody() ) {
                  responseBody = readString( socket.getInputStream(), aServer.readDataTimeout );
                }
              }
              catch( Throwable e ) {
                aServer.errorCount++;
                aServer.logger.error( "can't read responseBody. cause: %s", e.getLocalizedMessage() ); //$NON-NLS-1$
                headers.set( HEADER_ALLOW, ALLOWED_METHODS );
                he.sendResponseHeaders( STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH );
                break;
              }
              aServer.logger.info( "read response from pas = %s", responseBody ); //$NON-NLS-1$
              headers.set( HEADER_CONTENT_TYPE, String.format( "application/json; charset=%s", CHARSET.name() ) ); //$NON-NLS-1$
              final byte[] rawResponseBody = responseBody.getBytes( CHARSET );
              he.sendResponseHeaders( STATUS_OK, rawResponseBody.length );
              try( OutputStream os = he.getResponseBody() ) {
                os.write( rawResponseBody );
              }
            }
            finally {
              socket.close();
            }
            break;
          default:
            aServer.logger.warning( "unknow request method: %s (expected %s)", requestMethod, METHOD_POST ); //$NON-NLS-1$
            headers.set( HEADER_ALLOW, ALLOWED_METHODS );
            he.sendResponseHeaders( STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH );
            break;
        }
      }
      finally {
        he.close();
      }
    } );
  }

  /**
   * Чтение строки из потока
   *
   * @param aStream {@link InputStream} поток чтения
   * @param aReadTimeout int (мсек) таймаут ожидания данных сервера
   * @return String строка
   * @throws TsNullArgumentRtException аргумент = null
   */
  @SuppressWarnings( "resource" )
  private static String readString( InputStream aStream, int aReadTimeout ) {
    TsNullArgumentRtException.checkNull( aStream );
    try {
      InputStreamReader inputStreamReader = new InputStreamReader( aStream, CHARSET.name() );
      int nested = 0;
      long breakTime = 0;
      StringBuilder sb = new StringBuilder();
      while( true ) {
        if( !inputStreamReader.ready() ) {
          if( breakTime == 0 ) {
            breakTime = System.currentTimeMillis();
          }
          if( System.currentTimeMillis() - breakTime > aReadTimeout ) {
            throw new TsIoRtException( "timeout (%d msec) error", Integer.valueOf( aReadTimeout ) ); //$NON-NLS-1$
          }
          try {
            Thread.sleep( 100 );
          }
          catch( InterruptedException e ) {
            throw new TsIllegalArgumentRtException( e );
          }
          continue;
        }
        breakTime = 0;
        int ch = inputStreamReader.read();
        if( ch == -1 ) {
          break;
        }
        sb.append( (char)ch );
        if( ch == '{' ) {
          nested++;
        }
        if( ch == '}' ) {
          nested--;
          if( nested == 0 ) {
            break;
          }
        }
      }
      return sb.toString();
    }
    catch( IOException e ) {
      throw new TsIoRtException( e );
    }
  }
}
