package org.toxsoft.core.pas.common;

import java.io.*;
import java.nio.CharBuffer;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Читатель потока с возможностью журналирования принятых данных
 *
 * @author mvk
 */
class PasLoggableReader
    extends Reader {

  private final Reader source;
  private IPasIoLogger logger;

  /**
   * Конструктор
   *
   * @param aSource {@link Reader} читатель потока
   * @throws TsNullArgumentRtException аргумент = null
   */
  PasLoggableReader( Reader aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //
  /**
   * Установить журнал чтения
   *
   * @param aLogger {@link IPasIoLogger} журнал. null: отключить журналирование
   */
  void setIoLoggerOrNull( IPasIoLogger aLogger ) {
    logger = aLogger;
  }

  // ------------------------------------------------------------------------------------
  // Reader
  //
  @Override
  public int read()
      throws IOException {
    int retValue = source.read();
    if( logger != null ) {
      logger.readChar( (char)retValue );
    }
    return retValue;
  }

  @Override
  public int read( char[] cbuf, int off, int len )
      throws IOException {
    if( logger != null ) {
      for( int index = off; index < len; index++ ) {
        logger.readChar( cbuf[index] );
      }
    }
    return source.read( cbuf, off, len );
  }

  @Override
  public int read( CharBuffer target )
      throws IOException {
    if( logger != null ) {
      for( int index = 0, n = target.length(); index < n; index++ ) {
        logger.readChar( target.get( index ) );
      }
    }
    return source.read( target );
  }

  @Override
  public boolean ready()
      throws IOException {
    return source.ready();
  }

  @Override
  public long skip( long n )
      throws IOException {
    return source.skip( n );
  }

  @Override
  public long transferTo( Writer out )
      throws IOException {
    return source.transferTo( out );
  }

  @Override
  public void close()
      throws IOException {
    source.close();
  }

}
