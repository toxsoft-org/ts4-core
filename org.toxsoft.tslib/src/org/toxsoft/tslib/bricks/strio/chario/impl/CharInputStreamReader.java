package org.toxsoft.tslib.bricks.strio.chario.impl;

import java.io.IOException;
import java.io.Reader;

import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Character stream reader from {@link Reader}.
 *
 * @author hazard157
 */
public final class CharInputStreamReader
    extends AbstractCharInputStream {

  private final char[] cbuf = new char[1];
  private final Reader reader;

  /**
   * Constructor.
   *
   * @param aReader {@link Reader} - input stream
   */
  @SuppressWarnings( "resource" )
  public CharInputStreamReader( Reader aReader ) {
    super( TsNullArgumentRtException.checkNull( aReader ).toString() );
    reader = aReader;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICharInputStream
  //

  @Override
  public int doReadChar()
      throws IOException {
    // есть входные данные?
    if( !reader.ready() ) {
      return -1;
    }
    // считаем очередной символ, был ли при этом EOF?
    if( reader.read( cbuf ) == -1 ) {
      return -1;
    }
    return cbuf[0];
  }

}
