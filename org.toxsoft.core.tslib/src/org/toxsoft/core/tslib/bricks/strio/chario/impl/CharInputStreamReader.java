package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

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
  // ICharInputStream
  //

  @Override
  public int doReadChar()
      throws IOException {
    // считаем очередной символ, был ли при этом EOF?
    if( reader.read( cbuf ) == -1 ) {
      return -1;
    }
    return cbuf[0];
  }

}
